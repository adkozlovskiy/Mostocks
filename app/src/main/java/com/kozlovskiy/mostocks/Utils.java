package com.kozlovskiy.mostocks;

import java.text.NumberFormat;

public class Utils {

    public static String convertCost(double cost) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(cost);
    }
}
