package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/31/2015.
 */
public class OtpModel extends Model{

    @SerializedName("otp_id")
    @Expose
    private String otpId;

    /**
     *
     * @return
     * The otpId
     */
    public String getOtpId() {
        return otpId;
    }

    /**
     *
     * @param dealerId
     * The otp_id
     */
    public void setOtpId(String dealerId) {
        this.otpId = dealerId;
    }

    /**
     *
     * @return
     * The dealerName
     */
}
