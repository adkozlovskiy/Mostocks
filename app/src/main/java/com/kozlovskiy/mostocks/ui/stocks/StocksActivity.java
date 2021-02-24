package com.kozlovskiy.mostocks.ui.stocks;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabItem;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Ticker;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.ui.stocks.adapter.StocksAdapter;

import java.util.List;

public class StocksActivity extends AppCompatActivity
        implements StocksView {

    public static final String TAG = StocksActivity.class.getSimpleName();

    private StocksPresenter stocksPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler);
    }

    @Override
    protected void onResume() {
        super.onResume();

        StocksRepository stocksRepository = new StocksRepository(this);
        stocksPresenter = new StocksPresenter(this, stocksRepository);
        stocksPresenter.initializeStocks();
    }

    @Override
    public void showStocks(List<Ticker> tickers) {
        LinearLayoutManager llm = new LinearLayoutManager(StocksActivity.this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new StocksAdapter(StocksActivity.this, tickers));

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAlertDialog(Dialog dialog) {
        // TODO: 23.02.2021
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        stocksPresenter.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
        finish();
    }
}