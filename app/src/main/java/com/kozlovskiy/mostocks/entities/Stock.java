package com.kozlovskiy.mostocks.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Stock implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "ticker")
    @SerializedName("ticker")
    @Expose
    @NonNull
    private final String ticker;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    @Expose
    private String currency;

    @ColumnInfo(name = "logo")
    @SerializedName("logo")
    @Expose
    private String logo;

    @ColumnInfo(name = "industry")
    @SerializedName("finnhubIndustry")
    @Expose
    private String industry;

    @ColumnInfo(name = "ipo")
    @SerializedName("ipo")
    @Expose
    private String ipo;

    @ColumnInfo(name = "capitalization")
    @SerializedName("marketCapitalization")
    @Expose
    private double capitalization;

    private boolean isFavorite;

    public Stock(@NonNull String ticker) {
        this.ticker = ticker;
    }

    protected Stock(Parcel in) {
        ticker = in.readString();
        name = in.readString();
        currency = in.readString();
        logo = in.readString();
        industry = in.readString();
        ipo = in.readString();
        capitalization = in.readDouble();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    @NonNull
    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIpo() {
        return ipo;
    }

    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    public double getCapitalization() {
        return capitalization;
    }

    public void setCapitalization(double capitalization) {
        this.capitalization = capitalization;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticker);
        dest.writeString(name);
        dest.writeString(currency);
        dest.writeString(logo);
        dest.writeString(industry);
        dest.writeString(ipo);
        dest.writeDouble(capitalization);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
