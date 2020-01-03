package com.cellcom.cellpay_sdk.retrofit;

import com.cellcom.cellpay_sdk.model.ApiResponse;
import com.cellcom.cellpay_sdk.model.LoginBody;
import com.cellcom.cellpay_sdk.model.MemberPayment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CellPayClient {


    String TEST_URL = "https://test.cellpay.com.np/rest/";
    String LIVE_URL = " https://web.cellpay.com.np/rest/";

    @POST("access/login")
    Call<ApiResponse> login(@Header("Authorization") String authHeader, @Body LoginBody loginBody);

    @Headers("Content-Type: application/json")
    @GET("memberRecord/accounts")
    Call<ApiResponse> getMemberAccount(@Header("Paynet-Session-Token") String token);

    @Headers("Content-Type: application/json")
    @POST("payments/memberPayment")
    Call<ApiResponse> executeMemberPayment(@Header("Paynet-Session-Token") String token, @Body MemberPayment memberPayment);

    @Headers("Content-Type: application/json")
    @POST("payments/confirmMemberPayment")
    Call<ApiResponse> executeConfirmMemberPayment(@Header("Paynet-Session-Token") String token, @Body MemberPayment memberPayment);


//    public static final Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(TEST_URL)
//            .client(provideOkHttpClient())
//            .addConverterFactory(GsonConverterFactory.create(new Gson()))
//            .build();
//
//
//
//
//    public static OkHttpClient provideOkHttpClient() {
//        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
//        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
//        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
//        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        if (BuildConfig.DEBUG) {
//            okhttpClientBuilder.addInterceptor(logging);
//        }
//        return okhttpClientBuilder.build();
//    }
}
