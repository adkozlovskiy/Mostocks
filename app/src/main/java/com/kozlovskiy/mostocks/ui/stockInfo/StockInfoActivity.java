package com.kozlovskiy.mostocks.ui.stockInfo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart.ChartFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts.ForecastsFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.news.NewsFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.summary.SummaryFragment;
import com.kozlovskiy.mostocks.ui.stocks.adapter.StocksAdapter;

public class StockInfoActivity extends AppCompatActivity
        implements StockInfoView, TabLayout.OnTabSelectedListener {

    private StockInfoPresenter stockInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        stockInfoPresenter = new StockInfoPresenter(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(StocksAdapter.KEY_TICKER));
        toolbar.setNavigationIcon(R.drawable.back_button);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fr_container, ChartFragment.class, null)
                    .commit();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (tab.getPosition()) {
            case 0:
                transaction.replace(R.id.fr_container, ChartFragment.class, null);
                break;

            case 1:
                transaction.replace(R.id.fr_container, SummaryFragment.class, null);
                break;

            case 2:
                transaction.replace(R.id.fr_container, NewsFragment.class, null);
                break;

            case 3:
                transaction.replace(R.id.fr_container, ForecastsFragment.class, null);
                break;
        }

        transaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stockInfoPresenter.unsubscribe();
    }
}