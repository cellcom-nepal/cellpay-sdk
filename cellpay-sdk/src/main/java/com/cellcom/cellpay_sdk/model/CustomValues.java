package com.cellcom.cellpay_sdk.model;

public class CustomValues {
    private String internalName;
    private String fieldId;
    private String value;

    public CustomValues(String internalName, String fieldId, String value) {
        this.internalName = internalName;
        this.fieldId = fieldId;
        this.value = value;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
