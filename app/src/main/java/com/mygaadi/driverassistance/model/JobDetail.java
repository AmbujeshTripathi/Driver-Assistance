package com.mygaadi.driverassistance.model;

/**
 * Created by Manish on 6/24/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobDetail {

    @SerializedName("jobId")
    @Expose
    private String jobId;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("jobType")
    @Expose
    private String jobType;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerMobile")
    @Expose
    private String customerMobile;
    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;
    @SerializedName("customerLocality")
    @Expose
    private String customerLocality;
    @SerializedName("jobTypeLocality")
    @Expose
    private String jobTypeLocality;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("added")
    @Expose
    private String added;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("hubId")
    @Expose
    private String hubId;
    @SerializedName("hubName")
    @Expose
    private String hubName;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("statusId")
    @Expose
    private String statusId;
    @SerializedName("statusName")
    @Expose
    private String statusName;
    @SerializedName("substatusId")
    @Expose
    private String substatusId;
    @SerializedName("subStatusName")
    @Expose
    private String subStatusName;
    @SerializedName("hubAddress")
    @Expose
    private String hubAddress;

    /**
     * @return The jobId
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * @param jobId The jobId
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * @return The make
     */
    public String getMake() {
        return make;
    }

    /**
     * @param make The make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * @return The model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model The model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return The jobType
     */
    public String getJobType() {
        return jobType;
    }

    /**
     * @param jobType The jobType
     */
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    /**
     * @return The customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName The customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return The customerMobile
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * @param customerMobile The customerMobile
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    /**
     * @return The customerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @param customerEmail The customerEmail
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * @return The customerLocality
     */
    public String getCustomerLocality() {
        return customerLocality;
    }

    /**
     * @param customerLocality The customerLocality
     */
    public void setCustomerLocality(String customerLocality) {
        this.customerLocality = customerLocality;
    }

    /**
     * @return The jobTypeLocality
     */
    public String getJobTypeLocality() {
        return jobTypeLocality;
    }

    /**
     * @param jobTypeLocality The jobTypeLocality
     */
    public void setJobTypeLocality(String jobTypeLocality) {
        this.jobTypeLocality = jobTypeLocality;
    }

    /**
     * @return The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return The added
     */
    public String getAdded() {
        return added;
    }

    /**
     * @param added The added
     */
    public void setAdded(String added) {
        this.added = added;
    }

    /**
     * @return The updated
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * @param updated The updated
     */
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * @return The hubId
     */
    public String getHubId() {
        return hubId;
    }

    /**
     * @param hubId The hubId
     */
    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    /**
     * @return The hubName
     */
    public String getHubName() {
        return hubName;
    }

    /**
     * @param hubName The hubName
     */
    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The statusId
     */
    public String getStatusId() {
        return statusId;
    }

    /**
     * @param statusId The statusId
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
     * @param statusName The statusName
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return The substatusId
     */
    public String getSubstatusId() {
        return substatusId;
    }

    /**
     * @param substatusId The substatusId
     */
    public void setSubstatusId(String substatusId) {
        this.substatusId = substatusId;
    }

    /**
     * @return The subStatusName
     */
    public String getSubStatusName() {
        return subStatusName;
    }

    /**
     * @param subStatusName The subStatusName
     */
    public void setSubStatusName(String subStatusName) {
        this.subStatusName = subStatusName;
    }

    /**
     * @return The hubAddress
     */
    public String getHubAddress() {
        return hubAddress;
    }

    /**
     * @param hubAddress The hubAddress
     */
    public void setHubAddress(String hubAddress) {
        this.hubAddress = hubAddress;
    }
}

