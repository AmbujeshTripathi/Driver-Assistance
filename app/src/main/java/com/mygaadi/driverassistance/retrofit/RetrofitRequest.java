package com.mygaadi.driverassistance.retrofit;

import com.mygaadi.driverassistance.MyApplication;
import com.mygaadi.driverassistance.model.DriverLoginModel;
import com.mygaadi.driverassistance.model.JobListModel;
import com.mygaadi.driverassistance.model.OtpModel;
import com.mygaadi.driverassistance.model.SubStatusListModel;

import java.util.HashMap;

/**
 * Created by Manish on 6/24/2015.
 */
public class RetrofitRequest {

    private static RestService restService = MyApplication.getRestAdapter().create(RestService.class);


    public static void OtpReceiver(HashMap<String, String> map, MyCallback<OtpModel> cb) {
        restService.OtpReceiver(map, cb);
    }


    public static void otpVerification(HashMap<String, String> map, MyCallback<DriverLoginModel> cb) {
        restService.otpVerification(map, cb);
    }

    public static void getJobs(HashMap<String, String> map, MyCallback<JobListModel> cb) {
        restService.getJobs(map, cb);
    }


    public static void getSubStatus(HashMap<String, String> map, MyCallback<SubStatusListModel> cb) {
        restService.getSubStatusList(map, cb);
    }
}
