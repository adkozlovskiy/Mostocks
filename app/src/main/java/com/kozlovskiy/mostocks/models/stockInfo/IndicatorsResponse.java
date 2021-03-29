package com.kozlovskiy.mostocks.models.stockInfo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndicatorsResponse {

    @SerializedName("metric")
    @Expose
    private Indicators indicators;

    public Indicators getIndicators() {
        return indicators;
    }

    public void setIndicators(Indicators indicators) {
        this.indicators = indicators;
    }

    @Entity
    public static class Indicators {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        private Integer id;

        @ColumnInfo(name = "symbol")
        private String symbol;

        @SerializedName("marketCapitalization")
        @Expose
        private Double marketCap;

        @SerializedName("psTTM")
        @Expose
        private Double ps;

        @SerializedName("peExclExtraTTM")
        @Expose
        private Double pe;

        @SerializedName("epsBasicExclExtraItemsTTM")
        @Expose
        private Double eps;

        @SerializedName("epsGrowth5Y")
        @Expose
        private Double epsGrowth5Y;

        @SerializedName("revenueGrowth5Y")
        @Expose
        private Double revenueGrowth5Y;

        @SerializedName("roaeTTM")
        @Expose
        private Double roe;

        @SerializedName("roaa5Y")
        @Expose
        private Double roa5Y;

        @SerializedName("roiTTM")
        @Expose
        private Double roi;

        @SerializedName("totalDebt/totalEquityQuarterly")
        @Expose
        private Double debtPerEquity;

        @SerializedName("netProfitMarginTTM")
        @Expose
        private Double netProfitMargin;

        @SerializedName("payoutRatioAnnual")
        @Expose
        private Double payoutRatio;

        @SerializedName("dividendYield5Y")
        @Expose
        private Double dividendYield;

        @SerializedName("dividendYieldIndicatedAnnual")
        @Expose
        private Double dividendAnnual;

        @SerializedName("52WeekHigh")
        @Expose
        private Double weekHigh;

        @SerializedName("52WeekLow")
        @Expose
        private Double weekLow;

        @SerializedName("10DayAverageTradingVolume")
        @Expose
        private Double tenDaysAvgVolume;

        @SerializedName("3MonthAverageTradingVolume")
        @Expose
        private Double threeMonthAvgVolume;

        @SerializedName("beta")
        @Expose
        private Double beta;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Double getMarketCap() {
            return marketCap;
        }

        public void setMarketCap(Double marketCap) {
            this.marketCap = marketCap;
        }

        public Double getPs() {
            return ps;
        }

        public void setPs(Double ps) {
            this.ps = ps;
        }

        public Double getPe() {
            return pe;
        }

        public void setPe(Double pe) {
            this.pe = pe;
        }

        public Double getEps() {
            return eps;
        }

        public void setEps(Double eps) {
            this.eps = eps;
        }

        public Double getEpsGrowth5Y() {
            return epsGrowth5Y;
        }

        public void setEpsGrowth5Y(Double epsGrowth5Y) {
            this.epsGrowth5Y = epsGrowth5Y;
        }

        public Double getRevenueGrowth5Y() {
            return revenueGrowth5Y;
        }

        public void setRevenueGrowth5Y(Double revenueGrowth5Y) {
            this.revenueGrowth5Y = revenueGrowth5Y;
        }

        public Double getRoe() {
            return roe;
        }

        public void setRoe(Double roe) {
            this.roe = roe;
        }

        public Double getRoa5Y() {
            return roa5Y;
        }

        public void setRoa5Y(Double roa5Y) {
            this.roa5Y = roa5Y;
        }

        public Double getRoi() {
            return roi;
        }

        public void setRoi(Double roi) {
            this.roi = roi;
        }

        public Double getDebtPerEquity() {
            return debtPerEquity;
        }

        public void setDebtPerEquity(Double debtPerEquity) {
            this.debtPerEquity = debtPerEquity;
        }

        public Double getNetProfitMargin() {
            return netProfitMargin;
        }

        public void setNetProfitMargin(Double netProfitMargin) {
            this.netProfitMargin = netProfitMargin;
        }

        public Double getPayoutRatio() {
            return payoutRatio;
        }

        public void setPayoutRatio(Double payoutRatio) {
            this.payoutRatio = payoutRatio;
        }

        public Double getDividendYield() {
            return dividendYield;
        }

        public void setDividendYield(Double dividendYield) {
            this.dividendYield = dividendYield;
        }

        public Double getDividendAnnual() {
            return dividendAnnual;
        }

        public void setDividendAnnual(Double dividendAnnual) {
            this.dividendAnnual = dividendAnnual;
        }

        public Double getWeekHigh() {
            return weekHigh;
        }

        public void setWeekHigh(Double weekHigh) {
            this.weekHigh = weekHigh;
        }

        public Double getWeekLow() {
            return weekLow;
        }

        public void setWeekLow(Double weekLow) {
            this.weekLow = weekLow;
        }

        public Double getTenDaysAvgVolume() {
            return tenDaysAvgVolume;
        }

        public void setTenDaysAvgVolume(Double tenDaysAvgVolume) {
            this.tenDaysAvgVolume = tenDaysAvgVolume;
        }

        public Double getThreeMonthAvgVolume() {
            return threeMonthAvgVolume;
        }

        public void setThreeMonthAvgVolume(Double threeMonthAvgVolume) {
            this.threeMonthAvgVolume = threeMonthAvgVolume;
        }

        public Double getBeta() {
            return beta;
        }

        public void setBeta(Double beta) {
            this.beta = beta;
        }
    }
}
