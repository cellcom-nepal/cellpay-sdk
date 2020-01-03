package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("default")
    public boolean isDefault;

    @SerializedName("accountNo")
    public String mAccountNo;

    @SerializedName("memberRecordId")
    public String memberRecordId;

    public String getmAccountNo() {
        String accountNo = this.mAccountNo;
        String temp = "";

        int index = accountNo.length() - 4;
        for(int i= 0; i< accountNo.length(); i++) {
            if( i < index) {
                temp = temp.concat("*");
            } else {
                temp = temp.concat(String.valueOf(accountNo.charAt(i)));
            }
        }
        return temp;
    }

}
