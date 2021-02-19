package com.kozlovskiy.mostocks.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Stock {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "ticker")
    @SerializedName("ticker")
    @Expose
    private String ticker;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    @Expose
    private String currency;

    @ColumnInfo(name = "logo")
    @SerializedName("logo")
    @Expose
    private String logo;

    @ColumnInfo(name = "cc")
    private double currentCost;

    @ColumnInfo(name = "pc")
    private double previousCost;

    @ColumnInfo(name = "oc")
    private double openCost;

    @ColumnInfo(name = "lc")
    private double lowDailyCost;

    @ColumnInfo(name = "hc")
    private double highDailyCost;

    public Stock(int id, String ticker, String name, String currency, String logo) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.currency = currency;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void getTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
