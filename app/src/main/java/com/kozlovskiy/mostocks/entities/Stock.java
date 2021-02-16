package com.kozlovskiy.mostocks.entities;

import com.kozlovskiy.mostocks.Currency;

public class Stock {

    private int id;
    private String symbol;
    private String company;
    private Currency currency;
    private String src;
    private double cost;
    private double change;

    public Stock(int id, String symbol, String company, Currency currency, String src, double cost, double change) {
        this.id = id;
        this.symbol = symbol;
        this.company = company;
        this.currency = currency;
        this.src = src;
        this.cost = cost;
        this.change = change;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
