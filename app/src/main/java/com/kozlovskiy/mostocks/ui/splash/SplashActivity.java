package com.kozlovskiy.mostocks.ui.splash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.ui.stocks.StocksActivity;

public class SplashActivity extends AppCompatActivity
        implements SplashView {

    public static final String TAG = SplashActivity.class.getSimpleName();
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StocksRepository stocksRepository = new StocksRepository(this);
        splashPresenter = new SplashPresenter(this, stocksRepository);
        splashPresenter.updateStocksFromServer();
    }

    @Override
    public void startStocksActivity() {
        startActivity(new Intent(SplashActivity.this, StocksActivity.class));
    }

    @Override
    public void showAlertDialog(Dialog dialog) {
        // TODO: 19.02.2021
        Log.d(TAG, "showErrorAlertDialog: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashPresenter.unsubscribe();
    }
}