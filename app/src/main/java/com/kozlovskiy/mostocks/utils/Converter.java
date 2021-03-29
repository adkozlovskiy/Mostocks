package com.kozlovskiy.mostocks.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Converter {

    public static String toCurrencyFormat(double quote, int mid, int mad) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMinimumFractionDigits(mid);
        formatter.setMaximumFractionDigits(mad);

        return formatter.format(quote);
    }

    public static String toDefaultFormat(double quote, int mid, int mad) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(mid);
        formatter.setMaximumFractionDigits(mad);

        return formatter.format(quote);
    }

    public static String toPercentFormat(double quote, int mid, int mad) {
        return toDefaultFormat(quote, mid, mad) + "%";
    }

    public static String toBigCurrencyFormat(double quote, int mid, int mad) {
        if (quote >= 1000) {
            return toCurrencyFormat(quote / 1000, mid, mad) + "B";
        }

        return toCurrencyFormat(quote, mid, mad) + "M";
    }
}
