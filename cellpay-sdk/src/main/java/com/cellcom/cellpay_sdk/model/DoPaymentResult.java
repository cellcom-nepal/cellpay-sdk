package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

public class DoPaymentResult {

    @SerializedName("wouldRequireAuthorization")
    public boolean wouldRequreAuthorization;

    @SerializedName("from")
    public From from;

    @SerializedName("to")
    public From to;
//    @SerializedName("finalAmount")
//    public int finalAmount;

    @SerializedName("formattedFinalAmount")
    public String formattedFinalAmount;

    @SerializedName("isOtpEnable")
    public boolean isOtpEnable;

    @SerializedName("consolidatedAmount")
    public double consolidatedAmount;

    @SerializedName("consolidatedFormatterAmount")
    public String consolidatedFormatterAmount;

    public boolean isWouldRequreAuthorization() {
        return wouldRequreAuthorization;
    }

    public void setWouldRequreAuthorization(boolean wouldRequreAuthorization) {
        this.wouldRequreAuthorization = wouldRequreAuthorization;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

//    public int getFinalAmount() {
//        return finalAmount;
//    }
//
//    public void setFinalAmount(int finalAmount) {
//        this.finalAmount = finalAmount;
//    }

    public String getFormattedFinalAmount() {
        return formattedFinalAmount;
    }

    public void setFormattedFinalAmount(String formattedFinalAmount) {
        this.formattedFinalAmount = formattedFinalAmount;
    }

    public boolean isOtpEnable() {
        return isOtpEnable;
    }

    public void setOtpEnable(boolean otpEnable) {
        isOtpEnable = otpEnable;
    }

    public double getConsolidatedAmount() {
        return consolidatedAmount;
    }

    public void setConsolidatedAmount(long consolidatedAmount) {
        this.consolidatedAmount = consolidatedAmount;
    }

    public String getConsolidatedFormatterAmount() {
        return consolidatedFormatterAmount;
    }

    public void setConsolidatedFormatterAmount(String consolidatedFormatterAmount) {
        this.consolidatedFormatterAmount = consolidatedFormatterAmount;
    }
}
