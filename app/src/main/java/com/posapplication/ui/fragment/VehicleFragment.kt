package com.posapplication.ui.fragment


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.posapplication.R
import com.posapplication.Util.ApiInterface
import com.posapplication.Util.Const
import com.posapplication.Util.ServiceBuilder
import com.posapplication.Util.Utils
import com.posapplication.databinding.FragmentVehicleBinding
import com.posapplication.ui.Activity.MainActivity
import com.posapplication.ui.Adapter.VehicleAdapter
import kotlinx.coroutines.*
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class VehicleFragment : BaseFragment()
{
    var filename=""
    var imageUrl = ""
    var photoFile: File? = null
    val CAPTURE_IMAGE_REQUEST = 1
    var mCurrentPhotoPath: String? = null
    private val FILE_SELECT_CODE = 0
    private val CAMERA_PIC_REQUEST = 0
    var path : String? = ""
    var vehicleno= ""
    var vehicleAdapter: VehicleAdapter? = null
    var email: String =""
    var amount: String =""
    var optionchoose: String? =null
    var deviceid: String? =null
    private var strDeviceId: String = ""
    var vehicleList = ArrayList<String>()
    var isEmailValidate = false
    var networksBroadcast:NetworksBroadcast= NetworksBroadcast()
    var binding:FragmentVehicleBinding ? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).actionBar(getString(R.string.enter_detail), isActionBar = true, isBack = false)
        if (binding == null) binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_vehicle, container, false)
        return binding!!.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI()
    {

        optionchoose = activity?.intent?.getStringExtra(Const.OPTION_CHOOSE)
        strDeviceId= Utils.getPOSdeviceid(requireContext(), Const.POS_ID)
        var card_no = Const.CREDIT_CARD_NO


        var lat =   Utils.getLatitudeLocation(requireContext(), "latitude")
        var long =    Utils.getLongitudeLocation(requireContext(), "longitude")


        if(networksBroadcast.isOnline(requireContext()))
        {
            baseActivity?.showDialogLoader()
            getvehicleList(strDeviceId,lat,long,card_no)
        }
        else
        {
            Toast.makeText(requireContext(),resources.getString(R.string.no_internet),Toast.LENGTH_SHORT).show()
        }



        binding!!.submitBT.setOnClickListener(View.OnClickListener {
            amount = binding?.amountET?.text.toString()
            if (validation())
            {

                if(networksBroadcast.isOnline(requireContext()))
                {
                    baseActivity?.showDialogLoader()
                    setvehiclevalue(strDeviceId, card_no,  amount, lat, long, vehicleno,imageUrl)
                }
                else
                {
                    Toast.makeText(requireContext(),resources.getString(R.string.no_internet),Toast.LENGTH_SHORT).show()
                }
            }
        })



        binding!!.takePictureBT.setOnClickListener {
            captureImage()
//            showFileChooser()
        }

    }

    private fun captureImage()
    {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                baseActivity!!,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
        else
        {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireContext().packageManager) != null)
            {
                // Create the File where the photo should go
                try
                {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null)
                    {
                        val photoURI = FileProvider.getUriForFile(requireContext(),
                            "com.posapplication.fileprovider", photoFile!!)
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)


                    }
                } catch (ex: Exception)
                {
                    // Error occurred while creating the File
                    baseActivity?.showError( ex.message.toString())
                }

            } else {
                baseActivity?.showError( "Null")

            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File
    {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK)
        {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            binding?.takePictureBT?.setImageBitmap(myBitmap)
            try {
               var name = File(photoFile!!.absolutePath).name
                filename= name.toString()
                val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                val converetdImage = getResizedBitmap(myBitmap, 200)
                val outputStream = ByteArrayOutputStream()
                converetdImage?.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
                val byteArray = outputStream.toByteArray()
                //     Log.e("byte", " $byteArray")
                baseActivity!!.log("byte" + byteArray.size)
                imageUrl = Base64.encodeToString(byteArray, Base64.DEFAULT)
                imageUrl =
                    "data:image/png;base64," + imageUrl.replace(" ", "").replace("\n", "")

//                apiUploadProfilePic(filename.toString())

            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            baseActivity?.showToast( "Request cancelled")

        }
    }


    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }



//    @Nullable
//    fun createCopyAndReturnRealPath(@NonNull context: Context, @NonNull uri: Uri?): String? {
//        val contentResolver: ContentResolver = context.getContentResolver()
//            ?: return null
//        var extension = ""
//        val file1 = DocumentFile.fromSingleUri(context, uri!!)
//        val fileName = file1!!.name
//
//        //     String filePath = context.getApplicationInfo().dataDir + File.separator +  System.currentTimeMillis() + extension;
//        val filePath: String = context.getApplicationInfo().dataDir.toString() + File.separator + fileName
//        Log.e("TAG", "createCopyAndReturnRealPath: $filePath")
//        val file = File(filePath)
//        try {
//            val inputStream = contentResolver.openInputStream(uri) ?: return null
//            val outputStream: OutputStream = FileOutputStream(file)
//            val buf = ByteArray(1024)
//            var len: Int
//            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
//            outputStream.close()
//            inputStream.close()
//        }
//        catch (ignore: IOException)
//        {
//            return null
//        }
//        return file.absolutePath
//    }
    private fun setspineradapter()
    {
        var adapter: ArrayAdapter<String> = ArrayAdapter<String>( requireContext(), android.R.layout.simple_spinner_dropdown_item, vehicleList)
        binding?.VehicleET?.setAdapter(adapter)
        binding?.VehicleET?.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(

                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            )
            {
                vehicleno = vehicleList.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        })

        binding?.VehicleET?.setSelection(0)
    }

    private fun validation(): Boolean
    {


        var isValidate = true


        if (amount?.isEmpty() == true)
        {
            isValidate = false
            Toast.makeText(requireContext(),resources.getString(R.string.error_empty_amount),Toast.LENGTH_SHORT).show()

        }
//        if (email?.isEmpty() == true)
//        {
//            isValidate = false
//            binding?.llErrorEmail?.visibility = View.VISIBLE
//        }
//        else
        {
//        if (isEmailValidate(email.toString()))
//        {
//            isValidate = true
//        }
//        else
//        {
//            isValidate = false
//            binding?.llErrorEmail?.visibility = View.VISIBLE
//            binding?.tvErrorEmail?.text = "Invalid Email"
//        }
        }
        return isValidate

    }


    private fun setvehiclevalue(
        strDeviceId: String,
        CardNumber: String,
        TransAmount: String,
        lat: Double,
        long: Double,
        vehicleno: String,
        imageUrl: String
    )
    {


        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()

        }

        var jsonObject = JsonObject()
        jsonObject.addProperty("DeviceID", strDeviceId)
        jsonObject.addProperty("CardNumber", CardNumber)
        jsonObject.addProperty("TransAmount", TransAmount)
        jsonObject.addProperty("Lat", lat)
        jsonObject.addProperty("Lng", long)
        jsonObject.addProperty("PlateNumber", vehicleno)
        jsonObject.addProperty("imageURL", imageUrl)


        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response =  apiInterface.setvehiclevalue(jsonObject)
            withContext(Dispatchers.Main) {
                try {
                    if (response != null)
                    {
                        baseActivity?.dismissDialogLoader()
                        if (response.isSuccessful)
                        {
                        if(response.body()?.success == true)
                        {
                        Toast.makeText(requireContext(),"Payment Successfully Done", Toast.LENGTH_LONG).show()
                        baseActivity?.goToOptionActivity()

                        }
                        else
                        {
                            Toast.makeText(requireContext(),response.body()?.response, Toast.LENGTH_LONG).show()

                        }
                        }

                        else
                        {
                            Toast.makeText(requireContext(),"Error in uploding", Toast.LENGTH_LONG).show()
                        }

                    }
                    else
                    {

                        Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG).show()
                    }

                }

                catch (e: JSONException)
                {
//                    dismissDialogLoader()
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }


    private fun getvehicleList(
        strDeviceId: String,
        lat: Double,
        long: Double,
        CardNumber: String,
    )
    {
        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()

        }

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response =  apiInterface.getvehicleList(strDeviceId, lat.toString(), long.toString(),CardNumber)
            withContext(Dispatchers.Main) {
                try {
                    if (response != null)
                    {

                        baseActivity?.dismissDialogLoader()
                        if (response.isSuccessful)
                        {
                            vehicleList.clear()
                          if(response.body()!!.vehicleNumber==null && response.body()!!.response!==null)
                          {
                              baseActivity?.showError("Invalid CardNumber, Please contact Support")
                          }
                            else
                          {
                              val responseData = response.body()!!.vehicleNumber!!.size
                              if (responseData > 0)
                              {
                                  if (response.body()!!.success == true)
                                  {

                                      //  baseActivity!!.showToast("Success")
                                      for (i in 0 until responseData)
                                      {
                                          val vehicleno = response.body()!!.vehicleNumber!!
                                          vehicleList.add(vehicleno[i])

                                      }
                                      binding?.vehiclelistCL?.visibility = View.VISIBLE
//                                    setvehicleAdapter()
                                      setspineradapter()
                                  }
                                  else
                                  {

//                                      baseActivity?.showError(response.body()!!.response.toString())
                                      Toast.makeText(requireContext(),"Something Went Wrong", Toast.LENGTH_LONG).show()
//                                    baseActivity!!.showToast(resources.getString(R.string.NoRecordFound))

                                  }

                              }
                              else
                              {
                                  Toast.makeText(requireContext(),"Something Went Wrong", Toast.LENGTH_LONG).show()
                              }
                          }

                        }

                        else
                        {

                            Toast.makeText(requireContext(),"Something Went Wrong", Toast.LENGTH_LONG).show()
                        }

                    }
                    else
                    {

                        Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG).show()
                    }

                }

                catch (e: JSONException)
                {
//                    dismissDialogLoader()
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }



//    private fun setvehicleAdapter() {
//        vehicleAdapter = VehicleAdapter(baseActivity!! ,vehicleList ,this)
//        binding!!.VehicleRV.adapter = vehicleAdapter
//    }

    private fun isEmailValidate(toString: String): Boolean {
        TODO("Not yet implemented")
    }

    fun getSN(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()


        }
        else Build.SERIAL
    }



}