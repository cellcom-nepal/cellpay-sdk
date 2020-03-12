package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

public class ApiMessage {

    @SerializedName(alternate = {"shortMessage"}, value = "message")
    public String mMessage;

    @SerializedName("longMessage")
    public String mTitle;
}
