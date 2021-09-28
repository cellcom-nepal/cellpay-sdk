package com.cellcom.cellpay_sdk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cellcom.cellpay_sdk.model.WalletBalance;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MerchantPayactivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "MerchantPayactivity";
    private TextInputEditText amount, invoice_number, description, payment_to, total_amount, otp, pin;
    private TextInputLayout otpLayout;
    private LinearLayout walletBalance;
    private TextView walletBalanceAmount;
    Button next;
    CellPayClient client = RetrofitAPIClient.getRetrofitClinet(this).create(CellPayClient.class);

    private LinearLayout walletLayout, accountLayout;
    private LabelledSpinner banks;
    List<MemberNames> memberNames;
    MemberPayment memberPayment = new MemberPayment();
    private ImageView ivAccount, ivWallet;

    private Config config;

    String bankName, accountNumber;
    private String paymentType;
    private final String ACCOUNT = "account";
    private final String WALLET = "wallet";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_payment_activity);

        config = Store.getConfig();

        paymentType = ACCOUNT;


        getBankList();
        progressDialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        walletLayout = findViewById(R.id.act_top_wallet_lnr_lyt);
        accountLayout = findViewById(R.id.act_top_account_lnr_lyt);


        walletBalance = findViewById(R.id.et_wallet_balance_layout);


        ivAccount = findViewById(R.id.act_top_account_iv);
        ivWallet = findViewById(R.id.act_top_wallet_iv);


        //binding views to editText
        amount = findViewById(R.id.etAmount);
        walletBalanceAmount = findViewById(R.id.etWalletAmount);

        invoice_number = findViewById(R.id.etInvoiceNumber);
        description = findViewById(R.id.etDescription);
        description.setText(config.getDescription());
        banks = findViewById(R.id.spinner);
        next = findViewById(R.id.act_p2p_next_btn);

        //Setting text that were given by merchants
        amount.setText(NumberUtil.convertToRupees(config.getAmount()) + " NPR");
        invoice_number.setText(config.getInvoiceNumber());


        banks.setOnItemChosenListener(this);

        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: wallet");

                paymentType = WALLET;
                banks.setVisibility(View.GONE);
                walletBalance.setVisibility(View.VISIBLE);
                ivAccount.setColorFilter(ContextCompat.getColor(MerchantPayactivity.this, R.color.colorMenuColor), android.graphics.PorterDuff.Mode.SRC_IN);
                ivWallet.setColorFilter(ContextCompat.getColor(MerchantPayactivity.this, R.color.selectColor), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });

        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentType = ACCOUNT;
                walletBalance.setVisibility(View.GONE);
                banks.setVisibility(View.VISIBLE);
                ivWallet.setColorFilter(ContextCompat.getColor(MerchantPayactivity.this, R.color.colorMenuColor), android.graphics.PorterDuff.Mode.SRC_IN);
                ivAccount.setColorFilter(ContextCompat.getColor(MerchantPayactivity.this, R.color.selectColor), android.graphics.PorterDuff.Mode.SRC_IN);

                Log.e(TAG, "onClick: account");

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait ....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
                // Setting body for MemberPayment

                if (paymentType == ACCOUNT)
                    memberPayment.setTransferTypeId("50");
                if (paymentType == WALLET)
                    memberPayment.setTransferTypeId("68");
                memberPayment.setAmount("" + NumberUtil.convertToRupees(config.getAmount()));
                memberPayment.setToMemberPrincipal(config.getMerchantId());
                memberPayment.setDescription(config.getMerchantId());
                memberPayment.setCurrencyId("1");
                memberPayment.setWebRequest(true);
                List<CustomValues> customValues = new ArrayList<>();

                if(paymentType == ACCOUNT){
                    customValues.add( new CustomValues("PAYMENTMETHOD", "15", "Account"));
                    customValues.add(new CustomValues("SELECTBANK", "35", bankName));
                    customValues.add(new CustomValues("ACCOUNTNUMBER", "14", accountNumber));
                }
               if(paymentType == WALLET )
                   customValues.add( new CustomValues("PAYMENTMETHOD", "15", "Mobile Wallet"));

                customValues.add(new CustomValues("INVOICENUMBER", "99", config.getInvoiceNumber()));
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

        getWalletId();


    }

    private WalletBalance mWalletBalanceStatus;

    private void getWalletId() {

        Call<ApiResponse> call = client.getWalletId(SessionStore.getSessionId());

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {

                    getWalletStatus(response.body().payload.mWalletIds.get(1).id);


//                    walletBalanceAmount.setText(mWalletBalanceStatus.formattedBalance);


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
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getWalletStatus(String id) {

        Call<ApiResponse> call = client.getWalletStatus(SessionStore.getSessionId(), id);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {

                    mWalletBalanceStatus = response.body().payload.mWalletBalance;
                    walletBalanceAmount.setText("Wallet Balance: "+mWalletBalanceStatus.formattedBalance);


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
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
        otpLayout = customLayout.findViewById(R.id.edt_otp_layout);
        pin = customLayout.findViewById(R.id.edtPin);
        otp.setVisibility(View.GONE);
        otpLayout.setVisibility(View.GONE);

        confirmationMessage.setText(R.string.please_enter_your_pin);
        payment_to.setText(SessionStore.getDoPaymentResult().to.name);
        total_amount.setText(SessionStore.getDoPaymentResult().consolidatedFormatterAmount);


        if (SessionStore.getDoPaymentResult().isOtpEnable) {
            otp.setVisibility(view.VISIBLE);
            otpLayout.setVisibility(view.VISIBLE);
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
                                    try {
                                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MerchantPayactivity.this);
                                        builder.setMessage(errorResponse._iterateErrorMessage())
                                                .setTitle("Merchant Pay")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        HashMap<String, String> failureData = new HashMap<>();
                                                        failureData.put("On_Failure ", errorResponse._iterateErrorMessage());
                                                        OnCheckOutListener onCheckOutListener = config.getOnCheckOutListener();
                                                        onCheckOutListener.onError("On_Failure", errorResponse._iterateErrorMessage());
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
