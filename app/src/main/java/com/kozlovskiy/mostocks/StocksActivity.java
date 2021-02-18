package com.kozlovskiy.mostocks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kozlovskiy.mostocks.api.StocksService;
import com.kozlovskiy.mostocks.entities.Cost;
import com.kozlovskiy.mostocks.entities.Stock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class StocksActivity extends AppCompatActivity {

    private TextView tvFavorites;
    private TextView tvStocks;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final String TAG = StocksActivity.class.getSimpleName();
    public static final String BASE_URL = "https://finnhub.io/api/v1/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        StocksService service = retrofit.create(StocksService.class);
        List<Observable<Cost>> requests = new ArrayList<>();
        requests.add(service.getStockCost("AAPL", "c0l8c7748v6orbr0u010"));

        Observable.zip(requests, new Function<Object[], Object>() {
            @Override
            public Object apply(Object[] objects) throws Exception {
                return new Object();
            }
        })
                .subscribeOn(Schedulers.computation())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {
                        Log.d(TAG, "onNext: " + ((Cost) value).getCurrentCost());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

        RecyclerView recyclerView = findViewById(R.id.recycler);
        tvStocks = findViewById(R.id.tv_stocks);
        tvFavorites = findViewById(R.id.tv_favorites);
        tvStocks.setOnClickListener(onMenuItemClickListener);
        tvFavorites.setOnClickListener(onMenuItemClickListener);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));

        ArrayList<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock(1, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(2, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(3, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(4, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(5, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(6, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(7, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(8, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(9, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));
        stocks.add(new Stock(10, "GOOGL", "Google", Currency.USD, "123321", 41242.541, 412.22));
        stocks.add(new Stock(11, "YNDX", "Yandex, LLC", Currency.USD, "123321", 51512.541, -41.22));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        StocksAdapter adapter = new StocksAdapter(this, stocks);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

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
}