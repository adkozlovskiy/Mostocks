package com.kozlovskiy.mostocks.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.ui.main.MainActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StocksRepository stocksRepository = new StocksRepository(this);
        stocksRepository.initializeTickers()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Stock>>() {
                    @Override
                    public void onSuccess(@NonNull List<Stock> stocks) {
                        Gson gson = new Gson();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("stocks", gson.toJson(stocks));
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}