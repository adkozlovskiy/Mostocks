package com.kozlovskiy.mostocks.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.ui.main.fragments.favorites.FavoritesFragment;
import com.kozlovskiy.mostocks.ui.main.fragments.stocks.StocksFragment;

import static com.kozlovskiy.mostocks.ui.splash.SplashActivity.KEY_STOCKS_INTENT;

public class MainActivity extends AppCompatActivity implements TextWatcher, TabLayout.OnTabSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private EditText searchEditText;
    private Bundle bundles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.et_search);
        TabLayout navigationTabs = findViewById(R.id.tabs);
        navigationTabs.addOnTabSelectedListener(this);

        StocksDao stocksDao = ((AppDelegate) getApplicationContext())
                .getDatabase()
                .getDao();

        String json = getIntent().getStringExtra(KEY_STOCKS_INTENT);
        bundles = new Bundle();

        if (json.isEmpty()) {
            Gson gson = new Gson();
            bundles.putString(KEY_STOCKS_INTENT, gson.toJson(stocksDao.getStocks()));

        } else {
            bundles.putString(KEY_STOCKS_INTENT, json);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fr_container, StocksFragment.class, bundles)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchEditText.addTextChangedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tab.getPosition() == 1) {
            transaction.replace(R.id.fr_container, FavoritesFragment.class, bundles);
        } else {
            transaction.replace(R.id.fr_container, StocksFragment.class, bundles);
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