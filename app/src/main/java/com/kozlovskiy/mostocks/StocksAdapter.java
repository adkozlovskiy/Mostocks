package com.kozlovskiy.mostocks;

import android.content.Context;
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
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock, parent, false);
        return new StocksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stock stock = stocks.get(holder.getAdapterPosition());

        holder.symbolView.setText(stock.getSymbol());
        holder.companyView.setText(stock.getCompany());
        holder.costView.setText(Utils.convertCost(stock.getCost(), stock.getCurrency()));

        if (stock.getChange() > 0) {
            holder.changeView.setText("+" + Utils.convertCost(stock.getChange(), stock.getCurrency()));
            holder.changeView.setTextColor(context.getResources().getColor(R.color.positiveCost));
        } else if (stock.getChange() == 0) {
            holder.changeView.setText(Utils.convertCost(stock.getChange(), stock.getCurrency()));
            holder.changeView.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
            holder.changeView.setText(Utils.convertCost(stock.getChange(), stock.getCurrency()));
            holder.changeView.setTextColor(context.getResources().getColor(R.color.negativeCost));
        }

        if (holder.getAdapterPosition() % 2 == 0) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardColor));
        }
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
