package com.kozlovskiy.mostocks.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class StockCost {

    @PrimaryKey
    @ColumnInfo(name = "ticker")
    @SerializedName("ticker")
    @Expose
    @NonNull
    private String ticker;

    @ColumnInfo(name = "c")
    @SerializedName("c")
    @Expose
    private double currentCost;

    @ColumnInfo(name = "pc")
    @SerializedName("pc")
    @Expose
    private double previousCost;

    @ColumnInfo(name = "o")
    @SerializedName("o")
    @Expose
    private double openCost;

    @ColumnInfo(name = "l")
    @SerializedName("l")
    @Expose
    private double lowDailyCost;

    @ColumnInfo(name = "h")
    @SerializedName("h")
    @Expose
    private double highDailyCost;

    public StockCost(String ticker, double currentCost, double previousCost, double openCost, double lowDailyCost, double highDailyCost) {
        this.ticker = ticker;
        this.currentCost = currentCost;
        this.previousCost = previousCost;
        this.openCost = openCost;
        this.lowDailyCost = lowDailyCost;
        this.highDailyCost = highDailyCost;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }

    public double getPreviousCost() {
        return previousCost;
    }

    public void setPreviousCost(double previousCost) {
        this.previousCost = previousCost;
    }

    public double getOpenCost() {
        return openCost;
    }

    public void setOpenCost(double openCost) {
        this.openCost = openCost;
    }

    public double getLowDailyCost() {
        return lowDailyCost;
    }

    public void setLowDailyCost(double lowDailyCost) {
        this.lowDailyCost = lowDailyCost;
    }

    public double getHighDailyCost() {
        return highDailyCost;
    }

    public void setHighDailyCost(double highDailyCost) {
        this.highDailyCost = highDailyCost;
    }
}
