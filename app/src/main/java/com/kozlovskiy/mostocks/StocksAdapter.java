package com.kozlovskiy.mostocks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.entities.Stock;

import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.ViewHolder> {

    private final List<Stock> stocks;
    private final Context context;

    public StocksAdapter(Context context, List<Stock> stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock, parent, false);
        return new StocksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stock stock = stocks.get(holder.getAdapterPosition());

        holder.symbolView.setText(stock.getSymbol());
        holder.companyView.setText(stock.getCompany());

        int color = context.getResources().getColor(R.color.textColor);
        String text = Utils.convertCost(stock.getChange());
        holder.costView.setText(text);

        if (stock.getId() % 2 == 1)
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardColor));

        if (stock.getChange() > 0) {
            color = context.getResources().getColor(R.color.positiveCost);
            text = "+" + text;
        } else if (stock.getChange() < 0)
            color = context.getResources().getColor(R.color.negativeCost);

        holder.changeView.setText(text);
        holder.changeView.setTextColor(color);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockInfoActivity.class);
            intent.putExtra("id", stock.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;
        final TextView symbolView, companyView, costView, changeView;

        ViewHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.card);
            symbolView = view.findViewById(R.id.tv_symbol);
            companyView = view.findViewById(R.id.tv_company);
            costView = view.findViewById(R.id.tv_cost);
            changeView = view.findViewById(R.id.tv_change);
        }
    }
}
