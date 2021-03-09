package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.news.adapter.NewsAdapter;
import com.kozlovskiy.mostocks.utils.StockCostUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements NewsView {

    private final List<News> newsList = new ArrayList<>();
    private NewsPresenter newsPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView ivLogo;
    private TextView tvName;
    private TextView tvIpo;
    private TextView tvCapitalization;

    public NewsFragment() {
        super(R.layout.fragment_news);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsPresenter = new NewsPresenter(this, getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        ivLogo = view.findViewById(R.id.iv_logo);
        tvName = view.findViewById(R.id.tv_name);
        tvIpo = view.findViewById(R.id.tv_ipo);
        tvCapitalization = view.findViewById(R.id.tv_capitalization);

        String ticker = getTicker();
        newsPresenter.initializeStock(ticker);
        newsPresenter.initializeNews(ticker);
    }

    @Override
    public void updateNews(List<News> newsList) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        NewsAdapter newsAdapter = new NewsAdapter(newsList, getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(newsAdapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        newsAdapter.updateNews(newsList);
    }

    @Override
    public void showStockInfo(Stock stock) {
        if (stock.getLogo() != null && !stock.getLogo().isEmpty()) {
            Picasso.get().load(stock.getLogo())
                    .placeholder(R.drawable.white)
                    .error(R.drawable.no_image)
                    .into(ivLogo);

        } else ivLogo.setImageDrawable(
                ResourcesCompat.getDrawable(getContext().getResources(),
                        R.drawable.no_image,
                        null
                )
        );

        tvName.setText(stock.getName());
        tvIpo.setText(stock.getIpo());
        tvCapitalization.setText(StockCostUtils.convertCost(stock.getCapitalization()));
    }

    private String getTicker() {
        return getArguments().getString(StocksAdapter.KEY_TICKER);
    }
}