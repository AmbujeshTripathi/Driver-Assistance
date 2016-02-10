package com.mygaadi.driverassistance.retrofit;

import com.mygaadi.driverassistance.MyApplication;
import com.mygaadi.driverassistance.model.DriverLoginModel;
import com.mygaadi.driverassistance.model.JobListModel;
import com.mygaadi.driverassistance.model.Model;
import com.mygaadi.driverassistance.model.OtpModel;
import com.mygaadi.driverassistance.model.SubStatusListModel;

import java.io.File;
import java.util.HashMap;

import retrofit.mime.TypedFile;

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

    public static void updateStatus(HashMap<String, String> map, MyCallback<Model> cb) {
        restService.updateStatus(map, cb);
    }


    public static void uploadJobCard(File file,
                                     String jobId,
                                     String docType,
                                     String userId,
                                     UploadCallback<Model> cb) {

        restService.uploadCaptureLeadImages(new TypedFile("multipart/form-data", file),
                jobId,
                docType,
                userId,
                cb);
    }
}
