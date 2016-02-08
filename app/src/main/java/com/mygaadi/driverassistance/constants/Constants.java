package com.mygaadi.driverassistance.constants;

/**
 * Created by Ambujesh on 2/5/2016.
 */
public interface Constants {

    String APP_PREF = "";
    String USERID = "";
    String KEY_MOBILE = "";
    String KEY_OTPCODE = "";
    String USER_TOKEN = "";
    String USER_NAME = "";
    String USER_EMAIL = "";
    String START_TIME = "StartTime";
    String END_TIME = "EndTime";


    String APP_INSURER = "";


    String BASE_URL_CONSTANT = "http://servicestaging1.gaadi.com";
    String LOGIN_URL = "/service_apis/api_dummy/api_login.php";

    String OTP_VERIFICATION = "";
    String DEALER_LOGIN = "";
    String SUB_STATUS_LIST_URL = "/get_sub_status.php";
    String REQUEST_JOBS = "";

    public enum SERVICE_MODE {OTP_RECEIVE, OTP_VERIFICATION, GET_JOBS}
    public enum SERVICE_MODE {OTP_RECEIVE, SUB_STATUS_LIST, OTP_VERIFICATION}
}
