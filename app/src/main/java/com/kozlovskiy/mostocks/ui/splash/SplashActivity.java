package com.kozlovskiy.mostocks.ui.splash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity
        implements SplashView {

    private SplashPresenter splashPresenter;
    private static final String PREFIX = "_in";
    public static final String KEY_STOCKS_INTENT = Stock.class.getSimpleName() + PREFIX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashPresenter = new SplashPresenter(this, this);
        splashPresenter.initializeTickers();
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void startMainActivity(String json) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY_STOCKS_INTENT, json);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashPresenter.unsubscribe();
    }
}