package com.kozlovskiy.mostocks.ui.stocks;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.ui.stocks.adapter.StocksAdapter;

import java.util.List;

public class StocksActivity extends AppCompatActivity
        implements StocksView, TextWatcher, TabLayout.OnTabSelectedListener {

    public static final String TAG = StocksActivity.class.getSimpleName();

    private StocksPresenter stocksPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private StocksAdapter stocksAdapter;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler);

        TabLayout navigationTabs = findViewById(R.id.tabs);
        navigationTabs.addOnTabSelectedListener(this);

        EditText searchEditText = findViewById(R.id.et_search);
        searchEditText.addTextChangedListener(this);

        stocksPresenter = new StocksPresenter(this, this);
        stocksPresenter.initializeStocks();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showStocks(List<Stock> stocks) {
        llm = new LinearLayoutManager(StocksActivity.this);
        stocksAdapter = new StocksAdapter(StocksActivity.this, stocks);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(stocksAdapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRetryDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void setFilteredStocks(List<Stock> stocks) {
        if (stocksAdapter == null) {
            stocksAdapter = new StocksAdapter(this, stocks);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(stocksAdapter);
        }

        stocksAdapter.setFilteredStocks(stocks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stocksPresenter.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        stocksPresenter.filterStocks(s.toString());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 1) {
            stocksPresenter.filterFavorites();
        } else {
            stocksPresenter.removeFilter();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}