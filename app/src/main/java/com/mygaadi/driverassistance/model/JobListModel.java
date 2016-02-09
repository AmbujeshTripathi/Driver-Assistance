package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Himanshu on 2/8/2016.
 */
public class JobListModel extends Model {

    @SerializedName("data")
    @Expose
    private List<JobDetail> data = new ArrayList<JobDetail>();

    /**
     * @return The data
     */
    public List<JobDetail> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<JobDetail> data) {
        this.data = data;
    }

}
