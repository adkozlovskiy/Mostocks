package com.kozlovskiy.mostocks;

import android.app.Application;

import androidx.room.Room;

import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.room.StocksDatabase;

import java.util.ArrayList;
import java.util.List;

public class AppDelegate extends Application {

    private StocksDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                StocksDatabase.class, "database").allowMainThreadQueries().build();

        initializeStocks();
    }

    public StocksDatabase getDatabase() {
        return database;
    }

    public void initializeStocks() {
        List<Stock> stockList = new ArrayList<Stock>() {{
            add(new Stock(1, "YNDX", "Yandex, LLC", "USD", "mW40obc.png"));
            add(new Stock(2, "TSLA", "Tesla Motors", "USD", "kMZFr3O.png"));
            add(new Stock(3, "AAPL", "Apple Inc.", "USD", "zZa7wWl.png"));
            add(new Stock(4, "GOOGL", "Alphabet Inc.", "USD", "NPQqQhb.png"));
            add(new Stock(5, "AMZN", "Amazon.com", "USD", "QlA5z7n.png"));
        }};
        database.getDao().initializeStocks(stockList);
    }
}
