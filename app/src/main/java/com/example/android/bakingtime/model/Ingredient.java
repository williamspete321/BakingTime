package com.example.android.bakingtime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    @Expose
    private Double mQuantity;
    @SerializedName("measure")
    @Expose
    private Measurement mMeasurementType;
    @SerializedName("ingredient")
    @Expose
    private String mContent;

    public Ingredient(double quantity, Measurement measurement, String content) {
        mQuantity = quantity;
        mMeasurementType = measurement;
        mContent = content;
    }

    public enum Measurement {
        @SerializedName("G")
        @Expose
        G,
        @SerializedName("TSP")
        @Expose
        TSP,
        @SerializedName("TBLSP")
        @Expose
        TBLSP,
        @SerializedName("K")
        @Expose
        K,
        @SerializedName("CUP")
        @Expose
        CUP,
        @SerializedName("OZ")
        @Expose
        OZ,
        @SerializedName("UNIT")
        @Expose
        UNIT
    }

    public Double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Double quantity) {
        mQuantity = quantity;
    }

    public Measurement getMeasurementType() {
        return mMeasurementType;
    }

    public void setMeasurementType(Measurement measurementType) {
        mMeasurementType = measurementType;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

}
