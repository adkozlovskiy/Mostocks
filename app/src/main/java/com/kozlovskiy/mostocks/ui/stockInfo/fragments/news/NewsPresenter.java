package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import android.app.AlertDialog;
import android.content.Context;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenter {

    public static final String TAG = NewsPresenter.class.getSimpleName();
    private final NewsView newsView;
    private final StocksRepository stocksRepository;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final AlertDialog.Builder builder;
    private String symbol;

    public NewsPresenter(NewsView newsView, Context context) {
        this.newsView = newsView;
        stocksRepository = new StocksRepository(context);
        builder = new AlertDialog.Builder(context);
    }

    public void initializeNews(String symbol) {
        this.symbol = symbol;
        Calendar calendar = Calendar.getInstance();
        String to = formatter.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, -30);
        String from = formatter.format(calendar.getTime());

        stocksRepository.getCompanyNews(symbol, from, to)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<News>>() {
                    @Override
                    public void onSuccess(@NonNull List<News> news) {
                        newsView.updateNews(news);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void buildErrorLoadingDialog(Throwable e) {
        builder.setTitle(R.string.loading_error);
        if (e instanceof SocketTimeoutException) {
            builder.setMessage(R.string.timed_out);

        } else {
            builder.setMessage(R.string.unknown_error);

        }

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeNews(symbol))
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        newsView.showDialog(builder.create());
    }
}
