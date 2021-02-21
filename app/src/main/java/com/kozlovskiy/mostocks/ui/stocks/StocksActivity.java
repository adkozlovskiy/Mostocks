package com.kozlovskiy.mostocks.ui.stocks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.StockProfile;
import com.kozlovskiy.mostocks.entities.Ticker;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StocksActivity extends AppCompatActivity {

    public static final String TAG = StocksActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
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

        recyclerView = findViewById(R.id.recycler);
        tvStocks = findViewById(R.id.tv_stocks);
        tvFavorites = findViewById(R.id.tv_favorites);
        tvStocks.setOnClickListener(onMenuItemClickListener);
        tvFavorites.setOnClickListener(onMenuItemClickListener);

        swipeRefreshLayout = findViewById(R.id.refresh_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeStocks();
    }

    private void initializeStocks() {
        StocksRepository stocksRepository = new StocksRepository(this);
        List<Ticker> tickers = stocksRepository.getActualTickers().getValue();
        List<StockProfile> stockProfiles = new ArrayList<>();

        if (tickers == null || tickers.isEmpty()) {
            Log.e(TAG, "initializeStocks: ", new NullPointerException());

        } else {

            stocksRepository.updateProfilesFromServer(tickers)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<StockProfile>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            // TODO: 21.02.2021 refreshing true.
                            Log.d(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(@NonNull StockProfile stockProfile) {
                            stockProfiles.add(stockProfile);

                            if (stockProfiles.size() == 30) {
                                onComplete();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            // TODO: 21.02.2021 refreshing false.
                            StocksDao stocksDao = ((AppDelegate) getApplicationContext()).getDatabase().getDao();
                            stocksDao.cacheStockProfiles(stockProfiles);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StocksActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(new StocksAdapter(StocksActivity.this, tickers, stockProfiles));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });


        }
    }

}