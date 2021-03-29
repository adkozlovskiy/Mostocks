package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;
import com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators.adapter.IndicatorsAdapter;

import java.util.List;

public class IndicatorsFragment extends Fragment
        implements IndicatorsView {

    public static final String TAG = IndicatorsFragment.class.getSimpleName();
    private IndicatorsPresenter indicatorsPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
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
        indicatorsPresenter = new IndicatorsPresenter(this, context, getSymbol());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recycler);
        indicatorsPresenter.initializeIndicators();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showIndicators(List<Object> indicators) {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, indicators);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(indicatorsAdapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    private String getSymbol() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_SYMBOL);
    }
}
