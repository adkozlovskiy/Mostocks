package com.kozlovskiy.mostocks.models.stock;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favorite {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "symbol")
    private final String symbol;

    public Favorite(@NonNull String symbol) {
        this.symbol = symbol;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }
}
