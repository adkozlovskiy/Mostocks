package com.kozlovskiy.mostocks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kozlovskiy.mostocks.entities.Stock;

import java.util.ArrayList;

public class StocksActivity extends AppCompatActivity {

    private TextView tvFavorites;
    private TextView tvStocks;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        tvStocks = findViewById(R.id.tv_stocks);
        tvFavorites = findViewById(R.id.tv_favorites);
        tvStocks.setOnClickListener(onMenuItemClickListener);
        tvFavorites.setOnClickListener(onMenuItemClickListener);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));

        ArrayList<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock(1, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(2, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(3, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(4, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(5, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(6, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(7, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(8, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(9, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(10, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(11, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        StocksAdapter adapter = new StocksAdapter(this, stocks);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    View.OnClickListener onMenuItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tvStocks.setTextSize(v.getId() == R.id.tv_stocks ? 28 : 16);
            tvStocks.setTextColor(v.getId() == R.id.tv_stocks ? getResources().getColor(R.color.textAccentColor) : getResources().getColor(R.color.textColor));
            tvFavorites.setTextSize(v.getId() == R.id.tv_favorites ? 28 : 16);
            tvFavorites.setTextColor(v.getId() == R.id.tv_favorites ? getResources().getColor(R.color.textAccentColor) : getResources().getColor(R.color.textColor));

            /*
            if (v.getId() == R.id.tv_stocks) {
                tvStocks.setTextSize(28);
                tvFavorites.setTextSize(16);
                tvStocks.setTextColor(getResources().getColor(R.color.textAccentColor));
                tvFavorites.setTextColor(getResources().getColor(R.color.textColor));
            } else if (v.getId() == R.id.tv_favorites) {
                tvFavorites.setTextSize(28);
                tvStocks.setTextSize(16);
                tvFavorites.setTextColor(getResources().getColor(R.color.textAccentColor));
                tvStocks.setTextColor(getResources().getColor(R.color.textColor));
            }*/
        }
    };
}