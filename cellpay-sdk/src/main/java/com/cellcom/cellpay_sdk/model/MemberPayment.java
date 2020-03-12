package com.cellcom.cellpay_sdk.model;

import java.util.List;

public class MemberPayment {
    private String transferTypeId;
    private String amount;
    private String toMemberPrincipal;
    private String description;
    private String currencyId;
    private boolean webRequest;
    private boolean isOtpEnable;
    private List<CustomValues> customValues;
    private String transactionPin;
    private String otp;

    public String getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(String transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToMemberPrincipal() {
        return toMemberPrincipal;
    }

    public void setToMemberPrincipal(String toMemberPrincipal) {
        this.toMemberPrincipal = toMemberPrincipal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public boolean isWebRequest() {
        return webRequest;
    }

    public void setWebRequest(boolean webRequest) {
        this.webRequest = webRequest;
    }

    public boolean isOtpEnable() {
        return isOtpEnable;
    }

    public void setOtpEnable(boolean otpEnable) {
        isOtpEnable = otpEnable;
    }

    public List<CustomValues> getCustomValues() {
        return customValues;
    }

    public void setCustomValues(List<CustomValues> customValues) {
        this.customValues = customValues;
    }

    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
