package com.kozlovskiy.mostocks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocketResponse {

    @SerializedName("data")
    @Expose
    private List<SocketData> data = null;

    @SerializedName("type")
    @Expose
    private String type;

    public List<SocketData> getData() {
        return data;
    }

    public void setData(List<SocketData> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
