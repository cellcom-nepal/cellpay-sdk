package com.cellcom.cellpay_sdk.utils;

public class NumberUtil {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Double convertToRupees(Long amount) {
        Double paisa = Double.parseDouble(amount.toString());
        return round((paisa / 100), 3);
    }
}
