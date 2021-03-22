package com.kozlovskiy.mostocks.ui.stockInfo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart.ChartFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts.ForecastsFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.news.NewsFragment;

public class StockInfoActivity extends AppCompatActivity
        implements StockInfoView, TabLayout.OnTabSelectedListener {

    private StockInfoPresenter stockInfoPresenter;
    private Bundle bundles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        stockInfoPresenter = new StockInfoPresenter(this, this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(StocksAdapter.KEY_SYMBOL));
        toolbar.setNavigationIcon(R.drawable.back_button);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(this);

        String symbol = getIntent().getStringExtra(StocksAdapter.KEY_SYMBOL);
        double currentCost = getIntent().getDoubleExtra(StocksAdapter.KEY_CURRENT_COST, 0);
        double previousCost = getIntent().getDoubleExtra(StocksAdapter.KEY_PREVIOUS_COST, 0);

        bundles = new Bundle();
        bundles.putString(StocksAdapter.KEY_SYMBOL, symbol);
        bundles.putDouble(StocksAdapter.KEY_CURRENT_COST, currentCost);
        bundles.putDouble(StocksAdapter.KEY_PREVIOUS_COST, previousCost);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fr_container, ChartFragment.class, bundles)
                    .commit();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (tab.getPosition()) {
            case 0:
                transaction.replace(R.id.fr_container, ChartFragment.class, bundles);
                break;

            case 1:
                transaction.replace(R.id.fr_container, NewsFragment.class, bundles);
                break;

            case 2:
                transaction.replace(R.id.fr_container, ForecastsFragment.class, bundles);
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