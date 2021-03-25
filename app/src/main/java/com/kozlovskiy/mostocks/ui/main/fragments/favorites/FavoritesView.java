package com.kozlovskiy.mostocks.ui.main.fragments.favorites;

import com.kozlovskiy.mostocks.models.stock.Stock;

import java.util.List;

public interface FavoritesView {

    void updateStocks(List<Stock> stocks);
}
