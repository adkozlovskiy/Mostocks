package com.kozlovskiy.mostocks.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.kozlovskiy.mostocks.entities.Cost;
import com.kozlovskiy.mostocks.entities.Favorite;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.ui.stockInfo.StockInfoActivity;
import com.kozlovskiy.mostocks.utils.StockCostUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.ViewHolder> {

    public static final String TAG = StocksAdapter.class.getSimpleName();
    public static final String KEY_TICKER = "TICKER";
    private final Context context;
    private final StocksDao stocksDao;
    private List<Stock> stocks;
    private boolean isFavorite;

    public StocksAdapter(Context context, List<Stock> stocks) {
        this.context = context;
        this.stocks = stocks;
        this.stocksDao = ((AppDelegate) context
                .getApplicationContext())
                .getDatabase()
                .getDao();
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
        Stock stock = stocks.get(position);
        Cost stockCost = stocksDao.getCost(stock.getTicker());

        isFavorite = isFavorite(stock);

        holder.symbolView.setText(stock.getTicker());

        if (stock.getName() != null && !stock.getName().isEmpty()) {
            String nameCropped = stock.getName().length() > 21 ? stock.getName().substring(0, 19).trim() + "\u2026" : stock.getName();
            holder.companyView.setText(nameCropped);
        }

        holder.ivStar.setImageResource(isFavorite
                ? R.drawable.ic_star_gold
                : R.drawable.ic_star);

        if (stock.getLogo() != null && !stock.getLogo().isEmpty()) {
            Picasso.get().load(stock.getLogo())
                    .placeholder(R.drawable.white)
                    .error(R.drawable.no_image)
                    .into(holder.ivLogo);

        } else holder.ivLogo.setImageDrawable(
                ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.no_image,
                        null
                )
        );

        if (position % 2 == 0) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardColor));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
        }

        if (stockCost != null) {
            String costString = StockCostUtils.convertCost(stockCost.getCurrentCost());
            int color = context.getResources().getColor(R.color.textColor);
            holder.costView.setText(costString);

            String changeString = StockCostUtils.convertCost(stockCost.getCurrentCost() - stockCost.getPreviousCost());
            String percentString = StockCostUtils.convertPercents(stockCost.getCurrentCost() / stockCost.getPreviousCost());

            if (stockCost.getCurrentCost() - stockCost.getPreviousCost() > 0) {
                color = context.getResources().getColor(R.color.positiveCost);
                changeString = "+" + changeString;
            } else if (stockCost.getCurrentCost() - stockCost.getPreviousCost() < 0) {
                color = context.getResources().getColor(R.color.negativeCost);
                percentString = StockCostUtils.convertPercents(stockCost.getPreviousCost() / stockCost.getCurrentCost());
            }

            changeString += " (" + percentString + "%)";
            holder.changeView.setText(changeString);
            holder.changeView.setTextColor(color);
        }


        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockInfoActivity.class);
            intent.putExtra(KEY_TICKER, stock.getTicker());
            context.startActivity(intent);
        });

        holder.ivStar.setOnClickListener(v -> {

            Log.d(TAG, "isFavorite: clicked");
            if (isFavorite) {
                Log.d(TAG, "isFavorite: добавляем" + stock.getTicker());
                stocksDao.addFavorite(new Favorite(stock.getTicker()));
            } else {
                Log.d(TAG, "isFavorite: убираем");
                stocksDao.removeFavorite(new Favorite(stock.getTicker()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;
        final TextView symbolView, companyView, costView, changeView;
        final ImageView ivLogo, ivStar;

        ViewHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.card);
            symbolView = view.findViewById(R.id.tv_symbol);
            companyView = view.findViewById(R.id.tv_company);
            costView = view.findViewById(R.id.tv_cost);
            changeView = view.findViewById(R.id.tv_change);
            ivLogo = view.findViewById(R.id.iv_logo);
            ivStar = view.findViewById(R.id.iv_star);
        }

    }

    public void updateStocks(List<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    private boolean isFavorite(Stock stock) {
        Favorite favorite = stocksDao.getFavorite(stock.getTicker());
        if (favorite == null) {
            Log.d(TAG, "isFavorite: no" + stock.getTicker());
        } else {
            Log.d(TAG, "isFavorite: yes" + stock.getTicker());
        }
        return favorite != null;
    }
}