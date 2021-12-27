package com.cellcom.cellpay_sdk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.cellcom.cellpay_sdk.api.Config;
import com.cellcom.cellpay_sdk.api.OnCheckOutListener;
import com.cellcom.cellpay_sdk.model.ApiResponse;
import com.cellcom.cellpay_sdk.model.CustomValues;
import com.cellcom.cellpay_sdk.model.MemberNames;
import com.cellcom.cellpay_sdk.model.MemberPayment;
import com.cellcom.cellpay_sdk.retrofit.CellPayClient;
import com.cellcom.cellpay_sdk.retrofit.RetrofitAPIClient;
import com.cellcom.cellpay_sdk.utils.NumberUtil;
import com.cellcom.cellpay_sdk.utils.SessionStore;
import com.cellcom.cellpay_sdk.utils.Store;
import com.satsuware.usefulviews.LabelledSpinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MerchantPayactivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private TextInputEditText amount, invoice_number, description, payment_to, total_amount, otp, pin;
    Button next;
    CellPayClient client = RetrofitAPIClient.getRetrofitClinet(this).create(CellPayClient.class);
    private LabelledSpinner banks;
    List<MemberNames> memberNames;
    MemberPayment memberPayment = new MemberPayment();

    private Config config;

    String bankName, accountNumber;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_payment_activity);

        config = Store.getConfig();

        getBankList();
        progressDialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //binding views to editText
        amount = findViewById(R.id.etAmount);
        amount.setKeyListener(null);
        invoice_number = findViewById(R.id.etInvoiceNumber);
        invoice_number.setKeyListener(null);
        description = findViewById(R.id.etDescription);
        description.setText(config.getDescription());
        banks = findViewById(R.id.spinner);
        next = findViewById(R.id.act_p2p_next_btn);

        //Setting text that were given by merchants
        amount.setText(NumberUtil.convertToRupees(config.getAmount()) + " NPR");
        invoice_number.setText(config.getInvoiceNumber());


        banks.setOnItemChosenListener(this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait ....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
                // Setting body for MemberPayment

                memberPayment.setTransferTypeId("50");
                memberPayment.setAmount("" + NumberUtil.convertToRupees(config.getAmount()));
                memberPayment.setToMemberPrincipal(config.getMerchantId());
                memberPayment.setDescription(config.getMerchantId());
                memberPayment.setCurrencyId("1");
                memberPayment.setWebRequest(true);
                List<CustomValues> customValues = new ArrayList<>();
                CustomValues cv1 = new CustomValues("PAYMENTMETHOD", "15", "Account");
                CustomValues cv2 = new CustomValues("SELECTBANK", "35", bankName);
                CustomValues cv3 = new CustomValues("ACCOUNTNUMBER", "14", accountNumber);
                CustomValues cv4 = new CustomValues("INVOICENUMBER", "99", config.getInvoiceNumber());
                customValues.add(cv1);
                customValues.add(cv2);
                customValues.add(cv3);
                customValues.add(cv4);
                memberPayment.setCustomValues(customValues);
                memberPayment.setOtpEnable(false);


                Call<ApiResponse> call = client.executeMemberPayment(SessionStore.getSessionId(), memberPayment);

                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {

                            SessionStore.setDoPaymentResult(response.body().payload.doPaymentResult);
                            progressDialog.dismiss();
                            showAlertDialogButtonClicked(v);
                        } else {

                            switch (response.code()) {
                                case 400:
                                    try {
                                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MerchantPayactivity.this);
                                        builder.setMessage(errorResponse._iterateErrorMessage())
                                                .setTitle("Merchant Pay")
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
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });


    }

    public void getBankList() {

        List<String> banklst = new ArrayList<>();
        Call<ApiResponse> call = client.getMemberAccount(SessionStore.getSessionId());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    SessionStore.setMemberNames(response.body().payload.mMemberNames);
                    for (MemberNames mName : response.body().payload.mMemberNames) {

                        banklst.add(mName.getmMemberName());
                        Log.d("meTAG", "onResponse: " + mName.getmMemberName());
                    }
                    banks.setItemsArray(banklst);
                } else {
                    switch (response.code()) {
                        case 400:
                            try {
                                ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MerchantPayactivity.this);

                                builder.setMessage(errorResponse._iterateErrorMessage())
                                        .setTitle("Merchant Pay")
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

                            break;
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error has Occurred", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showSuccessDialog(View view, HashMap<String, Object> data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.transaction_successful)
                .setTitle("Pay Merchant")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OnCheckOutListener onCheckOutListener = config.getOnCheckOutListener();
                        onCheckOutListener.onSuccess(data);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialogButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_confirm_pin, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView confirmationMessage = customLayout.findViewById(R.id.confirmation_text);
        payment_to = customLayout.findViewById(R.id.edt_to);
        total_amount = customLayout.findViewById(R.id.edt_Amount);
        otp = customLayout.findViewById(R.id.edt_otp);
        pin = customLayout.findViewById(R.id.edtPin);
        otp.setVisibility(View.GONE);

        confirmationMessage.setText(R.string.please_enter_your_pin);
        payment_to.setText(SessionStore.getDoPaymentResult().to.name);
        total_amount.setText(SessionStore.getDoPaymentResult().consolidatedFormatterAmount);


        if (SessionStore.getDoPaymentResult().isOtpEnable) {
            otp.setVisibility(view.VISIBLE);
            confirmationMessage.setText(R.string.please_enter_your_otp_pin);
        }
        Button confirm_button = customLayout.findViewById(R.id.btnConfirm);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait, the transaction is being processed ....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                memberPayment.setTransactionPin(pin.getText().toString());
                if (SessionStore.getDoPaymentResult().isOtpEnable) {
                    memberPayment.setOtp(otp.getText().toString());
                }

                Call<ApiResponse> call = client.executeConfirmMemberPayment(SessionStore.getSessionId(), memberPayment);

                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
//                            Log.d("mabeTAG", "onResponse: " + response.body().payload.confirmPaymentResult.getId());
//                            Toast.makeText(getApplicationContext(), "Transaction Id :" + response.body().payload.confirmPaymentResult.getId(), Toast.LENGTH_LONG).show();

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("cellpay_transaction_status", "SUCCESS");
                            data.put("merchant_invoice_number", config.getInvoiceNumber());
                            data.put("merchant_amount", "" + NumberUtil.convertToRupees(config.getAmount()));
                            data.put("cellpay_ref_id", response.body().payload.confirmPaymentResult.getId());

                            progressDialog.dismiss();
                            dialog.dismiss();

                            showSuccessDialog(view, data);
                        } else {

                            switch (response.code()) {
//                                case 400:
//                                    try {
//                                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(MerchantPayactivity.this);
//                                        builder.setMessage(errorResponse._iterateErrorMessage())
//                                                .setTitle("Merchant Pay")
//                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        OnCheckOutListener onCheckOutListener = config.getOnCheckOutListener();
//                                                        onCheckOutListener.onError("api_response", errorResponse._iterateErrorMessage());
//                                                    }
//                                                });
//                                        AlertDialog dialog = builder.create();
//                                        dialog.show();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    progressDialog.dismiss();
//                                    break;
                                default:
                                    try{
                                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MerchantPayactivity.this);
                                        builder.setMessage(errorResponse._iterateErrorMessage())
                                                .setTitle("Merchant Pay")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        HashMap<String,String> failureData=new HashMap<>();
                                                        failureData.put("On_Failure ",errorResponse._iterateErrorMessage());
                                                        OnCheckOutListener onCheckOutListener = config.getOnCheckOutListener();
                                                        onCheckOutListener.onError("On_Failure", errorResponse._iterateErrorMessage());
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    break;

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d("mabeTAG", "onFailure: " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Error has occurred", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        if (labelledSpinner.getId() == R.id.spinner) {

            for (MemberNames mnames : SessionStore.getMemberNames()) {
                if (mnames.getmMemberName().equalsIgnoreCase(selectedText)) {
                    Log.d("TAGs", "onItemChosen: " + mnames.getmUserName() + "- " + mnames.getmAccountNo());
                    bankName = mnames.getmUserName();
                    accountNumber = mnames.getmAccountNo();
                }
            }
            Toast.makeText(getApplicationContext(), "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }

}
