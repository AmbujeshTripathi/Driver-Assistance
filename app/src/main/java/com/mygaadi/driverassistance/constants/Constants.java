package com.mygaadi.driverassistance.constants;

/**
 * Created by Ambujesh on 2/5/2016.
 */
public interface Constants {

    String APP_PREF = "";
    java.lang.String USERID = "";
    String KEY_MOBILE = "";
    String KEY_OTPCODE = "";
    String USER_TOKEN = "";
    String USER_NAME = "";
    String USER_EMAIL = "";


    String APP_INSURER = "";


    String BASE_URL_CONSTANT = "http://servicestaging1.gaadi.com/service_apis/api_dummy";

    String OTP_VERIFICATION = "";
    String DEALER_LOGIN = "";
    String SUB_STATUS_LIST_URL = "/get_sub_status.php";

    public enum SERVICE_MODE {OTP_RECEIVE, SUB_STATUS_LIST, OTP_VERIFICATION}
}
