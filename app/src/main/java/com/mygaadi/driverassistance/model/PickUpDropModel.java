package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 2/8/2016.
 */
public class PickUpDropModel {
    @SerializedName("sub_status_id")
    @Expose
    private String subStatusId;
    @SerializedName("sub_status_name")
    @Expose
    private String subStatusName;
    @SerializedName("status_id")
    @Expose
    private String statusId;
    @SerializedName("dialog")
    @Expose
    private String dialog;

    /**
     * @return The subStatusId
     */
    public String getSubStatusId() {
        return subStatusId;
    }

    /**
     * @param subStatusId The sub_status_id
     */
    public void setSubStatusId(String subStatusId) {
        this.subStatusId = subStatusId;
    }

    /**
     * @return The subStatusName
     */
    public String getSubStatusName() {
        return subStatusName;
    }

    /**
     * @param subStatusName The sub_status_name
     */
    public void setSubStatusName(String subStatusName) {
        this.subStatusName = subStatusName;
    }

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
     * @return The dialog
     */
    public String getDialog() {
        return dialog;
    }

    /**
     * @param dialog The dialog
     */
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

}
