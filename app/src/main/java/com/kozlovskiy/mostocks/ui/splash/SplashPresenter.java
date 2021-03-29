package com.kozlovskiy.mostocks.ui.splash;

import android.app.AlertDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stock.Stock;
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

    public void initializeSymbols() {
        if (NetworkUtil.isNetworkConnectionNotGranted(context))
            buildNoNetworkDialog();

        if (CacheUtil.getTickersCachedFlag(context)) {
            splashView.startMainActivity("");

        } else {
            StocksRepository stocksRepository = new StocksRepository(context);
            stocksRepository.getStockSymbols()
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

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeSymbols())
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        splashView.showDialog(builder.create());
    }

    public void buildNoNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.network_error)
                .setMessage(R.string.no_network_message)
                .setPositiveButton(R.string.retry, (di, id) -> initializeSymbols())
                .setNegativeButton(R.string.exit, (di, id) -> di.cancel());

        splashView.showDialog(builder.create());
    }

    public void unsubscribe() {
        splashView = null;
    }
}
