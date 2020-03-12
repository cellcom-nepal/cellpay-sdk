package com.cellcom.cellpay_sdk.retrofit;

import androidx.annotation.Nullable;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Nullable
    @Override
    public String getMessage() {
        return "No Internet Connection!";
    }
}
