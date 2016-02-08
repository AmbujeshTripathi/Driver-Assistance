package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav on 2/8/2016.
 */
public class StatusType {

    @SerializedName("pickUp ")
    @Expose
    private List<PickupDropStatus> pickUp = new ArrayList<PickupDropStatus>();
    @SerializedName("drop")
    @Expose
    private List<PickupDropStatus> drop = new ArrayList<PickupDropStatus>();

    /**
     * @return The pickUp
     */
    public List<PickupDropStatus> getPickUp() {
        return pickUp;
    }

    /**
     * @param pickUp The pickUp
     */
    public void setPickUp(List<PickupDropStatus> pickUp) {
        this.pickUp = pickUp;
    }

    /**
     * @return The drop
     */
    public List<PickupDropStatus> getDrop() {
        return drop;
    }

    /**
     * @param drop The drop
     */
    public void setDrop(List<PickupDropStatus> drop) {
        this.drop = drop;
    }
}
