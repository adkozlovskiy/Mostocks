package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators;

import android.content.Context;

import com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.utils.IndicatorUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class IndicatorsPresenter {

    public static final String TAG = IndicatorsPresenter.class.getSimpleName();
    public final StocksRepository stocksRepository;
    private final IndicatorsView indicatorsView;
    private final Context context;
    private final String symbol;

    public IndicatorsPresenter(IndicatorsView indicatorsView, Context context, String symbol) {
        this.indicatorsView = indicatorsView;
        this.context = context;
        this.stocksRepository = new StocksRepository(context);
        this.symbol = symbol;
    }

    public void initializeIndicators() {
        stocksRepository.getSymbolIndicators(symbol)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<IndicatorsResponse.Indicators>() {
                    @Override
                    public void onSuccess(@NonNull IndicatorsResponse.Indicators indicators) {
                        indicatorsView.showIndicators(IndicatorUtil.getIndicatorList(context, indicators));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}
