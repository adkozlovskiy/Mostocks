package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators;

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

public class IndicatorsFragment extends Fragment implements IndicatorsView {

    private IndicatorsPresenter indicatorsPresenter;
    private Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

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
        indicatorsPresenter = new IndicatorsPresenter(this, context, getSymbol());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recycler);
        indicatorsPresenter.initializeIndicators();
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

    private String getSymbol() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_SYMBOL);
    }
}
