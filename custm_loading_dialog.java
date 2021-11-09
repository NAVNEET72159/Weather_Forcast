package com.example.weatherforcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class custm_loading_dialog {

    private Activity activity;
    private AlertDialog alertDialog;

    custm_loading_dialog(Activity myActivity)
    {
        activity = myActivity;
    }

    void startLoading()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custm_progress_bar, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
    }

    void dismissLoading()
    {
        alertDialog.dismiss();
    }
}
