package com.cellcom.cellpay_sdk.api;

import java.io.Serializable;
import java.util.HashMap;

public class Config implements Serializable {
    private String publicKey;
    private String merchantId;
    private String invoiceNumber;
    private String description;
    private Long amount;
    private HashMap<String, String> additionalData;
    private OnCheckOutListener onCheckOutListener;

    public Config(String publicKey, OnCheckOutListener onCheckOutListener) {
        this.publicKey = publicKey;
        this.onCheckOutListener = onCheckOutListener;
    }

    public Config(String publicKey, String merchantId, String invoiceNumber, Long amount, OnCheckOutListener onCheckOutListener) {
        this.publicKey = publicKey;
        this.merchantId = merchantId;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.onCheckOutListener = onCheckOutListener;
    }

    public Config(String publicKey, String merchantId, String invoiceNumber, String description, Long amount, HashMap<String, String> additionalData, OnCheckOutListener onCheckOutListener) {
        this.publicKey = publicKey;
        this.merchantId = merchantId;
        this.invoiceNumber = invoiceNumber;
        this.description=description;
        this.amount = amount;
        this.additionalData = additionalData;
        this.onCheckOutListener = onCheckOutListener;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public HashMap<String, String> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(HashMap<String, String> additionalData) {
        this.additionalData = additionalData;
    }

    public OnCheckOutListener getOnCheckOutListener() {
        return onCheckOutListener;
    }

    public void setOnCheckOutListener(OnCheckOutListener onCheckOutListener) {
        this.onCheckOutListener = onCheckOutListener;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
