package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stockInfo.Indicator;

import java.util.List;

public class IndicatorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<Object> indicators;

    public IndicatorsAdapter(Context context, List<Object> indicators) {
        this.context = context;
        this.indicators = indicators;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 0) {
            View view = inflater.inflate(R.layout.indicator, parent, false);
            return new IndicatorViewHolder(view);

        } else {
            View view = inflater.inflate(R.layout.indicator_title, parent, false);
            return new IndicatorsAdapter.DividerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            final Indicator indicator = (Indicator) indicators.get(position);

            ((IndicatorViewHolder) holder).tvTitle.setText(indicator.getTitle());
            ((IndicatorViewHolder) holder).tvDescription.setText(indicator.getDescription());
            ((IndicatorViewHolder) holder).tvParam.setText(indicator.getParam());

        } else {
            final String dividerTitle = (String) indicators.get(position);
            ((DividerViewHolder) holder).tvTitle.setText(dividerTitle);

        }
    }

    @Override
    public int getItemCount() {
        return indicators.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (indicators.get(position) instanceof Indicator)
            return 0;

        return 1;
    }

    public static class IndicatorViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvParam;

        IndicatorViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tv_title);
            tvDescription = view.findViewById(R.id.tv_description);
            tvParam = view.findViewById(R.id.tv_param);
        }
    }

    public static class DividerViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        DividerViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tv_title);
        }
    }
}
