package com.cellcom.cellpay_sdk.model;

import android.accounts.Account;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MemberNames {

    @SerializedName("accounts")
    public List<Account> mAccountList = new ArrayList();

    @SerializedName("memberName")
    private String mMemberName;

    @SerializedName("memberId")
    private int memberId;

    @SerializedName("userName")
    private String mUserName;

    @SerializedName("accountNo")
    private String mAccountNo;

    @SerializedName("default")
    private boolean isDefault;

    @SerializedName("memberRecordId")
    private int memberRecordId;

    public MemberNames(){

    }

    public List<Account> getmAccountList() {

        return mAccountList;
    }

    public void setmAccountList(List<Account> mAccountList) {
        this.mAccountList = mAccountList;
    }

    public String getmMemberName() {
        return mMemberName;
    }

    public void setmMemberName(String mMemberName) {
        this.mMemberName = mMemberName;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmAccountNo() {
        return mAccountNo;
    }

    public void setmAccountNo(String mAccountNo) {
        this.mAccountNo = mAccountNo;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getMemberRecordId() {
        return memberRecordId;
    }

    public void setMemberRecordId(int memberRecordId) {
        this.memberRecordId = memberRecordId;
    }
}
