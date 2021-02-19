package com.kozlovskiy.mostocks.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Stock {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "symbol")
    private String symbol;

    @ColumnInfo(name = "company")
    private String company;

    @ColumnInfo(name = "currency")
    private String currency;

    @ColumnInfo(name = "src")
    private String src;

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

    public Stock(int id, String symbol, String company, String currency, String src) {
        this.id = id;
        this.symbol = symbol;
        this.company = company;
        this.currency = currency;
        this.src = src;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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
