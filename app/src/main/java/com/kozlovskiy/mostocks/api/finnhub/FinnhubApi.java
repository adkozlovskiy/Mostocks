package com.kozlovskiy.mostocks.api.finnhub;

import com.kozlovskiy.mostocks.models.candles.Candles;
import com.kozlovskiy.mostocks.models.stock.Quote;
import com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.models.stockInfo.Recommendation;
import com.kozlovskiy.mostocks.models.stockInfo.TechAnalysisResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FinnhubApi {

    /**
     * @param symbol is stock symbol or ticker.
     * @param token  is finnhub auth token.
     * @return current symbol's quotes
     */
    @GET("quote?")
    Call<Quote> getSymbolQuote(@Query("symbol") String symbol, @Query("token") String token);

    /**
     *
     * @param symbol is stock symbol or ticker.
     * @param from is start date.
     * @param to is end date.
     * @param token is auth token.
     * @return
     */
    @GET("company-news?")
    Call<List<News>> getCompanyNews(@Query("symbol") String symbol, @Query("from") String from, @Query("to") String to, @Query("token") String token);

    @GET("scan/technical-indicator?")
    Call<TechAnalysisResponse> getTechAnalysis(@Query("symbol") String symbol, @Query("resolution") String resolution, @Query("token") String token);

    @GET("stock/recommendation?")
    Call<List<Recommendation>> getSymbolRecommendation(@Query("symbol") String symbol, @Query("token") String token);

    @GET("stock/metric?")
    Call<IndicatorsResponse> getSymbolIndicators(@Query("symbol") String symbol, @Query("metric") String metric, @Query("token") String token);

    @GET("stock/candle?")
    Call<Candles> getSymbolCandles(
            @Query("symbol") String symbol,
            @Query("resolution") String resolution,
            @Query("from") String from,
            @Query("to") String to,
            @Query("token") String token
    );
}