package com.kozlovskiy.mostocks.ui.stockInfo;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart.ChartFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts.ForecastsFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators.IndicatorsFragment;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.news.NewsFragment;
import com.kozlovskiy.mostocks.utils.BitmapUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.MAIN_IMAGE_URL;

public class StockInfoActivity extends AppCompatActivity
        implements StockInfoView, TabLayout.OnTabSelectedListener {

    private StockInfoPresenter stockInfoPresenter;
    private ImageView ivLogo;
    private Bundle bundles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        stockInfoPresenter = new StockInfoPresenter(this, this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ivLogo = toolbar.findViewById(R.id.iv_logo);

        ImageButton ibReturn = toolbar.findViewById(R.id.ib_return);
        ibReturn.setOnClickListener(v -> onBackPressed());

        TextView tvSymbol = toolbar.findViewById(R.id.tv_symbol);
        TextView tvName = toolbar.findViewById(R.id.tv_name);

        setSupportActionBar(toolbar);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(this);

        String symbol = getIntent().getStringExtra(StocksAdapter.KEY_SYMBOL);
        String name = getIntent().getStringExtra(StocksAdapter.KEY_NAME);

        if (name != null && !name.isEmpty()) {
            String nameCropped = name.length() > 28
                    ? name.substring(0, 26).trim() + "\u2026"
                    : name;

            tvName.setText(nameCropped);
        }

        initializeLogo(symbol);
        tvSymbol.setText(symbol);


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

    private void initializeLogo(String symbol) {
        Picasso.get()
                .load(MAIN_IMAGE_URL + symbol + ".png")
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(ivLogo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(MAIN_IMAGE_URL + symbol + ".png")
                                .into(ivLogo, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        if (ivLogo != null) {
                                            ivLogo.setImageBitmap(BitmapUtil.getCompanyLogo(StockInfoActivity.this, R.drawable.bg, symbol.substring(0, 1)));
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (tab.getPosition()) {
            case 0:
                transaction.replace(R.id.fr_container, ChartFragment.class, bundles);
                break;

            case 1:
                transaction.replace(R.id.fr_container, IndicatorsFragment.class, bundles);
                break;

            case 2:
                transaction.replace(R.id.fr_container, NewsFragment.class, bundles);
                break;

            case 3:
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