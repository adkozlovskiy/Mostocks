package com.kozlovskiy.mostocks.models.sys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Uptime {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "symbol")
    private String symbol;

    @ColumnInfo(name = "news_ut")
    private long newsUptime;

    @ColumnInfo(name = "30_chart_ut")
    private long thirtyChartUptime;

    @ColumnInfo(name = "hour_chart_ut")
    private long hourChartUptime;

    @ColumnInfo(name = "day_chart_ut")
    private long dayChartUptime;

    @ColumnInfo(name = "week_chart_ut")
    private long weekChartUptime;

    @ColumnInfo(name = "month_chart_ut")
    private long monthChartUptime;

    @ColumnInfo(name = "tech_ut")
    private long techAnalysisUptime;

    @ColumnInfo(name = "rec_ut")
    private long recommendationUptime;

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

    public long getNewsUptime() {
        return newsUptime;
    }

    public void setNewsUptime(long newsUptime) {
        this.newsUptime = newsUptime;
    }

    public long getThirtyChartUptime() {
        return thirtyChartUptime;
    }

    public void setThirtyChartUptime(long thirtyChartUptime) {
        this.thirtyChartUptime = thirtyChartUptime;
    }

    public long getHourChartUptime() {
        return hourChartUptime;
    }

    public void setHourChartUptime(long hourChartUptime) {
        this.hourChartUptime = hourChartUptime;
    }

    public long getDayChartUptime() {
        return dayChartUptime;
    }

    public void setDayChartUptime(long dayChartUptime) {
        this.dayChartUptime = dayChartUptime;
    }

    public long getWeekChartUptime() {
        return weekChartUptime;
    }

    public void setWeekChartUptime(long weekChartUptime) {
        this.weekChartUptime = weekChartUptime;
    }

    public long getMonthChartUptime() {
        return monthChartUptime;
    }

    public void setMonthChartUptime(long monthChartUptime) {
        this.monthChartUptime = monthChartUptime;
    }

    public long getTechAnalysisUptime() {
        return techAnalysisUptime;
    }

    public void setTechAnalysisUptime(long techAnalysisUptime) {
        this.techAnalysisUptime = techAnalysisUptime;
    }

    public long getRecommendationUptime() {
        return recommendationUptime;
    }

    public void setRecommendationUptime(long recommendationUptime) {
        this.recommendationUptime = recommendationUptime;
    }
}
