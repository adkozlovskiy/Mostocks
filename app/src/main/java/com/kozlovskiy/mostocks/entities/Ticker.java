package com.kozlovskiy.mostocks.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ticker {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ticker")
    private String ticker;

    public Ticker(@NonNull String ticker) {
        this.ticker = ticker;
    }

    @NonNull
    public String getTicker() {
        return ticker;
    }

    public void setTicker(@NonNull String ticker) {
        this.ticker = ticker;
    }
}
