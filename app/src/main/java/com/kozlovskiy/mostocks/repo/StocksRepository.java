package com.kozlovskiy.mostocks.repo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.api.finnhub.FinnhubService;
import com.kozlovskiy.mostocks.api.mstack.MStackService;
import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Quote;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.entities.StockData;
import com.kozlovskiy.mostocks.entities.TechAnalysisResponse;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksRepository {

    private final StocksDao stocksDao;
    public static final String TAG = StocksRepository.class.getSimpleName();

    public StocksRepository(Context context) {
        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();
    }

    public Single<List<Stock>> getStockData() {
        Log.d(TAG, "getStockData: stocks loading...");
        return Single.create(emitter -> MStackService.getInstance().getApi()
                .getStockData("XNAS", "30", MStackService.TOKEN)
                .enqueue(new Callback<StockData>() {
                    @Override
                    public void onResponse(@NonNull Call<StockData> call, @NonNull Response<StockData> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "getStockData: stocks loaded...");
                            List<Stock> stocks = response.body().getData();
                            emitter.onSuccess(stocks);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StockData> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public Single<List<Stock>> getSymbolQuotes(List<Stock> stocks) {
        Log.d(TAG, "getSymbolQuotes: stocks costs loading...");
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

    public Single<List<News>> updateNews(String ticker) {
        return Single.create(emitter -> FinnhubService.getInstance().getApi()
                .getCompanyNews(ticker, "2021-01-01", "2021-03-01", FinnhubService.TOKEN)
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<News>> call, @NonNull Response<List<News>> response) {
                        if (response.body() != null) {
                            List<News> newsList = response.body();

                            for (News news : newsList) {
                                news.setTicker(ticker);
                            }

                            stocksDao.cacheNews(newsList);
                            emitter.onSuccess(newsList);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<News>> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public Single<TechAnalysisResponse.TechnicalAnalysis> updateTechAnalysis(String ticker) {
        return Single.create(emitter -> FinnhubService.getInstance().getApi()
                .getTechAnalysis(ticker, "M", FinnhubService.TOKEN)
                .enqueue(new Callback<TechAnalysisResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TechAnalysisResponse> call, @NonNull Response<TechAnalysisResponse> response) {
                        if (response.body() != null) {
                            TechAnalysisResponse.TechnicalAnalysis technicalAnalysis = response
                                    .body()
                                    .getTechnicalAnalysis();

                            emitter.onSuccess(technicalAnalysis);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TechAnalysisResponse> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }
}
