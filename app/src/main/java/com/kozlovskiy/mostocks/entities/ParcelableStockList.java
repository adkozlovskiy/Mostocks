package com.kozlovskiy.mostocks.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ParcelableStockList extends ArrayList<Stock> implements Parcelable {
    protected ParcelableStockList(Parcel in) {
        this();
        readFromParcel(in);
    }

    public ParcelableStockList(){

    }

    public static final Creator<ParcelableStockList> CREATOR = new Creator<ParcelableStockList>() {
        @Override
        public ParcelableStockList createFromParcel(Parcel in) {
            return new ParcelableStockList(in);
        }

        @Override
        public ParcelableStockList[] newArray(int size) {
            return new ParcelableStockList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();
        dest.writeInt(size);

        for (int n = 0; n < size; n++) {
            Stock stock = this.get(n);

            dest.writeString(stock.getTicker());
            dest.writeDouble(stock.getCapitalization());
            dest.writeString(stock.getCurrency());
            dest.writeString(stock.getIndustry());
            dest.writeString(stock.getIpo());
            dest.writeString(stock.getLogo());
            dest.writeString(stock.getName());
        }
    }

    private void readFromParcel(Parcel in) {
        this.clear();
        int size = in.readInt();

        for (int n = 0; n < size; n++) {
            Stock stock = new Stock(in);
            this.add(stock);
        }
    }
}
