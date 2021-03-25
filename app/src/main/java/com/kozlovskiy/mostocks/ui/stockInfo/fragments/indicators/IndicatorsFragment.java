package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;

public class IndicatorsFragment extends Fragment implements IndicatorsView {

    private IndicatorsPresenter indicatorsPresenter;
    private Context context;

    public IndicatorsFragment() {
        super(R.layout.fragment_indicators);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        indicatorsPresenter = new IndicatorsPresenter(this, context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
