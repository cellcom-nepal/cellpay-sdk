package com.cellcom.cellpay_sdk.utils;

import com.cellcom.cellpay_sdk.model.DoPaymentResult;
import com.cellcom.cellpay_sdk.model.MemberNames;

import java.util.List;

public class SessionStore {

    private static List<MemberNames> memberNames;
    private static DoPaymentResult doPaymentResult;
    private static String sessionId;

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        SessionStore.sessionId = sessionId;
    }

    public static List<MemberNames> getMemberNames() {
        return memberNames;
    }

    public static void setMemberNames(List<MemberNames> memberNames) {
        SessionStore.memberNames = memberNames;
    }

    public static DoPaymentResult getDoPaymentResult() {
        return doPaymentResult;
    }

    public static void setDoPaymentResult(DoPaymentResult doPaymentResult) {
        SessionStore.doPaymentResult = doPaymentResult;
    }
}
