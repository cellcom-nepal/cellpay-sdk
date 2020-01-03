package com.cellcom.cellpay_sdk.helper;

import com.cellcom.cellpay_sdk.api.Config;

public interface CellpayCheckOutInterface {
    void show();

    void show(Config config);

    void destroy();
}
