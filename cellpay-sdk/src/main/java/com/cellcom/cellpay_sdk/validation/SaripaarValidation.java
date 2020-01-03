package com.cellcom.cellpay_sdk.validation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

public class SaripaarValidation extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {
    protected Validator validator;
    protected boolean validated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    protected void validate() {
        if (validator != null) {
            validator.validate();
        }
    }

    @Override
    public void onClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        validated = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validated = false;
    }
}
