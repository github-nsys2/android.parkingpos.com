package com.posapplication.Util


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentData  {

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("Id")
    @Expose
    var id: String? = null

    @SerializedName("ActionId")
    @Expose
    var actionId: String? = null

    @SerializedName("Amount")
    @Expose
    var amount: Int? = null

    @SerializedName("Currency")
    @Expose
    var currency: String? = null

    @SerializedName("Approved")
    @Expose
    var approved: Boolean? = null

    @SerializedName("Status")
    @Expose
    var status: String? = null

    @SerializedName("AuthCode")
    @Expose
    var authCode: String? = null

    @SerializedName("ResponseCode")
    @Expose
    var responseCode: String? = null

    @SerializedName("ResponseSummary")
    @Expose
    var responseSummary: String? = null

    @SerializedName("Source")
    @Expose
    var cardData: CardData? = null
    @SerializedName("Customer ")
    @Expose
    var customerData: CustomerData? = null


    class CardData {
        @SerializedName("Id")
        @Expose
        var id: String? = null

        @SerializedName("Type")
        @Expose
        var type: String? = null

        @SerializedName("BillingAddress")
        @Expose
        var billingAddress: Any? = null

        @SerializedName("Phone")
        @Expose
        var phone: Any? = null

        @SerializedName("ExpiryMonth")
        @Expose
        var expiryMonth: Int? = null

        @SerializedName("ExpiryYear")
        @Expose
        var expiryYear: Int? = null

        @SerializedName("Name")
        @Expose
        var name: String? = null

        @SerializedName("Scheme")
        @Expose
        var scheme: String? = null

        @SerializedName("Last4")
        @Expose
        var last4: String? = null

        @SerializedName("Fingerprint")
        @Expose
        var fingerprint: String? = null

        @SerializedName("Bin")
        @Expose
        var bin: String? = null

        @SerializedName("CardType")
        @Expose
        var cardType: String? = null

        @SerializedName("CardCategory")
        @Expose
        var cardCategory: String? = null

        @SerializedName("Issuer")
        @Expose
        var issuer: String? = null

        @SerializedName("IssuerCountry")
        @Expose
        var issuerCountry: String? = null

        @SerializedName("ProductId")
        @Expose
        var productId: String? = null

        @SerializedName("ProductType")
        @Expose
        var productType: String? = null

        @SerializedName("AvsCheck")
        @Expose
        var avsCheck: String? = null

        @SerializedName("CvvCheck")
        @Expose
        var cvvCheck: String? = null

        @SerializedName("Payouts")
        @Expose
        var payouts: Boolean? = null

        @SerializedName("FastFunds")
        @Expose
        var fastFunds: String? = null
    }

    class CustomerData {


        @SerializedName("Name")
        @Expose
        val name: Any? = null
    }

    }