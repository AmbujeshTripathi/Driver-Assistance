package com.mygaadi.driverassistance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ambujesh on 2/8/2016.
 */
public class SubStatusListModel extends Model implements Serializable{

    @SerializedName("data")
    @Expose
    private Data data;


    /**
     * @return The data
     */
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {
        this.data = data;
    }


    public class Data  implements Serializable{

        @SerializedName("pickup")
        @Expose
        private List<SubStatusModel> pickup = new ArrayList<SubStatusModel>();
        @SerializedName("drop")
        @Expose
        private List<SubStatusModel> drop = new ArrayList<SubStatusModel>();

        /**
         * @return The pickup
         */
        public List<SubStatusModel> getPickup() {
            return pickup;
        }

        /**
         * @param pickup The pickup
         */
        public void setPickup(List<SubStatusModel> pickup) {
            this.pickup = pickup;
        }

        /**
         * @return The drop
         */
        public List<SubStatusModel> getDrop() {
            return drop;
        }

        /**
         * @param drop The drop
         */
        public void setDrop(List<SubStatusModel> drop) {
            this.drop = drop;
        }

    }

    public class SubStatusModel implements Serializable{

        @SerializedName("substatus_id")
        @Expose
        private String subStatusId;
        @SerializedName("substatus_name")
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


}
