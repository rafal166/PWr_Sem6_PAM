package com.example.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    /**
     * Metoda umożliwia pokazanie okna dialogowego z informacją, że jakieś informacje są ładowane
     */
    void showLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    /**
     * Umożliwia ukrycie okna dialogowego logowania
     */
    void hideLoading(){
        dialog.dismiss();
    }
}
