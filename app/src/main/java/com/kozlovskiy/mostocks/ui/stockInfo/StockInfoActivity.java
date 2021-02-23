package com.kozlovskiy.mostocks.ui.stockInfo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kozlovskiy.mostocks.R;

public class StockInfoActivity extends AppCompatActivity
        implements StockInfoView {

    private StockInfoPresenter stockInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);

        stockInfoPresenter = new StockInfoPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stockInfoPresenter.unsubscribe();
    }
}