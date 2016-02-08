package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 2/8/2016.
 */
public class SubStatusModel extends Model {
    public SubStatusType getData() {
        return data;
    }

    public void setData(SubStatusType data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose

    private SubStatusType data;
}
