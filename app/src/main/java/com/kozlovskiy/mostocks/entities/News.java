package com.kozlovskiy.mostocks.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class News {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "ticker")
    private String ticker;

    @ColumnInfo(name = "datetime")
    @SerializedName("datetime")
    @Expose
    private long datetime;

    @ColumnInfo(name = "headline")
    @SerializedName("headline")
    @Expose
    private String headline;

    @ColumnInfo(name = "summary")
    @SerializedName("summary")
    @Expose
    private String summary;

    @ColumnInfo(name = "image")
    @SerializedName("image")
    @Expose
    private String image;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    @Expose
    private String url;

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

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
