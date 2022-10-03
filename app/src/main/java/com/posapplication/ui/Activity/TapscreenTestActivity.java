package com.posapplication.ui.Activity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

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

public class TapscreenTestActivity extends BaseActivity implements NfcAdapter.ReaderCallback {
    private NfcAdapter mNfcAdapter;

    TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_screentest);
//        nfcaContent = findViewById(R.id.tvNfcaContent);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        titleTV = findViewById(R.id.titleTV);
        titleTV.setTextSize(19f);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            Bundle options = new Bundle();

            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);

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

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableReaderMode(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTagDiscovered(Tag tag) {

        IsoDep isoDep = null;


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
            Const.CREDIT_CARD_NO=cardNumber;
            Date date = null;
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
            String temp = expireDate.toString();
            try {
                date = formatter.parse(temp);
                Log.e("formated date ", date + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formateDate = new SimpleDateFormat("MM-dd-yyyy").format(date);

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
          Const.CREDIT_CARD_NO_XXXX = credit_card;
//            Const.CREDIT_CARD_NO = "1111 XXXX XXXX XXXX";
            List<Application> applications = card.getApplications();
            idContentString = idContentString + "\n" + "cardNumber: " + credit_card;
            idContentString = idContentString + "\n" + "expireDate: " + formateDate;

            Intent myIntent = new Intent(TapscreenTestActivity.this, MainActivity.class);
            myIntent.putExtra(Const.OPTION_CHOOSE, "scanactivity");
            startActivity(myIntent);


            String finalIdContentString = idContentString;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

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

            e.printStackTrace();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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

    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    @Override
    public void onBackPressed() {

        Intent myIntent = new Intent(TapscreenTestActivity.this, OptionActivity.class);
        startActivity(myIntent);
        finishAffinity();
        super.onBackPressed();
    }


}
