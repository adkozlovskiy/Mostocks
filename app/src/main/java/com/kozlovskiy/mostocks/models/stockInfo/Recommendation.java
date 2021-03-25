package com.kozlovskiy.mostocks.models.stockInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommendation {

    @SerializedName("symbol")
    @Expose
    private String symbol;

    @SerializedName("buy")
    @Expose
    private int buySignals;

    @SerializedName("hold")
    @Expose
    private int holdSignals;

    @SerializedName("period")
    @Expose
    private String period;

    @SerializedName("sell")
    @Expose
    private int sellSignals;

    @SerializedName("strongBuy")
    @Expose
    private int strongBuySignals;

    @SerializedName("strongSell")
    @Expose
    private int strongSellSignals;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getBuySignals() {
        return buySignals;
    }

    public void setBuySignals(int buySignals) {
        this.buySignals = buySignals;
    }

    public int getHoldSignals() {
        return holdSignals;
    }

    public void setHoldSignals(int holdSignals) {
        this.holdSignals = holdSignals;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getSellSignals() {
        return sellSignals;
    }

    public void setSellSignals(int sellSignals) {
        this.sellSignals = sellSignals;
    }

    public int getStrongBuySignals() {
        return strongBuySignals;
    }

    public void setStrongBuySignals(int strongBuySignals) {
        this.strongBuySignals = strongBuySignals;
    }

    public int getStrongSellSignals() {
        return strongSellSignals;
    }

    public void setStrongSellSignals(int strongSellSignals) {
        this.strongSellSignals = strongSellSignals;
    }
}
