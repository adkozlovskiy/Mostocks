package com.kozlovskiy.mostocks.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Table contains favorite tickers.
 */
@Entity
public class Favorite {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ticker")
    private final String ticker;

    public Favorite(@NonNull String ticker) {
        this.ticker = ticker;
    }

    @NonNull
    public String getTicker() {
        return ticker;
    }
}
