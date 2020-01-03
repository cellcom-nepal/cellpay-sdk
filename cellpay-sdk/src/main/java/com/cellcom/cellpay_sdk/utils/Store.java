package com.cellcom.cellpay_sdk.utils;

import com.cellcom.cellpay_sdk.api.Config;

public class Store {
    private static Config config;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        Store.config = config;
    }
}
