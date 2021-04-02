package com.kozlovskiy.mostocks.repo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.api.finnhub.FinnhubService;
import com.kozlovskiy.mostocks.api.mstack.MStackService;
import com.kozlovskiy.mostocks.models.chart.Candles;
import com.kozlovskiy.mostocks.models.stock.Quote;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.models.stock.StockResponse;
import com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.models.stockInfo.Recommendation;
import com.kozlovskiy.mostocks.models.stockInfo.TechAnalysisResponse;
import com.kozlovskiy.mostocks.room.RoomDelegate;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.utils.CacheUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksRepository {

    public static final String TAG = StocksRepository.class.getSimpleName();
    private final RoomDelegate roomDelegate;
    private final StocksDao stocksDao;
    private final Context context;

    public StocksRepository(Context context) {
        this.context = context;

        roomDelegate = new RoomDelegate(context);
        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();
    }

    public Single<List<Stock>> getStockSymbols() {
        return Single.create(emitter -> MStackService.getInstance().getApi()
                .getStockData("XNAS", "25", MStackService.TOKEN)
                .enqueue(new Callback<StockResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StockResponse> call, @NonNull Response<StockResponse> response) {
                        if (response.body() != null) {
                            List<Stock> stocks = response.body().getData();
                            roomDelegate.cacheStocks(stocks);
                            emitter.onSuccess(stocks);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StockResponse> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public Single<List<Stock>> getSymbolQuotes(List<Stock> stocks) {
        return Single.create(emitter -> {
            List<Stock> updatedStocks = new ArrayList<>();
            for (Stock stock : stocks) {
                FinnhubService.getInstance().getApi()
                        .getSymbolQuote(stock.getSymbol(), FinnhubService.TOKEN)
                        .enqueue(new Callback<Quote>() {
                            @Override
                            public void onResponse(@NonNull Call<Quote> call, @NonNull Response<Quote> response) {
                                if (response.body() != null) {
                                    Quote quote = response.body();
                                    stock.setOpen(quote.getOpen());
                                    stock.setCurrent(quote.getCurrent());
                                    stock.setLow(quote.getLow());
                                    stock.setHigh(quote.getHigh());
                                    stock.setPrevious(quote.getPrevious());
                                    stock.setTime(quote.getTime());

                                    updatedStocks.add(stock);

                                    if (updatedStocks.size() == stocks.size()) {
                                        stocksDao.updateStocks(stocks);
                                        emitter.onSuccess(updatedStocks);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Quote> call, @NonNull Throwable t) {
                                emitter.onError(t);
                            }
                        });
            }
        });
    }

    public Single<List<News>> getCompanyNews(String symbol, String from, String to) {
        return Single.create(emitter -> {
            if (CacheUtil.newsCacheIsUpToDate(symbol, context))
                emitter.onSuccess(stocksDao.getNewsBySymbol(symbol));

            else FinnhubService.getInstance().getApi()
                    .getCompanyNews(symbol, from, to, FinnhubService.TOKEN)
                    .enqueue(new Callback<List<News>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<News>> call, @NonNull Response<List<News>> response) {
                            if (response.body() != null) {
                                List<News> newsList = response.body();

                                for (News news : newsList) {
                                    news.setSymbol(symbol);
                                }

                                stocksDao.cacheNews(newsList);
                                CacheUtil.updateNewsUptime(symbol, context);
                                emitter.onSuccess(newsList);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<News>> call, @NonNull Throwable t) {
                            emitter.onError(t);
                        }
                    });

        });
    }

    public Single<TechAnalysisResponse.TechAnalysis> getSymbolTechAnalysis(String symbol) {
        return Single.create(emitter -> FinnhubService.getInstance().getApi()
                .getTechAnalysis(symbol, "D", FinnhubService.TOKEN)
                .enqueue(new Callback<TechAnalysisResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TechAnalysisResponse> call, @NonNull Response<TechAnalysisResponse> response) {
                        if (response.body() != null) {
                            TechAnalysisResponse.TechAnalysis techAnalysis = response.body()
                                    .getTechAnalysis();

                            emitter.onSuccess(techAnalysis);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TechAnalysisResponse> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public Single<Candles> getSymbolCandles(String symbol, String resolution, long from, long to) {
        return Single.create(emitter -> FinnhubService.getInstance().getApi()
                .getSymbolCandles(symbol, resolution, String.valueOf(from), String.valueOf(to), FinnhubService.TOKEN)
                .enqueue(new Callback<Candles>() {
                    @Override
                    public void onResponse(@NonNull Call<Candles> call, @NonNull Response<Candles> response) {
                        if (response.body() != null) {
                            Candles candles = response.body();

                            if (candles.getStatus().equals("ok"))
                                emitter.onSuccess(candles);

                            else emitter.onError(new NullPointerException());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Candles> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public Single<List<Recommendation>> getSymbolRecommendation(String symbol) {
        return Single.create(emitter -> {
            if (CacheUtil.recommendationCacheIsUpToDate(symbol, context))
                emitter.onSuccess(stocksDao.getRecommendationsBySymbol(symbol));

            else FinnhubService.getInstance().getApi()
                    .getSymbolRecommendation(symbol, FinnhubService.TOKEN)
                    .enqueue(new Callback<List<Recommendation>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Recommendation>> call, @NonNull Response<List<Recommendation>> response) {
                            if (response.body() != null) {
                                List<Recommendation> recommendations = response.body();

                                roomDelegate.cacheRecommendations(recommendations);
                                CacheUtil.updateRecommendationUptime(symbol, context);
                                emitter.onSuccess(recommendations);
                                Log.d(TAG, "getSymbolRecommendation: loaded");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Recommendation>> call, @NonNull Throwable t) {
                            emitter.onError(t);
                        }
                    });
        });
    }

    public Single<IndicatorsResponse.Indicators> getSymbolIndicators(String symbol) {
        return Single.create(emitter -> {
            if (CacheUtil.indicatorsCacheIsUpToDate(symbol, context))
                emitter.onSuccess(stocksDao.getIndicatorsBySymbol(symbol));

            else FinnhubService.getInstance().getApi()
                    .getSymbolIndicators(symbol, "all", FinnhubService.TOKEN)
                    .enqueue(new Callback<IndicatorsResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<IndicatorsResponse> call, @NonNull Response<IndicatorsResponse> response) {
                            if (response.body() != null) {
                                IndicatorsResponse indicatorsResponse = response.body();
                                IndicatorsResponse.Indicators indicators = indicatorsResponse.getIndicators();

                                indicators.setSymbol(symbol);
                                roomDelegate.cacheIndicators(indicators);
                                CacheUtil.updateIndicatorsUptime(symbol, context);
                                emitter.onSuccess(indicators);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<IndicatorsResponse> call, @NonNull Throwable t) {
                            emitter.onError(t);
                        }
                    });

        });
    }
}
