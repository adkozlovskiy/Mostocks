package com.kozlovskiy.mostocks.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.models.stock.Quote;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.models.sys.Uptime;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class CacheUtil {

    public static final String TAG = CacheUtil.class.getSimpleName();
    public static final String KEY_SETTINGS = CacheUtil.class.getSimpleName();
    public static final String POSTFIX = "_ut";
    public static final String KEY_QUOTE_UPTIME = Quote.class.getSimpleName() + POSTFIX;
    public static final String KEY_TICKERS_CACHED = Stock.class.getSimpleName() + POSTFIX;
    public static final long QUOTE_UPDATE_INTERVAL = 120 * 1000 * 60;
    public static final long NEWS_UPDATE_INTERVAL = TimeUnit.DAYS.toMillis(1);

    public static void updateQuoteUptime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_QUOTE_UPTIME, new Date().getTime());
        editor.apply();
    }

    public static boolean quoteCacheIsUpToDate(Context context) {
        return new Date().getTime() - context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE)
                .getLong(KEY_QUOTE_UPTIME, 0) < QUOTE_UPDATE_INTERVAL;
    }

    public static void updateNewsUptime(String symbol, Context context) {
        StocksDao stocksDao = ((AppDelegate) context.getApplicationContext()).getDatabase().getDao();

        if (stocksDao.getUptimeSymbol(symbol) != null)
            stocksDao.setNewsUptime(symbol, new Date().getTime() / 1000);

        else {
            Uptime uptime = new Uptime();
            uptime.setSymbol(symbol);
            uptime.setNewsUptime(new Date().getTime() / 1000);
            stocksDao.addUptimeSymbol(uptime);
        }
    }

    public static boolean newsCacheIsUpToDate(String symbol, Context context) {
        StocksDao stocksDao = ((AppDelegate) context.getApplicationContext()).getDatabase().getDao();
        long uptime = stocksDao.getNewsUptime(symbol);
        return new Date().getTime() - uptime * 1000 < NEWS_UPDATE_INTERVAL;
    }

    public static boolean getTickersCachedFlag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_TICKERS_CACHED, false);
    }

    public static void setTickersCachedFlag(Context context, boolean flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_TICKERS_CACHED, flag);
        editor.apply();
    }
}
