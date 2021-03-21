package com.kozlovskiy.mostocks.ui.main.fragments.favorites;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.lang.reflect.Type;
import java.util.List;

import static com.kozlovskiy.mostocks.ui.splash.SplashActivity.KEY_STOCKS_INTENT;

public class FavoritesFragment extends Fragment
        implements FavoritesView, StocksAdapter.ItemsCountListener {

    private FavoritesPresenter favoritesPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvNoTicker;
    private LinearLayoutManager linearLayoutManager;
    private StocksAdapter stocksAdapter;
    private Gson gson;
    private Type type;

    public FavoritesFragment() {
        super(R.layout.fragment_stocks);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        favoritesPresenter = new FavoritesPresenter(this, context);

        stocksAdapter = new StocksAdapter(context, true, this);
        linearLayoutManager = new LinearLayoutManager(context);

        gson = new Gson();
        type = new TypeToken<List<Stock>>() {
        }.getType();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        tvNoTicker = view.findViewById(R.id.tv_no_tickers);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(stocksAdapter);

        if (getArguments() != null) {
            String json = getArguments().getString(KEY_STOCKS_INTENT);

            List<Stock> stocks = gson.fromJson(json, type);
            favoritesPresenter.initializeFavorites(stocks);
        }
    }

    @Override
    public void updateStocks(List<Stock> stocks) {
        stocksAdapter.updateStocks(stocks);

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);

            if (stocksAdapter.getItemCount() == 0) {
                tvNoTicker.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoritesPresenter.unsubscribe();
    }

    @Override
    public void onZeroItems() {
        recyclerView.setVisibility(View.GONE);
        tvNoTicker.setVisibility(View.VISIBLE);
    }
}