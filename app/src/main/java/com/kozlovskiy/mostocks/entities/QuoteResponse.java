package com.kozlovskiy.mostocks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuoteResponse {

    @SerializedName("Global Quote")
    @Expose
    private Quote quote;

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }
}
