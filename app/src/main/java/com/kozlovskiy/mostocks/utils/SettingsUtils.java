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
    private static final long UPDATE_INTERVAL = 60 * 1000; // 60 * 60 * 24 * 1000

    public static void updateStocksUptime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_STOCKS_UPTIME, new Date().getTime());
        editor.apply();
    }

    /**
     * @return time in millis when stocks was loaded last time.
     */
    public static boolean cacheIsUpToDate(Context context) {
        return new Date().getTime() - context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE)
                .getLong(KEY_STOCKS_UPTIME, 0) < UPDATE_INTERVAL;
    }
}
