package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 2/8/2016.
 */
public class StatusModel extends Model {
    public StatusType getData() {
        return data;
    }

    public void setData(StatusType data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose

    private StatusType data;
}
