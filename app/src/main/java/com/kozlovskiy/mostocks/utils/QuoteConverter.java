package com.kozlovskiy.mostocks.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class QuoteConverter {

    public static String convertToCurrencyFormat(double quote, int minDigits, int maxDigits) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMinimumFractionDigits(minDigits);
        formatter.setMaximumFractionDigits(maxDigits);

        return formatter.format(quote);
    }

    public static String convertToDefaultFormat(double quote, int minDigits, int maxDigits) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(minDigits);
        formatter.setMaximumFractionDigits(maxDigits);

        return formatter.format(quote);
    }
}
