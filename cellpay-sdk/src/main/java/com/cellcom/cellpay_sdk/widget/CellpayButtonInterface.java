package com.cellcom.cellpay_sdk.widget;

import android.view.View;

import androidx.annotation.Keep;

import com.cellcom.cellpay_sdk.api.Config;

@Keep
public interface CellpayButtonInterface {
    void setText(String buttonText);

    void setCheckOutConfig(Config config);

    void setCustomView(View view);

    void setButtonStyle(ButtonStyle style);

    void showCheckOut();

    void showCheckOut(Config config);

    void destroyCheckOut();
}
