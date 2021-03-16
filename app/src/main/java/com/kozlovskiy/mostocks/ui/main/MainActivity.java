package com.kozlovskiy.mostocks.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.fragments.favorites.FavoritesFragment;
import com.kozlovskiy.mostocks.ui.main.fragments.stocks.StocksFragment;

public class MainActivity extends AppCompatActivity implements TextWatcher, TabLayout.OnTabSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private EditText searchEditText;
    private TabLayout navigationTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEditText = findViewById(R.id.et_search);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fr_container, StocksFragment.class, null)
                    .commit();
        }

        navigationTabs = findViewById(R.id.tabs);
        navigationTabs.addOnTabSelectedListener(this);
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
        if (navigationTabs.getSelectedTabPosition() == 1) {

            // TODO: 05.03.2021

        } else {

            // TODO: 05.03.2021

        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tab.getPosition() == 1) {
            transaction.replace(R.id.fr_container, FavoritesFragment.class, null);
        } else {
            transaction.replace(R.id.fr_container, StocksFragment.class, null);
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