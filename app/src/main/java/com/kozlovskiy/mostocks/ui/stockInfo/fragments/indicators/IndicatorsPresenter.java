package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators;

import android.app.AlertDialog;
import android.content.Context;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.utils.IndicatorUtil;

import java.net.SocketTimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class IndicatorsPresenter {

    public static final String TAG = IndicatorsPresenter.class.getSimpleName();
    private final AlertDialog.Builder builder;
    public final StocksRepository stocksRepository;
    private final IndicatorsView indicatorsView;
    private final Context context;
    private final String symbol;

    public IndicatorsPresenter(IndicatorsView indicatorsView, Context context, String symbol) {
        this.indicatorsView = indicatorsView;
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
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
                        buildErrorLoadingDialog(e);
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

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeIndicators())
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        indicatorsView.showDialog(builder.create());
    }
}
