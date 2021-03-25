package com.kozlovskiy.mostocks.models.stock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName("o")
    @Expose
    private double open;

    @SerializedName("h")
    @Expose
    private double high;

    @SerializedName("l")
    @Expose
    private double low;

    @SerializedName("c")
    @Expose
    private double current;

    @SerializedName("pc")
    @Expose
    private double previous;

    @SerializedName("t")
    @Expose
    private long time;

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getPrevious() {
        return previous;
    }

    public void setPrevious(double previous) {
        this.previous = previous;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
