package com.kozlovskiy.mostocks.models.chart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Candles {

    @SerializedName("c")
    @Expose
    private List<Double> closePrices = null;

    @SerializedName("h")
    @Expose
    private List<Double> highPrices = null;

    @SerializedName("l")
    @Expose
    private List<Double> lowPrices = null;

    @SerializedName("o")
    @Expose
    private List<Double> openPrices = null;

    @SerializedName("s")
    @Expose
    private String status;

    @SerializedName("t")
    @Expose
    private List<Long> timestamps = null;

    @SerializedName("v")
    @Expose
    private List<Long> volumes = null;

    public List<Double> getClosePrices() {
        return closePrices;
    }

    public void setClosePrices(List<Double> closePrices) {
        this.closePrices = closePrices;
    }

    public List<Double> getHighPrices() {
        return highPrices;
    }

    public void setHighPrices(List<Double> highPrices) {
        this.highPrices = highPrices;
    }

    public List<Double> getLowPrices() {
        return lowPrices;
    }

    public void setLowPrices(List<Double> lowPrices) {
        this.lowPrices = lowPrices;
    }

    public List<Double> getOpenPrices() {
        return openPrices;
    }

    public void setOpenPrices(List<Double> openPrices) {
        this.openPrices = openPrices;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }

    public List<Long> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Long> volumes) {
        this.volumes = volumes;
    }

}