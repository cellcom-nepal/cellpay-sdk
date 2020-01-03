package com.cellcom.cellpay_sdk.widget;

import androidx.annotation.NonNull;

import com.cellcom.cellpay_sdk.api.Config;
import com.cellcom.cellpay_sdk.utils.EmptyUtil;
import com.cellcom.cellpay_sdk.utils.GuavaUtil;

public class PayPresenter implements PayContract.Presenter {
    @NonNull
    private final PayContract.View mPayView;

    PayPresenter(@NonNull PayContract.View mPayView) {
        this.mPayView = GuavaUtil.checkNotNull(mPayView);
        mPayView.setPresenter(this);
    }

    @Override
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

    @Override
    public void setCustomButtonView() {
        mPayView.setCustomButtonView();
    }

    @Override
    public void setButtonStyle(int id) {
        mPayView.setButtonStyle(id);
    }

    @Override
    public void setButtonText(String text) {
        text = (EmptyUtil.isNotNull(text) && EmptyUtil.isNotEmpty(text)) ? text : "PAY";
        mPayView.setButtonText(text);
    }

    @Override
    public void setButtonClick() {
        mPayView.setButtonClick();
    }

    @Override
    public void openForm() {
        mPayView.openForm();
    }

    @Override
    public void destroyCheckOut() {
        mPayView.destroyCheckOut();
    }
}
