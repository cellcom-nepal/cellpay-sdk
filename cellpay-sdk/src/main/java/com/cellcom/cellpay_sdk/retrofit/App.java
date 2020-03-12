package com.cellcom.cellpay_sdk.retrofit;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.cellcom.cellpay_sdk.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private CellPayClient cellPayClient;
    private InternetConnectionListener mInternetConnectionListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void removeInternetConnectionListener(){
        mInternetConnectionListener = null;
    }

    public void setInternetConnectionListener(InternetConnectionListener listener){
        mInternetConnectionListener = listener;
    }

    public CellPayClient getCellPayClient(){
        if(cellPayClient == null){
            cellPayClient = provideRetrofit(CellPayClient.TEST_URL).create(CellPayClient.class);
        }
        return cellPayClient;
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static final Retrofit provideRetrofit(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    private static final OkHttpClient provideOkHttpClient(){
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        if(BuildConfig.DEBUG) {
            okhttpClientBuilder.addInterceptor(logging);
        }
        /*okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return App.this.isNetworkAvailable();
            }

            @Override
            public void onInternetUnavailable() {
                if(mInternetConnectionListener != null){
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }
        });*/
        return okhttpClientBuilder.build();
    }
}
