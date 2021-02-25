package com.kozlovskiy.mostocks.ui.stocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.ui.stocks.adapter.StocksAdapter;
import com.kozlovskiy.mostocks.utils.SettingsUtils;

import java.util.List;

public class StocksActivity extends AppCompatActivity
        implements StocksView, TextWatcher {

    public static final String TAG = StocksActivity.class.getSimpleName();

    private StocksPresenter stocksPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private StocksAdapter stocksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler);

        EditText searchEditText = findViewById(R.id.et_search);
        searchEditText.addTextChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        StocksRepository stocksRepository = new StocksRepository(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        stocksPresenter = new StocksPresenter(this, stocksRepository, builder);
        stocksPresenter.initializeStocks();
    }

    @Override
    public void showStocks(List<Stock> stocks) {

        Log.d(TAG, "showStocks: ");
        LinearLayoutManager llm = new LinearLayoutManager(StocksActivity.this);
        stocksAdapter = new StocksAdapter(StocksActivity.this, stocks);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(stocksAdapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        SettingsUtils.updateStocksUptime(this);
    }

    @Override
    public void showRetryDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void setFilteredStocks(List<Stock> stocks) {
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
}