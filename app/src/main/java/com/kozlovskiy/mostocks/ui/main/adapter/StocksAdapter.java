package com.kozlovskiy.mostocks.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stock.Favorite;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.room.RoomDelegate;
import com.kozlovskiy.mostocks.ui.stockInfo.StockInfoActivity;
import com.kozlovskiy.mostocks.utils.BitmapUtil;
import com.kozlovskiy.mostocks.utils.Converter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.ViewHolder> {

    public static final String MAIN_IMAGE_URL = "https://eodhistoricaldata.com/img/logos/US/";
    public static final String TAG = StocksAdapter.class.getSimpleName();
    public static final String KEY_CURRENT_COST = "CURRENT_COST";
    public static final String KEY_PREVIOUS_COST = "PREVIOUS_COST";
    public static final String KEY_IS_FAVORITE = "IS_FAVORITE";
    public static final String KEY_SYMBOL = "SYMBOL";
    public static final String KEY_NAME = "NAME";

    private final boolean isFavoriteRecycler;

    private final Context context;
    private final RoomDelegate roomDelegate;
    private final ItemsCountListener itemsCountListener;

    private List<Stock> stocks;

    public StocksAdapter(Context context, boolean isFavoriteRecycler, ItemsCountListener itemsCountListener) {
        this.context = context;
        this.isFavoriteRecycler = isFavoriteRecycler;
        this.itemsCountListener = itemsCountListener;
        this.roomDelegate = new RoomDelegate(context);
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
        holder.symbolView.setText(stock.getSymbol());

        if (stock.getName() != null && !stock.getName().isEmpty()) {
            String nameCropped = stock.getName().length() > 21
                    ? stock.getName().substring(0, 19).trim() + "\u2026"
                    : stock.getName();
            holder.companyView.setText(nameCropped);
        }

        stock.setFavorite(isFavorite(stock));
        holder.ivStar.setImageResource(stock.isFavorite()
                ? R.drawable.ic_star_gold
                : R.drawable.ic_star_gray);

        Bitmap bitmap = BitmapUtil.getCompanyLogo(context,
                R.drawable.bg, stock.getSymbol().substring(0, 1));

        Drawable placeholder = new BitmapDrawable(context.getResources(), bitmap);

        Picasso.get()
                .load(MAIN_IMAGE_URL + stock.getSymbol() + ".png")
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .placeholder(placeholder)
                .into(holder.ivLogo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(MAIN_IMAGE_URL + stock.getSymbol() + ".png")
                                .fit()
                                .placeholder(placeholder)
                                .into(holder.ivLogo);
                    }
                });

        holder.cardView.setCardBackgroundColor(position % 2 == 0
                ? context.getResources().getColor(R.color.cardColor, context.getTheme())
                : context.getResources().getColor(R.color.backgroundColor, context.getTheme()));

        String quote = Converter.toCurrencyFormat(stock.getCurrent(), 2, 2);
        holder.costView.setText(quote);

        int color = context.getResources().getColor(R.color.textColor, context.getTheme());
        double difference = stock.getCurrent() - stock.getPrevious();
        String changeString = Converter.toCurrencyFormat(difference, 2, 2);
        String percentString = Converter.toDefaultFormat(difference / stock.getPrevious() * 100, 2, 2);

        if (difference > 0) {
            color = context.getResources().getColor(R.color.positiveCost, context.getTheme());
            changeString = "+" + changeString;

        } else if (difference < 0) {
            color = context.getResources().getColor(R.color.negativeCost, context.getTheme());
            percentString = Converter.toDefaultFormat(difference / stock.getPrevious() * -100, 2, 2);

        }

        changeString += " (" + percentString + "%)";

        holder.changeView.setText(changeString);
        holder.changeView.setTextColor(color);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockInfoActivity.class);
            intent.putExtra(KEY_SYMBOL, stock.getSymbol());
            intent.putExtra(KEY_NAME, stock.getName());
            intent.putExtra(KEY_IS_FAVORITE, stock.isFavorite());
            intent.putExtra(KEY_CURRENT_COST, stock.getCurrent());
            intent.putExtra(KEY_PREVIOUS_COST, stock.getPrevious());
            context.startActivity(intent);
        });

        holder.ivStar.setOnClickListener(v -> {
            if (!stock.isFavorite()) {
                roomDelegate.addFavorite(new Favorite(stock.getSymbol()));

                holder.ivStar.setImageResource(R.drawable.ic_star_gold);

            } else {
                roomDelegate.removeFavorite(new Favorite(stock.getSymbol()));

                if (isFavoriteRecycler) {
                    stocks.remove(stock);
                    notifyItemRemoved(holder.getAdapterPosition());

                    if (getItemCount() == 0) {
                        itemsCountListener.onZeroItems();
                    }
                }
                holder.ivStar.setImageResource(R.drawable.ic_star_gray);

            }

            stock.setFavorite(!stock.isFavorite());
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
        Favorite favorite = roomDelegate.getFavorite(stock.getSymbol());
        return favorite != null;
    }

    public interface ItemsCountListener {
        void onZeroItems();
    }
}
