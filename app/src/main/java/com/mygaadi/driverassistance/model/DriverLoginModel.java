package com.mygaadi.driverassistance.model;

/**
 * Created by Manish on 6/24/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverLoginModel extends Model {

    @SerializedName("user-token")
    @Expose
    private String userToken;
    @SerializedName("data")
    @Expose
    private Data data;


    /**
     * @return The userToken
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * @param userToken The user-token
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

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


    public class Data {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("name")
        @Expose
        private String firstName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("address")
        @Expose
        private String address;



        /**
         * @return The userId
         */
        public String getUserId() {
            return userId;
        }

        /**
         * @param userId The user_id
         */
        public void setUserId(String userId) {
            this.userId = userId;
        }


        /**
         * @return The status
         */
        public String getStatus() {
            return status;
        }

        /**
         * @param status The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * @return The firstName
         */
        public String getName() {
            return firstName;
        }

        /**
         * @param firstName The first_name
         */
        public void setName(String firstName) {
            this.firstName = firstName;
        }


        /**
         * @return The email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email The email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
