package com.kozlovskiy.mostocks.ui.stocks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.entities.StockCost;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.ui.stockInfo.StockInfoActivity;
import com.kozlovskiy.mostocks.utils.StockCostUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.ViewHolder> {

    public static final String TAG = StocksAdapter.class.getSimpleName();
    public static final String KEY_TICKER = "TICKER";
    private List<Stock> stocks;
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
        StocksDao stocksDao = ((AppDelegate) context.getApplicationContext()).getDatabase().getDao();
        StockCost stockCost = stocksDao.getStockCost(stock.getTicker());

        holder.symbolView.setText(stock.getTicker());
        holder.companyView.setText(stock.getName());

        if (stock.getLogo() != null && !stock.getLogo().isEmpty()) {
            Picasso.get().load(stock.getLogo())
                    .placeholder(R.drawable.icon)
                    .error(R.drawable.icon)
                    .into(holder.imageView);

        } else holder.imageView.setImageDrawable(
                ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.icon,
                        null
                )
        );

        if (stockCost != null) {
            String text = StockCostUtils.convertCost(stockCost.getCurrentCost());
            int color = context.getResources().getColor(R.color.textColor);
            holder.costView.setText(text);

            if (stockCost.getCurrentCost() > 0) {
                color = context.getResources().getColor(R.color.positiveCost);
                text = "+" + text;
            } else if (stockCost.getCurrentCost() < 0)
                color = context.getResources().getColor(R.color.negativeCost);

            holder.changeView.setText(text);
            holder.changeView.setTextColor(color);
        }

        if (position % 2 == 0) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardColor));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockInfoActivity.class);
            intent.putExtra(KEY_TICKER, stock.getTicker());
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
        final ImageView imageView;

        ViewHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.card);
            symbolView = view.findViewById(R.id.tv_symbol);
            companyView = view.findViewById(R.id.tv_company);
            costView = view.findViewById(R.id.tv_cost);
            changeView = view.findViewById(R.id.tv_change);
            imageView = view.findViewById(R.id.image);
        }
    }

    public void setFilteredStocks(List<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }
}
