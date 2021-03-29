package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import android.app.Dialog;

import com.kozlovskiy.mostocks.models.stockInfo.News;

import java.util.List;

public interface NewsView {

    void updateNews(List<News> newsList);

    void showDialog(Dialog dialog);

}
