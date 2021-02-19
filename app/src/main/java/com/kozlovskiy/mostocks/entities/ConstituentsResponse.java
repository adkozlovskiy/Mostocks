package com.kozlovskiy.mostocks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConstituentsResponse {

    @SerializedName("constituents")
    @Expose
    private List<String> constituents = null;

    public List<String> getConstituents() {
        return constituents;
    }

}
