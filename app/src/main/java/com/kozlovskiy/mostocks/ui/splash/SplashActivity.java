package com.kozlovskiy.mostocks.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.ui.stocks.StocksActivity;

import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StocksRepository stocksRepository = new StocksRepository(this);
        stocksRepository.updateTickersFromServer()
                .subscribeOn(Schedulers.io())
                .doOnComplete(this::startStocksActivity)
                .doOnError(this::showErrorAlertDialog)
                .subscribe();
    }

    private void showErrorAlertDialog(Throwable throwable) {
        // TODO: 19.02.2021
        Log.d(TAG, "showErrorAlertDialog: ");
    }

    private void startStocksActivity() {
        startActivity(new Intent(SplashActivity.this, StocksActivity.class));
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
}