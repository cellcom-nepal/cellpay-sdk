package com.cellcom.cellpay_sdk.retrofit;

import android.content.Context;

import com.cellcom.cellpay_sdk.BuildConfig;
import com.cellcom.cellpay_sdk.utils.Store;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIClient {
    private static Retrofit retrofit = null;

    static String Key = Store.getConfig().getPublicKey();
    static String  URL ;
    public static Retrofit getRetrofitClinet(Context mContext){

        if(Key.equalsIgnoreCase("cellpayTestKey")) {
            URL = CellPayClient.TEST_URL;
        } else if(Key.equalsIgnoreCase("Y8TU95W7XA8") || Key.equalsIgnoreCase("Manjit")){
            URL = CellPayClient.LIVE_URL;
        }
        if(retrofit == null){} {
            OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
            okhttpClientBuilder.connectTimeout(180, TimeUnit.SECONDS);
            okhttpClientBuilder.readTimeout(180, TimeUnit.SECONDS);
            okhttpClientBuilder.writeTimeout(180, TimeUnit.SECONDS);
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor(mContext));
            if (BuildConfig.DEBUG) {
                okhttpClientBuilder.addInterceptor(logging);
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClientBuilder.build())
                    .build();
        }

        return retrofit;
    }
}
