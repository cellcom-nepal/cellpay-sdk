package com.cellcom.cellpay_sdk.utils;

public class GuavaUtil {
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
