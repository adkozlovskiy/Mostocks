package com.kozlovskiy.mostocks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.entities.Stock;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);

        ArrayList<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock(1, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(3, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));

        linearLayoutManager = new LinearLayoutManager(this);
        StocksAdapter adapter = new StocksAdapter(this, stocks);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}