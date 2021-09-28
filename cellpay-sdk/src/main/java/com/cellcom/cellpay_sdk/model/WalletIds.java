package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Monika on 11/7/18
 */

public class WalletIds {

    //main response
    @SerializedName("id")
    public String id;


    //for currency
    @SerializedName("name")
    public String name;

}
