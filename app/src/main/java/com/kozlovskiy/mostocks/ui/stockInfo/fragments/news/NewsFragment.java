package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.news.adapter.NewsAdapter;
import com.kozlovskiy.mostocks.utils.BitmapUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.MAIN_IMAGE_URL;

public class NewsFragment extends Fragment implements NewsView {

    private final List<News> newsList = new ArrayList<>();
    private NewsPresenter newsPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView ivLogo;
    private TextView tvName;
    private TextView tvIpo;
    private TextView tvCapitalization;
    private Context context;

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
        ivLogo.setImageBitmap(BitmapUtil.markSymbolOnBitmap(getContext(), R.drawable.blue_background, ticker.substring(0, 1)));
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
        Picasso.get()
                .load(MAIN_IMAGE_URL + stock.getSymbol() + ".png")
                .networkPolicy(NetworkPolicy.OFFLINE)

                .into(ivLogo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(MAIN_IMAGE_URL + stock.getSymbol() + ".png")
                                .into(ivLogo, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        if (ivLogo != null) {
                                            ivLogo.setImageBitmap(BitmapUtil.markSymbolOnBitmap(context, R.drawable.blue_background, stock.getSymbol().substring(0, 1)));
                                        }
                                    }
                                });
                    }
                });


        tvName.setText(stock.getName());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private String getTicker() {
        return getArguments().getString(StocksAdapter.KEY_SYMBOL);
    }
}