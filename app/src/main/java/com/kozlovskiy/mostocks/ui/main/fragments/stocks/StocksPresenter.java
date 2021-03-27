package com.kozlovskiy.mostocks.ui.main.fragments.stocks;

import android.app.AlertDialog;
import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.utils.CacheUtil;
import com.kozlovskiy.mostocks.utils.NetworkUtil;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StocksPresenter {

    public static final String TAG = StocksPresenter.class.getSimpleName();
    private final StocksRepository stocksRepository;
    private final AlertDialog.Builder builder;
    private final StocksDao stocksDao;
    private final Context context;
    private List<Stock> stocks;

    private StocksView stocksView;

    public StocksPresenter(StocksView stocksView, Context context, List<Stock> stocks) {
        this.stocksRepository = new StocksRepository(context);
        this.builder = new AlertDialog.Builder(context);
        this.stocksView = stocksView;
        this.context = context;
        this.stocks = stocks;

        this.stocksDao = ((AppDelegate) context
                .getApplicationContext())
                .getDatabase()
                .getDao();

    }

    public void initializeQuotes() {
        if (NetworkUtil.isNetworkConnectionNotGranted(context))
            buildNoNetworkDialog();

        if (CacheUtil.quoteCacheIsUpToDate(context)) {
            stocksView.updateStocks(stocksDao.getStocks());
            stocksView.stopRefreshing();

        } else {
            stocksRepository.getSymbolQuotes(stocks).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Stock>>() {
                        @Override
                        public void onSuccess(@NonNull List<Stock> stocks) {
                            CacheUtil.updateQuoteUptime(context);
                            stocksView.updateStocks(stocks);
                            stocksView.stopRefreshing();
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

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeQuotes())
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        stocksView.showDialog(builder.create());
    }


    public void buildNoNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.network_error)
                .setMessage(R.string.no_network_message)
                .setPositiveButton(R.string.retry, (di, id) -> initializeQuotes())
                .setNegativeButton(R.string.exit, (di, id) -> di.cancel());

        stocksView.showDialog(builder.create());
    }

    public void unsubscribe() {
        stocksView = null;
    }
}
