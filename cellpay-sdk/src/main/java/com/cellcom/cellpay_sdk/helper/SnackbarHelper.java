package com.cellcom.cellpay_sdk.helper;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.cellcom.cellpay_sdk.R;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class SnackbarHelper {

    public static void configSnackbar(Context context, Snackbar snackbar) {
        addMargins(snackbar);
        //fromTop(snackbar);
        setRoundBordersBg(context,snackbar);
        ViewCompat.setElevation(snackbar.getView(), 6f);
    }

    private static void addMargins(Snackbar snackbar){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins(8, 8, 8, 8);

        snackbar.getView().setLayoutParams(params);
    }

    private static void fromTop(Snackbar snackbar){
        View view = snackbar.getView();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
    }

    private static void setRoundBordersBg(Context context, Snackbar snackbar){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            snackbar.getView().setBackground(context.getDrawable(R.drawable.snackbar));
        }
    }
}
