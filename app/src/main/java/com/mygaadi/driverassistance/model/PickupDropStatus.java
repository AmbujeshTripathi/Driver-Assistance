package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 2/8/2016.
 */
public class PickupDropStatus {
    @SerializedName("status_id")
    @Expose
    private String statusId;
    @SerializedName("status_name")
    @Expose
    private String statusName;

    /**
     * @return The statusId
     */
    public String getStatusId() {
        return statusId;
    }

    /**
     * @param statusId The status_id
     */
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    /**
     * @return The statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName The status_name
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
