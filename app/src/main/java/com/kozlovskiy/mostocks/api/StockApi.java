package com.kozlovskiy.mostocks.api;

import com.kozlovskiy.mostocks.entities.ConstituentsResponse;
import com.kozlovskiy.mostocks.entities.Stock;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockApi {

    /**
     * Request which get tickers by index.
     *
     * @param symbol is index.
     * @param token  is auth token.
     * @return callable object of {@link ConstituentsResponse}
     */
    @GET("index/constituents?")
    Call<ConstituentsResponse> getTickers(@Query("symbol") String symbol, @Query("token") String token);

    @GET("stock/profile2?")
    Call<Stock> getCompanyInfo(@Query("symbol") String symbol, @Query("token") String token);

    @GET("quote?")
    Call<Stock> getStockCost(@Query("symbol") String symbol, @Query("token") String token);
}
