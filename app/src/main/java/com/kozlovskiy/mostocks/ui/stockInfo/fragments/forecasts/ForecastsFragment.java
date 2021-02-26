package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;

public class ForecastsFragment extends Fragment implements ForecastsView {

    public ForecastsFragment() {
        super(R.layout.fragment_forecasts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForecastsPresenter forecastsPresenter = new ForecastsPresenter(this);
    }
}