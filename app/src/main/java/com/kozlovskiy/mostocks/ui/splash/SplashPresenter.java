package com.kozlovskiy.mostocks.ui.splash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.utils.CacheUtil;
import com.kozlovskiy.mostocks.utils.NetworkUtil;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter {

    private SplashView splashView;
    private final Context context;
    private final AlertDialog.Builder builder;

    public SplashPresenter(SplashView splashView, Context context) {
        this.splashView = splashView;
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
    }

    public void initializeTickers() {
        if (CacheUtil.getTickersCachedFlag(context)) {
            splashView.startMainActivity("");

        } else {
            StocksRepository stocksRepository = new StocksRepository(context);
            stocksRepository.getStockData()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Stock>>() {
                        @Override
                        public void onSuccess(@NonNull List<Stock> stocks) {
                            Gson gson = new Gson();
                            splashView.startMainActivity(gson.toJson(stocks));

                            CacheUtil.setTickersCachedFlag(context, true);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            buildErrorLoadingDialog(e);
                        }
                    });
        }
    }

    public void buildErrorLoadingDialog(Throwable e) {
        builder.setTitle(R.string.loading_error);
        if (e instanceof SocketTimeoutException) {
            builder.setMessage(R.string.timed_out);

        } else {
            builder.setMessage(R.string.unknown_error);

        }

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeTickers())
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        splashView.showDialog(builder.create());
    }

    public void buildNoNetworkDialog() {
        Dialog dialog = NetworkUtil.buildNoNetworkDialog(context);
        splashView.showDialog(dialog);
    }

    public void unsubscribe() {
        splashView = null;
    }
}
