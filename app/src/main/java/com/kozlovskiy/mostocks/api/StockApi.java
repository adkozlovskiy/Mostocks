package com.kozlovskiy.mostocks.api;

import com.kozlovskiy.mostocks.entities.ConstituentsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockApi {

    @GET("index/constituents?")
    Call<ConstituentsResponse> getStocks(@Query("symbol") String symbol, @Query("token") String token);
}
