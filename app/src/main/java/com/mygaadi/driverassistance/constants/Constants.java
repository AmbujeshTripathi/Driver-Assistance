package com.mygaadi.driverassistance.constants;

/**
 * Created by Ambujesh on 2/5/2016.
 */
public interface Constants {

    String APP_PREF = "AppPreferenceDriver-Assistance";
    String USER_ID = "user_id";
    String KEY_MOBILE = "mobile";
    String KEY_OTP_CODE = "otp";
    String USER_TOKEN = "user_token";
    String USER_NAME = "user_name";
    String USER_EMAIL = "email";
    String START_TIME = "from";
    String END_TIME = "to";
    String JOB_TYPE = "job_type";
    String IS_SELFIE_UPLOADED = "is_selfie_uploaded";
    String CUSTOMER_ADDRESS = "customer_address";
    String HUB_ADDRESS = "hub_address";
    String JOB_ID = "job_id";


    String APP_INSURER = "";


    String BASE_URL_CONSTANT = "http://carservicedev1.gaadi.com/service_apis";
    String LOGIN_URL = "/save/api_login?key=46a94e1a05ab04758d9b4597b5e375a2";

    String OTP_VERIFICATION = "/save/api_login?key=46a94e1a05ab04758d9b4597b5e375a2";
    String REQUEST_JOBS = "/fetch/get_job_list?key=46a94e1a05ab04758d9b4597b5e375a2";
    String SUB_STATUS_LIST_URL = "/get_sub_status.php";
    String KEY = "key";

    public enum SERVICE_MODE {OTP_RECEIVE, OTP_VERIFICATION, GET_JOBS, SUB_STATUS_LIST}
}
