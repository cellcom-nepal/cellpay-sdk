package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    @SerializedName("errors")
    private List<ApiMessage> mErrorMessageList;

    @SerializedName("payload")
    public Payload payload;

    @SerializedName("sessionExpired")
    public boolean mSesionExpired;

    @SerializedName("sessionId")
    public String sessionId;

    @SerializedName("Status")
    public boolean mStatus;

    @SerializedName("statusCode")
    private String mStatusCode;

    @SerializedName(alternate = {"messages"}, value = "message")
    private List<ApiMessage> mSuccessMessageList;

    public String _iterateErrorMessage(){
        List<ApiMessage> list = this.mErrorMessageList;
        if(list != null) {
            return ((ApiMessage) list.get(0)).mTitle;
        }
        return "";
    }

    public int getErrorCode() {
        return Integer.parseInt(this.mStatusCode);
    }

    public String getErrorCodeInString() {return this.mStatusCode;}

    public String _iterateSuccessMessage() {
        return ((ApiMessage) this.mSuccessMessageList.get(0)).mMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<ApiMessage> getErrorMessage(){
        return mErrorMessageList;
    }
}
