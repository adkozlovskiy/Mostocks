package com.kozlovskiy.mostocks.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Stock {

    @PrimaryKey
    @ColumnInfo(name = "ticker")
    @SerializedName("ticker")
    @Expose
    @NonNull
    private final String ticker;

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

    @ColumnInfo(name = "c")
    @SerializedName("c")
    @Expose
    private double currentCost;

    @ColumnInfo(name = "pc")
    @SerializedName("pc")
    @Expose
    private double previousCost;

    public Stock(@NonNull String ticker) {
        this.ticker = ticker;
    }

    @NonNull
    public String getTicker() {
        return ticker;
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
}
