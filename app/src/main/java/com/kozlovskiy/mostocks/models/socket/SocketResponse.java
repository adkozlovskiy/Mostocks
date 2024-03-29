package com.kozlovskiy.mostocks.models.socket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocketResponse {

    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    @SerializedName("type")
    @Expose
    private String type;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Data {

        @SerializedName("c")
        @Expose
        private List<String> c = null;

        @SerializedName("p")
        @Expose
        private Double quote;

        @SerializedName("s")
        @Expose
        private String symbol;

        @SerializedName("t")
        @Expose
        private Long timestamp;

        @SerializedName("v")
        @Expose
        private Long volume;

        public List<String> getC() {
            return c;
        }

        public void setC(List<String> c) {
            this.c = c;
        }

        public Double getQuote() {
            return quote;
        }

        public void setQuote(Double quote) {
            this.quote = quote;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }
    }
}
