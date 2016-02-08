package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav on 2/8/2016.
 */
public class SubStatusType {
    @SerializedName("pickUp ")
    @Expose
    private List<PickUpDropModel> pickUp = new ArrayList<PickUpDropModel>();
    @SerializedName("drop")
    @Expose
    private List<PickUpDropModel> drop = new ArrayList<PickUpDropModel>();

    /**
     * @return The pickUp
     */
    public List<PickUpDropModel> getPickUp() {
        return pickUp;
    }

    /**
     * @param pickUp The pickUp
     */
    public void setPickUp(List<PickUpDropModel> pickUp) {
        this.pickUp = pickUp;
    }

    /**
     * @return The drop
     */
    public List<PickUpDropModel> getDrop() {
        return drop;
    }

    /**
     * @param drop The drop
     */
    public void setDrop(List<PickUpDropModel> drop) {
        this.drop = drop;
    }
}
