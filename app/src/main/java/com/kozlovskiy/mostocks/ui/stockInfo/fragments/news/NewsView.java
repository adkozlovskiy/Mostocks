package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Stock;

import java.util.List;

public interface NewsView {

    void updateNews(List<News> newsList);

    void showStockInfo(Stock stock);

}
