package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;

import java.util.ArrayList;

import im.dacer.androidcharts.BarView;

public class ForecastsFragment extends Fragment implements ForecastsView {

    private ForecastsPresenter forecastsPresenter;

    public ForecastsFragment() {
        super(R.layout.fragment_forecasts);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forecastsPresenter = new ForecastsPresenter(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BarView barView = (BarView) view.findViewById(R.id.bar_view);
        ArrayList<String> strList = new ArrayList<String>() {{
            add("S");
            add("N");
            add("B");
        }};

        ArrayList<Integer> dataList = new ArrayList<Integer>() {{
            add(6);
            add(8);
            add(12);
        }};

        barView.setBottomTextList(strList);
        barView.setDataList(dataList, 15);
    }
}