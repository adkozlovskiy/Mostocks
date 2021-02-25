package com.kozlovskiy.mostocks.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kozlovskiy.mostocks.entities.Stock;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SettingsUtils {

    public static final String KEY_SETTINGS = SettingsUtils.class.getSimpleName();
    public static final String POSTFIX = "_ut";
    public static final String KEY_STOCKS_UPTIME = Stock.class.getSimpleName() + POSTFIX;

    public static void updateStocksUptime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_STOCKS_UPTIME, new Date().getTime());
        editor.apply();
    }

    /**
     * @return time in millis when profiles was loaded last time.
     */
    public static long getStocksUptime(Context context) {
        return context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE)
                .getLong(KEY_STOCKS_UPTIME, 0);
    }
}
