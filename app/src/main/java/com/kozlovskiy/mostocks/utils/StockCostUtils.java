package com.kozlovskiy.mostocks.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class StockCostUtils {

    public static String convertCost(double cost) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        return formatter.format(cost);
    }
}
