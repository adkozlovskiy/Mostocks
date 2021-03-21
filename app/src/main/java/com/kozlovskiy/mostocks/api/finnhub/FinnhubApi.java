package com.kozlovskiy.mostocks.api.finnhub;

import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Quote;
import com.kozlovskiy.mostocks.entities.TechAnalysisResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FinnhubApi {

    @GET("quote?")
    Call<Quote> getSymbolQuote(@Query("symbol") String symbol, @Query("token") String token);

    @GET("company-news?")
    Call<List<News>> getCompanyNews(@Query("symbol") String symbol, @Query("from") String from, @Query("to") String to, @Query("token") String token);

    @GET("scan/technical-indicator?")
    Call<TechAnalysisResponse> getTechAnalysis(@Query("symbol") String symbol, @Query("resolution") String resolution, @Query("token") String token);
}