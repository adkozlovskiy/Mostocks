package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.content.Context;

import com.kozlovskiy.mostocks.models.stockInfo.Recommendation;
import com.kozlovskiy.mostocks.models.stockInfo.TechAnalysisResponse;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ForecastsPresenter {

    private final ForecastsView forecastsView;
    public static final String TAG = ForecastsPresenter.class.getSimpleName();
    private final StocksRepository stocksRepository;

    public ForecastsPresenter(ForecastsView forecastsView, Context context) {
        this.forecastsView = forecastsView;
        stocksRepository = new StocksRepository(context);
    }

    public void initializeRecommendation(String symbol) {
        stocksRepository.getSymbolRecommendation(symbol)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Recommendation>() {
                    @Override
                    public void onSuccess(@NonNull Recommendation recommendation) {
                        forecastsView.showRecommendationsResult(recommendation.getPeriod());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void initializeTechAnalysis(String symbol) {
        stocksRepository.updateTechAnalysis(symbol)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<TechAnalysisResponse.TechnicalAnalysis>() {
                    @Override
                    public void onSuccess(@NonNull TechAnalysisResponse.TechnicalAnalysis object) {
                        forecastsView.showTechAnalysisResult(object.getSignal());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}
