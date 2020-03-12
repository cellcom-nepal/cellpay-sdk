package com.cellcom.cellpay_sdk.retrofit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {

    private String credentials = null;
    public BasicAuthInterceptor(String username, String pin){
        credentials = Credentials.basic(username,pin);
    }
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
       request.newBuilder().header("Authorization", credentials).build();

        return null;
    }
}
