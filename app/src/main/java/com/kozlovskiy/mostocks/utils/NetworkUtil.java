package com.kozlovskiy.mostocks.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kozlovskiy.mostocks.R;

public class NetworkUtil {

    /**
     * @param context context.
     * @return true if client's network is not good, else false.
     */
    public static boolean isNetworkConnectionNotGranted(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo == null || !netInfo.isConnectedOrConnecting();
    }

    public static Dialog buildNoNetworkDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.network_error)
                .setMessage(R.string.no_network_message)
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        return builder.create();
    }
}
