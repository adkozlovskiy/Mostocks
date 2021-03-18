package com.kozlovskiy.mostocks.ui.main.fragments.stocks;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.util.List;

public class StocksFragment extends Fragment
        implements StocksView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    public static final String TAG = StocksFragment.class.getSimpleName();
    private StocksPresenter stocksPresenter;
    private StocksAdapter stocksAdapter;
    private LinearLayoutManager llm;

    public StocksFragment() {
        super(R.layout.fragment_stocks);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stocksPresenter = new StocksPresenter(this, getContext());
        llm = new LinearLayoutManager(getContext());
        stocksAdapter = new StocksAdapter(getContext(), false, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(stocksAdapter);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = llm.getChildCount();
                int totalItemCount = llm.getItemCount();
                int firstVisibleItems = llm.findFirstVisibleItemPosition();

                if (!stocksPresenter.isLoading()) {
                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                        stocksPresenter.setLoading(true);
                        /* if (loadingListener != null) {
                            loadingListener.loadMoreItems(totalItemCount);
                        } */
                        Log.d(TAG, "onScrolled: ");
                    }
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        stocksPresenter.initializeStocks();
    }

    @Override
    public void showRetryDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void updateStocks(List<Stock> stocks) {
        stocksAdapter.updateStocks(stocks);

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stocksPresenter.unsubscribe();
    }
}