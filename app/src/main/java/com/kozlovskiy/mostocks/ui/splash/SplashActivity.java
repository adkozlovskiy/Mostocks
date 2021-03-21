package com.kozlovskiy.mostocks.ui.splash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.ui.main.MainActivity;
import com.kozlovskiy.mostocks.utils.NetworkUtil;

public class SplashActivity extends AppCompatActivity
        implements SplashView {

    private SplashPresenter splashPresenter;
    private static final String PREFIX = "_in";
    public static final String KEY_STOCKS_INTENT = Stock.class.getSimpleName() + PREFIX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashPresenter = new SplashPresenter(this, this);

        if (NetworkUtil.isNetworkConnectionNotGranted(this)) {
            splashPresenter.buildNoNetworkDialog();
        }

        splashPresenter.initializeTickers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashPresenter.unsubscribe();
        // TODO: 21.03.2021 cancel loading
    }

    @Override
    public void startMainActivity(String json) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY_STOCKS_INTENT, json);
        startActivity(intent);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }
}