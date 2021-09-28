package com.cellcom.cellpay_sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit on 9/1/18
 */

public class WalletBalance {

    public static final String CUSTOMER_WALLET = "Customer Wallet";
    public static final String LOYALTY_WALLET = "Loyalty Wallet";
    public static final String AGENT_WALLET = "Agent Wallet";

    @SerializedName("balance")
    public Float balance;

    @SerializedName("formattedBalance")
    public String formattedBalance;

    @SerializedName("availableBalance")
    public Float availableBalance;

    @SerializedName("formattedAvailableBalance")
    public String formattedAvailableBalance;

    @SerializedName("reservedAmount")
    public Integer reservedAmount;

    @SerializedName("formattedReservedAmount")
    public String formattedReservedAmount;

    @SerializedName("creditLimit")
    public Float creditLimit;

    @SerializedName("formattedCreditLimit")
    public String formattedCreditLimit;

}
