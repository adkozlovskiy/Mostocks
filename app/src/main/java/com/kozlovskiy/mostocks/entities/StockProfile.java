package com.kozlovskiy.mostocks.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class StockProfile {

    @PrimaryKey
    @ColumnInfo(name = "ticker")
    @SerializedName("ticker")
    @Expose
    @NonNull
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

    public StockProfile(@NonNull String ticker, String name, String currency, String logo) {
        this.ticker = ticker;
        this.name = name;
        this.currency = currency;
        this.logo = logo;
    }

    @NonNull
    public String getTicker() {
        return ticker;
    }

    public void setTicker(@NonNull String ticker) {
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
}
