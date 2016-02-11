package com.mygaadi.driverassistance.retrofit;


import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.model.DriverLoginModel;
import com.mygaadi.driverassistance.model.JobListModel;
import com.mygaadi.driverassistance.model.Model;
import com.mygaadi.driverassistance.model.OtpModel;
import com.mygaadi.driverassistance.model.SubStatusListModel;

import java.util.HashMap;
import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;

public interface RestService {
    @FormUrlEncoded
    @POST(Constants.LOGIN_URL)
    void OtpReceiver(@FieldMap Map<String, String> map,
                     MyCallback<OtpModel> callback);

    @FormUrlEncoded
    @POST(Constants.OTP_VERIFICATION)
    void otpVerification(@FieldMap Map<String, String> map, MyCallback<DriverLoginModel> callback);

    @GET(Constants.REQUEST_JOBS)
    void getJobs(@QueryMap Map<String, String> map, MyCallback<JobListModel> callback);

    @GET(Constants.SUB_STATUS_LIST_URL)
    void getSubStatusList(@QueryMap HashMap<String, String> map, MyCallback<SubStatusListModel> cb);

    @FormUrlEncoded
    @POST(Constants.UPDATE_STATUS)
    void updateStatus(@FieldMap HashMap<String, String> map, MyCallback<Model> cb);

    @FormUrlEncoded
    @POST(Constants.SEND_LAT_LONGS)
    void sendLatLongs(@FieldMap HashMap<String, String> map, MyCallback<Model> cb);

    @Multipart
    @POST(Constants.JOB_CARD_UPLOAD_IMAGE_URL)
    void uploadCaptureLeadImages(@Part(Constants.UPLOAD_IMAGE) TypedFile file,
                                 @Part(Constants.JOB_ID) String jobId,
                                 @Part(Constants.DOC_TYPE) String docType,
                                 @Part(Constants.USER_ID) String userId,
                                 UploadCallback<Model> cb);
}
