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
    String JOB_ID = "jobId";
    String KEY = "key";
    String KEY_LATITUDE = "latitude";
    String KEY_LONGITUDE = "longitude";
    String KEY_STATUS_ID = "statusId";
    String KEY_SUB_STATUS_ID = "subStatusId";
    String KEY_COMMENT = "comment";
    String KEY_SUB_STATUS_MODEL = "subStatusModel";
    String KEY_DOC_ID = "docId";
    String UPLOAD_IMAGE = "uploadImage";
    String DOC_TYPE = "docType";

    String APP_INSURER = "";


    String BASE_URL_CONSTANT = "http://carservicedev1.gaadi.com/service_apis";
    String LOGIN_URL = "/save/api_login?key=46a94e1a05ab04758d9b4597b5e375a2";

    String OTP_VERIFICATION = "/save/api_login?key=46a94e1a05ab04758d9b4597b5e375a2";
    String REQUEST_JOBS = "/fetch/get_job_list?key=46a94e1a05ab04758d9b4597b5e375a2";
    String SUB_STATUS_LIST_URL = "/fetch/get_sub_status?key=46a94e1a05ab04758d9b4597b5e375a2";
    String UPDATE_STATUS = "/save/job_conversation?key=46a94e1a05ab04758d9b4597b5e375a2";
    String JOB_CARD_UPLOAD_IMAGE_URL = "/save/image_upload?key=46a94e1a05ab04758d9b4597b5e375a2";

    String STATUS_IN_PROGRESS = "2";
    String STATUS_CANCEL = "3";
    String STATUS_COMPLETE = "4";
    String SUB_STATUS_CANCEL_BY_DRIVER = "16";
    String SUB_STATUS_READY_FOR_NEXT_JOB = "18";


    public enum SERVICE_MODE {
        OTP_RECEIVE, OTP_VERIFICATION, GET_JOBS, UPDATE_STATUS_IN_PROGRESS, UPLOAD_JOB_CARD,//
        SUB_STATUS_LIST, UPDATE_STATUS_COMPLETE_CANCEL, UPDATE_STATUS_CANCEL, UPLOAD_SELFIE
    }

}
