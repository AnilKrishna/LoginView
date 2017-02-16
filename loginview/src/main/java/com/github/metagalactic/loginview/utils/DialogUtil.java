package com.github.metagalactic.loginview.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

public class DialogUtil {
    public static Dialog getErrorDialog(int errorCode, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(errorCode);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }
}
