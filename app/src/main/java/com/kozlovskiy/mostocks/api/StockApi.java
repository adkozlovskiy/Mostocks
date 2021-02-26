package com.kozlovskiy.mostocks.api;

import com.kozlovskiy.mostocks.entities.ConstituentsResponse;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.entities.StockCost;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockApi {

    /**
     * Requests a set of tickers included in the passed index.
     *
     * @param symbol is index.
     * @param token  is auth token.
     * @return callable object of {@link ConstituentsResponse} which contains set of tickers we need.
     */
    @GET("index/constituents?")
    Call<ConstituentsResponse> getTickers(@Query("symbol") String symbol, @Query("token") String token);

    /**
     * Requests the profile of the company that owns the transmitted ticker.
     *
     * @param symbol is ticker.
     * @param token  is auth token.
     * @return update object of {@link Stock}.
     */
    @GET("stock/profile2?")
    Call<Stock> getStockProfile(@Query("symbol") String symbol, @Query("token") String token);

    /**
     * Requests the costs of the stock that owns the transmitted ticker.
     *
     * @param symbol is ticker.
     * @param token  is auth token.
     * @return callable object of {@link StockCost}.
     */
    @GET("quote?")
    Call<Stock> getStockCost(@Query("symbol") String symbol, @Query("token") String token);
}
