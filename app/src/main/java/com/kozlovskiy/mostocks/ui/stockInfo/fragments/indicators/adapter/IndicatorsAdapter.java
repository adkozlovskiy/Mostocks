package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;

public class IndicatorsAdapter extends RecyclerView.Adapter<IndicatorsAdapter.ViewHolder> {

    private final Context context;

    public IndicatorsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public IndicatorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.indicator, parent, false);
        return new IndicatorsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
        }
    }
}
