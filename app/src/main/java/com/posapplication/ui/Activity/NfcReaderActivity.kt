package com.posapplication.ui.Activity

import a.a.a.a.a.e
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.posapplication.R

class NfcReaderActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_reader2)
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        var tagFromIntent: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val nfc = NfcA.get(tagFromIntent)

        val atqa: ByteArray = nfc.getAtqa()
        val sak: Short = nfc.getSak()
        nfc.connect()

        Toast.makeText(this,atqa.toString(),Toast.LENGTH_LONG).show()
        Toast.makeText(this,sak.toString(),Toast.LENGTH_LONG).show()

//        val isConnected = nfc.isConnected()
//
//        if (isConnected) {
//            val receivedData: ByteArray = nfc.transceive(NFC_READ_COMMAND)
//
//        } else {
//            Log.e("ans", "Not connected")
//        }


    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException(e.toString())
            }
        }
        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }



    override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)
    }
}