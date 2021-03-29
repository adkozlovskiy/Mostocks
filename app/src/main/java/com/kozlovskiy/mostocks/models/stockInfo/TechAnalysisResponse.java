package com.kozlovskiy.mostocks.models.stockInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class TechAnalysisResponse {

    @SerializedName("technicalAnalysis")
    private TechAnalysis techAnalysis;

    public TechAnalysis getTechAnalysis() {
        return techAnalysis;
    }

    public void setTechAnalysis(TechAnalysis techAnalysis) {
        this.techAnalysis = techAnalysis;
    }

    public static class TechAnalysis {

        private int id;

        private String symbol;

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

        @SerializedName("count")
        @Expose
        private HashMap<String, String> count = new HashMap<>();

        @SerializedName("signal")
        @Expose
        private String signal;

        public HashMap<String, String> getCount() {
            return count;
        }

        public void setCount(HashMap<String, String> count) {
            this.count = count;
        }

        public String getSignal() {
            return signal;
        }

        public void setSignal(String signal) {
            this.signal = signal;
        }
    }
}
