package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

public class ConfirmPaymentResult {
    @SerializedName("id")
    private int id;
    @SerializedName("pending")
    private String pending;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }
}
