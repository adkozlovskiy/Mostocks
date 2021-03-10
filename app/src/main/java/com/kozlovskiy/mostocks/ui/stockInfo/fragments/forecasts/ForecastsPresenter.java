package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.content.Context;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.TechAnalysisResponse;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import java.util.ArrayList;

import im.dacer.androidcharts.PieHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ForecastsPresenter {

    private final ForecastsView forecastsView;
    private final ArrayList<PieHelper> entries;
    public static final String TAG = ForecastsPresenter.class.getSimpleName();
    private final StocksRepository stocksRepository;

    public ForecastsPresenter(ForecastsView forecastsView, Context context) {
        this.forecastsView = forecastsView;
        stocksRepository = new StocksRepository(context);

        entries = new ArrayList<>();
    }

    public void initializeGraphData(String ticker) {
        stocksRepository.updateTechAnalysis(ticker)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<TechAnalysisResponse.TechnicalAnalysis>() {
                    @Override
                    public void onSuccess(@NonNull TechAnalysisResponse.TechnicalAnalysis object) {

                        int buyMark = Integer.parseInt(object.getCount().get("buy"));
                        int holdMark = Integer.parseInt(object.getCount().get("neutral"));
                        int sellMark = Integer.parseInt(object.getCount().get("sell"));
                        float sum = buyMark + holdMark + sellMark;

                        entries.add(new PieHelper(Math.round((buyMark / sum) * 100), R.color.buyIndicatorColor));
                        entries.add(new PieHelper(Math.round((holdMark / sum) * 100), R.color.holdIndicatorColor));
                        entries.add(new PieHelper(Math.round((sellMark / sum) * 100), R.color.sellIndicatorColor));

                        buildGraph();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void buildGraph() {
        forecastsView.showGraph(entries);
    }
}
