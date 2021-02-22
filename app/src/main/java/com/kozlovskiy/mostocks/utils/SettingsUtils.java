package com.kozlovskiy.mostocks.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kozlovskiy.mostocks.entities.StockProfile;
import com.kozlovskiy.mostocks.entities.Ticker;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SettingsUtils {

    public static final String KEY_SETTINGS = SettingsUtils.class.getSimpleName();
    public static final String POSTFIX = "_ut";
    public static final String KEY_PROFILES_UPTIME = StockProfile.class.getSimpleName() + POSTFIX;
    public static final String KEY_TICKERS_UPTIME = Ticker.class.getSimpleName() + POSTFIX;

    public static void setUptime(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, new Date().getTime());
        editor.apply();

    }

    public static long getProfilesUptime(Context context) {
        return context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE)
                .getLong(KEY_PROFILES_UPTIME, 0);
    }

    public static long getTickersUptime(Context context) {
        return context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE)
                .getLong(KEY_TICKERS_UPTIME, 0);
    }

}
