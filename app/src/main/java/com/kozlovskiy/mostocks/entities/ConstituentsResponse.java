package com.kozlovskiy.mostocks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConstituentsResponse {

    @SerializedName("constituents")
    @Expose
    private List<String> constituents = null;

    @SerializedName("symbol")
    @Expose
    private String symbol;

    public List<String> getConstituents() {
        return constituents;
    }

    public void setConstituents(List<String> constituents) {
        this.constituents = constituents;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
