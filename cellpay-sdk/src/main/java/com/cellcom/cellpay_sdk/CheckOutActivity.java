package com.cellcom.cellpay_sdk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.cellcom.cellpay_sdk.helper.SnackbarHelper;
import com.cellcom.cellpay_sdk.model.ApiResponse;
import com.cellcom.cellpay_sdk.model.LoginBody;
import com.cellcom.cellpay_sdk.retrofit.CellPayClient;
import com.cellcom.cellpay_sdk.retrofit.NoConnectivityException;
import com.cellcom.cellpay_sdk.retrofit.RetrofitAPIClient;
import com.cellcom.cellpay_sdk.utils.SessionStore;
import com.cellcom.cellpay_sdk.validation.SaripaarValidation;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import javax.xml.validation.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckOutActivity extends SaripaarValidation {

    private Button login_button;

    //CellPayClient client = CellPayClient.retrofit.create(CellPayClient.class);
    CellPayClient client = RetrofitAPIClient.getRetrofitClinet(this).create(CellPayClient.class);
    ProgressDialog progressDialog;

    @NotEmpty
    @Pattern(regex = "(98)[0-9]{8}", message = "Enter a registered Phone Number")
    private TextInputEditText mobile_number;

    @NotEmpty
    @Password(min = 6, message = "Please enter your valid 6 digit PIN ")
    private TextInputEditText pin;

    protected Validator validator;
    protected boolean validated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cellpay_payment_activity);
        progressDialog = new ProgressDialog(this);
        signIn();
    }

    private void signIn() {
        mobile_number = findViewById(R.id.act_login_mobile_num);
        pin = findViewById(R.id.act_login_pin);

        login_button = findViewById(R.id.act_login_proceed_btn);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        validated = true;

        progressDialog.setMessage("Please wait ....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        LoginBody loginBody = new LoginBody("SESSION");

        String base = mobile_number.getText() + ":" + pin.getText();
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        sendNetworkRequest(authHeader, loginBody);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validated = false;
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof TextInputEditText) {
                TextInputEditText et = (TextInputEditText) view;
                et.setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    // private void sendNetworkRequest(View v, String authHeader, LoginBody loginBody) {
    private void sendNetworkRequest(String authHeader, LoginBody loginBody) {
        Call<ApiResponse> call = client.login(authHeader, loginBody);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    SessionStore.setSessionId(response.body().getSessionId());
                    Intent intent = new Intent(CheckOutActivity.this, MerchantPayactivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                    finish();
                    //Toast.makeText(getApplicationContext(), response.body().getSessionId(), Toast.LENGTH_SHORT).show();
                } else {

                    switch (response.code()) {
                        case 400:
                            try {
                                ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this);
                                builder.setMessage(errorResponse._iterateErrorMessage())
                                        .setTitle("Sign In")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            progressDialog.dismiss();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    //Toast.makeText(getApplicationContext(), "Make Sure you have internet connection!", Toast.LENGTH_SHORT).show();
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.rel_parent);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please check your Internet Connection", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);

                    SnackbarHelper.configSnackbar(getApplicationContext(), snackbar);
                    snackbar.show();
                }
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
