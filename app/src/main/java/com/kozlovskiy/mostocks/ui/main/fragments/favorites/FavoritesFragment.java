package com.kozlovskiy.mostocks.ui.main.fragments.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.util.List;

public class FavoritesFragment extends Fragment
        implements FavoritesView, StocksAdapter.ItemsCountListener {

    private FavoritesPresenter favoritesPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvNoTicker;
    public static final String TAG = FavoritesFragment.class.getSimpleName();

    public FavoritesFragment() {
        super(R.layout.fragment_stocks);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritesPresenter = new FavoritesPresenter(this, getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        tvNoTicker = view.findViewById(R.id.tv_no_tickers);
        favoritesPresenter.initializeFavorites();
    }

    @Override
    public void updateStocks(List<Stock> stocks) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        StocksAdapter stocksAdapter = new StocksAdapter(getContext(), stocks, true, this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(stocksAdapter);

        progressBar.setVisibility(View.GONE);

        if (stocksAdapter.getItemCount() == 0) {
            tvNoTicker.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }

        stocksAdapter.updateStocks(stocks);
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