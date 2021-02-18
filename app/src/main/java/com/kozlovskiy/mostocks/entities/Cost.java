package com.kozlovskiy.mostocks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cost {

    @SerializedName("c")
    @Expose
    double currentCost;

    @SerializedName("pc")
    @Expose
    double previousCost;

    @SerializedName("o")
    @Expose
    double openPrice;

    @SerializedName("l")
    @Expose
    double lowDailyPrice;

    @SerializedName("h")
    @Expose
    double highDailyPrice;

    public Cost(double currentCost, double previousCost) {
        this.currentCost = currentCost;
        this.previousCost = previousCost;
    }

    public Cost(double currentCost, double previousCost, double openPrice, double lowDailyPrice, double highDailyPrice) {
        this.currentCost = currentCost;
        this.previousCost = previousCost;
        this.openPrice = openPrice;
        this.lowDailyPrice = lowDailyPrice;
        this.highDailyPrice = highDailyPrice;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public double getPreviousCost() {
        return previousCost;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public double getLowDailyPrice() {
        return lowDailyPrice;
    }

    public double getHighDailyPrice() {
        return highDailyPrice;
    }
}
