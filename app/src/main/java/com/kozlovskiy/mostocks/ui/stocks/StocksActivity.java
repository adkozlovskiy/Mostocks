package com.kozlovskiy.mostocks.ui.stocks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import java.util.List;

public class StocksActivity extends AppCompatActivity {

    public static final String TAG = StocksActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tvFavorites;
    private TextView tvStocks;

    View.OnClickListener onMenuItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        recyclerView = findViewById(R.id.recycler);
        tvStocks = findViewById(R.id.tv_stocks);
        tvFavorites = findViewById(R.id.tv_favorites);
        tvStocks.setOnClickListener(onMenuItemClickListener);
        tvFavorites.setOnClickListener(onMenuItemClickListener);

        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::initializeStocks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeStocks();
    }

    private void initializeStocks() {
        StocksRepository stocksRepository = new StocksRepository(this);
        List<Stock> stocks = stocksRepository.getStocks().getValue();

        if (stocks == null || stocks.isEmpty()) {
            // TODO: Если список пустой.
            Log.e(TAG, "initializeStocks: ", new NullPointerException());

        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new StocksAdapter(this, stocks));
            swipeRefreshLayout.setRefreshing(false);

        }
    }
}