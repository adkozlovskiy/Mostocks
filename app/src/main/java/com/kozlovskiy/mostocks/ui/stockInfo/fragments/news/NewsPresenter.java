package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import android.content.Context;
import android.util.Log;

import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.repo.StocksRepository;

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
    private final Context context;
    private final StocksRepository stocksRepository;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public NewsPresenter(NewsView newsView, Context context) {
        this.context = context;
        this.newsView = newsView;
        stocksRepository = new StocksRepository(context);
    }

    public void initializeNews(String symbol) {
        Log.d(TAG, "initializeNews: ");
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
}
