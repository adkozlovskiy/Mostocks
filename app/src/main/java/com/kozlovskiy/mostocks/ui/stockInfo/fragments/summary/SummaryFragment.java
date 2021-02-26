package com.kozlovskiy.mostocks.ui.stockInfo.fragments.summary;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;

public class SummaryFragment extends Fragment implements SummaryView {

    public SummaryFragment() {
        super(R.layout.fragment_summary);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SummaryPresenter summaryPresenter = new SummaryPresenter(this);

    }
}