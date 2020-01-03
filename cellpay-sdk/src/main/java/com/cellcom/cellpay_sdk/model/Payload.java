package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payload {

    @SerializedName("MemberDetailsList")
    public List<MemberNames> mMemberNames;

    @SerializedName("DoPaymentResult")
    public DoPaymentResult doPaymentResult;

    @SerializedName("ConfirmPaymentResult")
    public ConfirmPaymentResult confirmPaymentResult;
}
