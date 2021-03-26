package com.kozlovskiy.mostocks.models.stock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockResponse {

    @SerializedName("data")
    @Expose
    private List<Stock> data;

    public List<Stock> getData() {
        return data;
    }

    public void setData(List<Stock> data) {
        this.data = data;
    }
}
