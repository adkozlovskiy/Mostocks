package com.kozlovskiy.mostocks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class TechAnalysisResponse {

    @SerializedName("technicalAnalysis")
    private TechnicalAnalysis technicalAnalysis;

    public TechnicalAnalysis getTechnicalAnalysis() {
        return technicalAnalysis;
    }

    public void setTechnicalAnalysis(TechnicalAnalysis technicalAnalysis) {
        this.technicalAnalysis = technicalAnalysis;
    }

    public class TechnicalAnalysis {

        private int id;
        private String ticker;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
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
