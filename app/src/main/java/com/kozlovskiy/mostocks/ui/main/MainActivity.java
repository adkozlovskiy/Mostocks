package com.kozlovskiy.mostocks.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.services.WebSocketService;
import com.kozlovskiy.mostocks.ui.main.fragments.favorites.FavoritesFragment;
import com.kozlovskiy.mostocks.ui.main.fragments.stocks.StocksFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.kozlovskiy.mostocks.ui.splash.SplashActivity.KEY_STOCKS_INTENT;

public class MainActivity extends AppCompatActivity implements TextWatcher, TabLayout.OnTabSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private EditText searchEditText;
    private StocksFragment stocksFragment;
    private List<Stock> stocks;
    private FavoritesFragment favoritesFragment;
    private int selectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.et_search);
        TabLayout navigationTabs = findViewById(R.id.tabs);
        navigationTabs.addOnTabSelectedListener(this);

        String json = getIntent().getStringExtra(KEY_STOCKS_INTENT);
        Bundle bundles = new Bundle();

        Gson gson = new Gson();

        Type type = new TypeToken<List<Stock>>() {
        }.getType();

        stocks = gson.fromJson(json, type);
        bundles.putString(KEY_STOCKS_INTENT, json);

        stocksFragment = new StocksFragment();
        favoritesFragment = new FavoritesFragment();

        stocksFragment.setArguments(bundles);
        favoritesFragment.setArguments(bundles);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fr_container, stocksFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchEditText.setText("");
        searchEditText.clearFocus();
        searchEditText.addTextChangedListener(this);
        ArrayList<String> symbols = new ArrayList<>();
        for (Stock stock : stocks) {
            symbols.add(stock.getSymbol());
        }

        Intent intent = new Intent(this, WebSocketService.class);
        intent.putStringArrayListExtra("symbols", symbols);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (selectedTab == 0) {
            stocksFragment.filter(s.toString());
        } else {
            favoritesFragment.filter(s.toString());
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        selectedTab = tab.getPosition();

        if (selectedTab == 1) {
            transaction.replace(R.id.fr_container, favoritesFragment);
        } else {
            transaction.replace(R.id.fr_container, stocksFragment);
        }

        transaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}