package com.posapplication.ui.Activity;

import androidx.annotation.RequiresApi;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.github.devnied.emvnfccard.enums.EmvCardScheme;
import com.github.devnied.emvnfccard.model.Application;
import com.github.devnied.emvnfccard.model.EmvCard;
import com.github.devnied.emvnfccard.parser.EmvTemplate;
import com.posapplication.Util.Const;
import com.posapplication.Util.PcscProvider;
import com.posapplication.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ScanActivity extends BaseActivity implements NfcAdapter.ReaderCallback{

    NfcAdapter mNfcAdapter;
    PendingIntent mPendingIntent;
//    TextView nfcaContent;
    String formateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
//        nfcaContent = findViewById(R.id.tvNfcaContent);

//        showProgressDialog(this);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


//        -----------------comment-------------------------
//        if (mNfcAdapter == null)
//        {
//            //nfc not support your device.
//            return;
//        }
////        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//            mPendingIntent = PendingIntent.getActivity
//                    (this, 0, new Intent(this,
//                            getClass()), PendingIntent.FLAG_MUTABLE);
//        }
//        else
//        {
//            mPendingIntent = PendingIntent.getActivity
//                    (this, 0, new Intent(this,
//                            getClass()), PendingIntent.FLAG_ONE_SHOT);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            Bundle options = new Bundle();
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);

            // Enable ReaderMode for all types of card and disable platform sounds
            // the option NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK is NOT set
            // to get the data of the tag after reading
            mNfcAdapter.enableReaderMode(this,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A |
                            NfcAdapter.FLAG_READER_NFC_B |
                            NfcAdapter.FLAG_READER_NFC_F |
                            NfcAdapter.FLAG_READER_NFC_V |
                            NfcAdapter.FLAG_READER_NFC_BARCODE |
                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                    options);
        }
    }

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//
//        if (mNfcAdapter != null) {
//            Bundle options = new Bundle();
//            // Work around for some broken Nfc firmware implementations that poll the card too fast
//            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);
//
//            // Enable ReaderMode for all types of card and disable platform sounds
//            // the option NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK is NOT set
//            // to get the data of the tag after reading
//            mNfcAdapter.enableReaderMode(this,
//                    this,
//                    NfcAdapter.FLAG_READER_NFC_A |
//                            NfcAdapter.FLAG_READER_NFC_B |
//                            NfcAdapter.FLAG_READER_NFC_F |
//                            NfcAdapter.FLAG_READER_NFC_V |
//                            NfcAdapter.FLAG_READER_NFC_BARCODE |
//                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
//                    options);
//        }
//
//        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
//
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent)
//    {
//        super.onNewIntent(intent);
//
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        GetDataFromTag(tag, intent);
//
//    }
//
//    private void GetDataFromTag(Tag tag, Intent intent)
//    {
//        Ndef ndef = Ndef.get(tag);
//        try {
//            ndef.connect();
////            txtType.setText(ndef.getType().toString());
////            txtSize.setText(String.valueOf(ndef.getMaxSize()));
////            txtWrite.setText(ndef.isWritable() ? "True" : "False");
//            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//            if (messages != null) {
//                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
//                for (int i = 0; i < messages.length; i++) {
//                    ndefMessages[i] = (NdefMessage) messages[i];
//                }
//                NdefRecord record = ndefMessages[0].getRecords()[0];
//
//                byte[] payload = record.getPayload();
//                String text = new String(payload);
//                Log.e("tag", "vahid" + text);
//                ndef.close();
//
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTagDiscovered(Tag tag) {

        IsoDep isoDep = null;

        // Whole process is put into a big try-catch trying to catch the transceive's IOException
        try {
            isoDep = IsoDep.get(tag);
            if (isoDep != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, 10));
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //UI related things, not important for NFC
//                    nfcaContent.setText("");
                }
            });
            isoDep.connect();
            byte[] response;
            String idContentString = "Content of ISO-DEP tag";

            PcscProvider provider = new PcscProvider();
            provider.setmTagCom(isoDep);

            EmvTemplate.Config config = EmvTemplate.Config()
                    .setContactLess(true)
                    .setReadAllAids(true)
                    .setReadTransactions(true)
                    .setRemoveDefaultParsers(false)
                    .setReadAt(true);

            EmvTemplate parser = EmvTemplate.Builder()
                    .setProvider(provider)
                    .setConfig(config)
                    .build();

            EmvCard card = parser.readEmvCard();
            String cardNumber = card.getCardNumber();

            Date expireDate = card.getExpireDate();
           String cardstate = String.valueOf(card.getState());
//            LocalDate date = LocalDate.of(2022, 12, 31);
            if (expireDate != null) {
                Date date = null;
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                String temp = expireDate.toString();
                try {
                    date = formatter.parse(temp);
                    Log.e("formated date ", date + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                 formateDate = new SimpleDateFormat("MM-dd-yyyy").format(date);
            }
            EmvCardScheme cardGetType = card.getType();
            if (cardGetType != null) {
                String typeName = card.getType().getName();
                String[] typeAids = card.getType().getAid();
                idContentString = idContentString + "\n" + "typeName: " + typeName;
                for (int i = 0; i < typeAids.length; i++) {
                    idContentString = idContentString + "\n" + "aid " + i + " : " + typeAids[i];
                }
            }
            String credit_card = prettyPrintCardNumber(cardNumber);
//            Const.CREDIT_CARD_NO = credit_card;
            Const.CREDIT_CARD_NO = "1111 XXXX XXXX XXXX";
            List<Application> applications = card.getApplications();
            idContentString = idContentString + "\n" + "cardNumber: " + credit_card;
            idContentString = idContentString + "\n" + "expireDate: " + formateDate;
            idContentString = idContentString + "\n" + "cardstate: " + cardstate;

//            stopProgresDialog();

            Intent myIntent = new Intent(ScanActivity.this, MainActivity.class);
            myIntent.putExtra(Const.FROM_ACTIVITY, "scanactivity");
            startActivity(myIntent);

            String finalIdContentString = idContentString;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //UI related things, not important for NFC
//                    nfcaContent.setText(finalIdContentString);
                }
            });
            try {
                isoDep.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            //Trying to catch any ioexception that may be thrown
            e.printStackTrace();
        } catch (Exception e) {
            //Trying to catch any exception that may be thrown
            e.printStackTrace();
        }


    }

    public static String prettyPrintCardNumber(String cardNumber) {
        if (cardNumber == null) return null;
        String firstDigits = cardNumber.substring(0,4);
        String lastDigits = cardNumber.substring(firstDigits.length());

        String mask =lastDigits.replaceAll("\\w(?=\\w{0})", "X");
        String maskedString= firstDigits+mask ;

        char delimiter = ' ';
        return maskedString.replaceAll(".{4}(?!$)", "$0" + delimiter);

    }



}