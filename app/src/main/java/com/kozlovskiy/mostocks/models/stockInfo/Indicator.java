package com.kozlovskiy.mostocks.models.stockInfo;

public class Indicator {

    private final String title;
    private final String description;
    private final String param;

    public Indicator(String title, String description, String param) {
        this.title = title;
        this.description = description;
        this.param = param;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getParam() {
        return param;
    }
}
