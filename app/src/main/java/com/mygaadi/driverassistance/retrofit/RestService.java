package com.mygaadi.driverassistance.retrofit;


import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.model.DealerLoginModel;
import com.mygaadi.driverassistance.model.JobDetailModel;
import com.mygaadi.driverassistance.model.OtpModel;
import com.mygaadi.driverassistance.model.SubStatusListModel;

import java.util.HashMap;
import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RestService {
    @FormUrlEncoded
    @POST(Constants.DEALER_LOGIN)
    void OtpReceiver(@FieldMap Map<String, String> map,
                     MyCallback<OtpModel> callback);

    @FormUrlEncoded
    @POST(Constants.OTP_VERIFICATION)
    void otpVerification(@FieldMap Map<String, String> map, MyCallback<DealerLoginModel> callback);

    @GET(Constants.REQUEST_JOBS)
    void getJobs(@FieldMap Map<String, String> map, MyCallback<JobDetailModel> callback);

    @FormUrlEncoded
    @POST(Constants.SUB_STATUS_LIST_URL)
    void getSubStatusList(@FieldMap HashMap<String, String> map, MyCallback<SubStatusListModel> cb);
}
