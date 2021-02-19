package com.kozlovskiy.mostocks.utils;

import java.text.NumberFormat;

public class StockCostUtils {

    public static String convertCost(double cost) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        return formatter.format(cost);
    }
}
