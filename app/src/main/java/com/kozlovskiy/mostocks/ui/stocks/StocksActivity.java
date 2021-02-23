package com.kozlovskiy.mostocks.ui.stocks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Ticker;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.utils.SettingsUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StocksActivity extends AppCompatActivity {

    public static final String TAG = StocksActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
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

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler);
        tvStocks = findViewById(R.id.tv_stocks);
        tvFavorites = findViewById(R.id.tv_favorites);
        tvStocks.setOnClickListener(onMenuItemClickListener);
        tvFavorites.setOnClickListener(onMenuItemClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeStocks();
    }

    private void initializeStocks() {
        StocksRepository stocksRepository = new StocksRepository(this);
        List<Ticker> tickers = stocksRepository.getActualTickers().getValue();

        if (tickers == null || tickers.isEmpty()) {
            // TODO: 22.02.2021 alertDialog
            Log.e(TAG, "initializeStocks: Error! ", new NullPointerException());

        } else {
            stocksRepository.updateProfilesFromServer(tickers)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(Throwable::printStackTrace) // TODO: 23.02.2021
                    .doOnComplete(() -> onProfilesUpdatingSuccess(tickers))
                    .subscribe();

        }
    }

    private void onProfilesUpdatingSuccess(List<Ticker> tickers) {
        SettingsUtils.setUptime(this, SettingsUtils.KEY_PROFILES_UPTIME);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        Log.d(TAG, "initializeStocks: done!");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StocksActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new StocksAdapter(StocksActivity.this, tickers));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
        finish();
    }
}