package com.kozlovskiy.mostocks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveData {

    @SerializedName("c")
    @Expose
    private List<String> c = null;
    @SerializedName("p")
    @Expose
    private Double p;
    @SerializedName("s")
    @Expose
    private String s;
    @SerializedName("t")
    @Expose
    private Integer t;
    @SerializedName("v")
    @Expose
    private Integer v;

    public List<String> getC() {
        return c;
    }

    public void setC(List<String> c) {
        this.c = c;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}