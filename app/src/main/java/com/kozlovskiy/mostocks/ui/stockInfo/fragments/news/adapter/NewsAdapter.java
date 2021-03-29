package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private final Context context;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.tvHeadline.setText(news.getHeadline());
        holder.tvSummary.setText(news.getSummary());

        if (news.getImage() != null && !news.getImage().isEmpty()) {
            Picasso.get()
                    .load(news.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .fit()
                    .placeholder(R.drawable.white)
                    .into(holder.ivImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(news.getImage())
                                    .fit()
                                    .placeholder(R.drawable.white)
                                    .into(holder.ivImage, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            holder.ivImage.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    });

        } else holder.ivImage.setVisibility(View.GONE);

        Date date = new Date(news.getDatetime() * 1000);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        String dateString = format.format(date);
        holder.tvDatetime.setText(dateString);

        String link = "<a href=\"" + news.getUrl() + "\">Read more...</a>";

        holder.tvLink.setText(Html.fromHtml(link, Html.FROM_HTML_MODE_LEGACY));
        holder.tvLink.setLinksClickable(true);
        holder.tvLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvHeadline, tvSummary, tvLink;
        final ImageView ivImage;
        final TextView tvDatetime;

        ViewHolder(View view) {
            super(view);

            tvHeadline = view.findViewById(R.id.tv_headline);
            tvSummary = view.findViewById(R.id.tv_summary);
            tvLink = view.findViewById(R.id.tv_link);
            ivImage = view.findViewById(R.id.iv_image);
            tvDatetime = view.findViewById(R.id.tv_datetime);

        }
    }

    public void updateNews(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }
}
