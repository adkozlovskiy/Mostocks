package com.kozlovskiy.mostocks.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Cost {

    @PrimaryKey
    @ColumnInfo(name = "ticker")
    @SerializedName("ticker")
    @Expose
    @NonNull
    private final String ticker;

    public Cost(@NonNull String ticker) {
        this.ticker = ticker;
    }

    @ColumnInfo(name = "c")
    @SerializedName("c")
    @Expose
    private double currentCost;

    @ColumnInfo(name = "pc")
    @SerializedName("pc")
    @Expose
    private double previousCost;

    @NonNull
    public String getTicker() {
        return ticker;
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
