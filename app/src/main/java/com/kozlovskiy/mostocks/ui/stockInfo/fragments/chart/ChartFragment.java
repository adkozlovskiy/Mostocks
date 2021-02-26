package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;

public class ChartFragment extends Fragment implements ChartView {

    public ChartFragment() {
        super(R.layout.fragment_chart);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChartPresenter chartPresenter = new ChartPresenter(this);
    }
}