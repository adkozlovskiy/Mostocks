package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.news.adapter.NewsAdapter;

import java.util.List;

public class NewsFragment extends Fragment
        implements NewsView {

    public static final String TAG = NewsFragment.class.getSimpleName();
    private NewsPresenter newsPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Context context;

    public NewsFragment() {
        super(R.layout.fragment_news);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        newsPresenter = new NewsPresenter(this, context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);

        newsPresenter.initializeNews(getSymbol());
    }

    @Override
    public void updateNews(List<News> newsList) {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        NewsAdapter newsAdapter = new NewsAdapter(newsList, context);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(newsAdapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        newsAdapter.updateNews(newsList);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    private String getSymbol() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_SYMBOL);
    }
}