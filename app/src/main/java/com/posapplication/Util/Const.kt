package com.posapplication.Util

object Const {
 @kotlin.jvm.JvmField

 var CREDIT_CARD_NO: String = ""
 @kotlin.jvm.JvmField
 var CREDIT_CARD_NO_XXXX: String = "1111 XXXX XXXX XXXX"

    //dummy
 const val PUBLIC_KEY = "pk_sbox_4lw44zofzbdcdloyrt2w3bz37y*"


//       const val PUBLIC_KEY = "pk_test_08b1f22f-fbb7-4bc3-8869-a9a44aa5f3e1"
//    const val PUBLIC_KEY = "pk_44d26b1b-5a24-4b3e-8d27-b8c39376df6b"
//    const val PUBLIC_KEY = "pk_44d26b1b-5a24-4b3e-8d27-b8c39376df6b"
    const val KEY_PROFILE = "profile"
   var validtxn = false
    var invalidlocation = false
    var invaliddevice = false
    var invalidcard = false
    const val FROM_ACTIVITY = "FROM_ACTIVITY"
    const val KEY_AMOUNT = "KEY_AMOUNT"
    const val OPTION_CHOOSE = "OPTION_CHOOSE"
    const val PRINT_TEXT = "PRINT_TEXT"
    const val DEVICE_ID = "DEVICE_ID"
    const val EMAIL_ID = "EMAIL_ID"
    const val KEY_PAYMENT_STATUS = "KEY_PAYMENT_STATUS"

    const val KEY_PAYMENT_DATA = "payment"
    const val KEY_TOTAL_AMOUNT = "KEY_TOTAL_AMOUNT"
    const val KEY_CARD_TYPE = "KEY_CARD_TYPE"
    const val KEY_CARD_NAME = "KEY_CARD_NAME"
    const val KEY_CARD_NUM = "KEY_CARD_NUM"
    const val KEY_REFRENCE = "KEY_REFRENCE"
    const val KEY_CURRENCY = "KEY_CURRENCY"
    const val BASE_URL = "https://demo.erpcrebit.com/json/"
    const val BASE_URLLOCAL = " http://192.168.1.6/json/"
    const val BASE_URLSEC = "https://royalblue.erpcrebit.com//Json/"
    const val BASE_URL1 = "https://market.erpcrebit.com/json/"
    const val API_TIMEOUT = 15000
    const val API_PROCESS_PAYMENT = "CourierApi/ProcessPaymentPOS"
    const val API_PROCESS_PAYMENT1 = "CloudApi/ProcessPayment"
    const val DEVICE_CODE = 124
    const val POS_ID = "POSID"
}