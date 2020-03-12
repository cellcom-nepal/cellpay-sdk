package com.cellcom.cellpay_sdk.helper;

import android.content.Context;

import com.cellcom.cellpay_sdk.CheckOutActivity;
import com.cellcom.cellpay_sdk.api.Config;
import com.cellcom.cellpay_sdk.utils.ActivityUtil;
import com.cellcom.cellpay_sdk.utils.EmptyUtil;
import com.cellcom.cellpay_sdk.utils.Store;

public class CellpayCheckOut implements CellpayCheckOutInterface {
    private Context context;

    public CellpayCheckOut(Context context) {
        this.context = context;
    }

    public CellpayCheckOut(Context context, Config config) {
        Store.setConfig(config);
        this.context = context;
    }

    @Override
    public void show() {
        if (EmptyUtil.isNull(Store.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        String message = checkConfig(Store.getConfig());
        if (EmptyUtil.isNotNull(message)) {
            throw new IllegalArgumentException(message);
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void show(Config config) {
        Store.setConfig(config);
        if (EmptyUtil.isNull(Store.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        String message = checkConfig(Store.getConfig());
        if (EmptyUtil.isNotNull(message)) {
            throw new IllegalArgumentException(message);
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void destroy() {

    }


    public String checkConfig(Config config) {
        if (EmptyUtil.isNull(config.getPublicKey())) {
            return "Public key cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getPublicKey())) {
            return "Public key cannot be empty";
        }
        if (EmptyUtil.isNull(config.getMerchantId())) {
            return "Merchant identity cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getMerchantId())) {
            return "Merchant identity cannot be empty";
        }
        if (EmptyUtil.isNull(config.getInvoiceNumber())) {
            return "Invoice number cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getInvoiceNumber())) {
            return "Invoice number cannot be empty";
        }

        if (EmptyUtil.isNull(config.getAmount())) {
            return "Product url cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getAmount())) {
            return "Product url cannot be 0";
        }
        if (EmptyUtil.isNull(config.getOnCheckOutListener())) {
            return "Listener cannot be null";
        }
        return null;
    }
}
