package com.mygaadi.driverassistance.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.custom_view.CustomGearDialog;
import com.mygaadi.driverassistance.model.DriverLoginModel;
import com.mygaadi.driverassistance.model.OtpModel;
import com.mygaadi.driverassistance.recievers.SmsReaderReceiver;
import com.mygaadi.driverassistance.retrofit.MyCallback;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;
import com.mygaadi.driverassistance.utils.Utility;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

import java.util.HashMap;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ambujesh on 6/19/2015.
 */
public class ActivityRegistration extends AppCompatActivity implements View.OnClickListener,
        RestCallback, SmsReaderReceiver.SmsCallback {

    private final int LOCATION_DB_TASK = 1101;
    private final int MAKE_MODAL_DB_TASK = 1102;
    private boolean flagLocationDbTaskCompleted, flagMakeModalDbTaskCompleted;
    public static final String TAG = "ActivityRegistration";
    private EditText etMobileNumber, edtOtpCode;
    private Button btnRegister, btnOTPVerify;
    private Dialog mOtpDialog;
    private AlertDialog.Builder alertDialog;
    private OtpModel otpModel;
    private DriverLoginModel dealerLoginModel;
    private boolean result;
    TextView txtResendOtp, resendMessage;
    //Progress dialog
    private CustomGearDialog mProgressHUD;
    int time = 120 * 1000, interval = 1000;
    CountDownTimer mTimer;
    public static final int PERMISSION_REQUEST_CODE = 102;
    //Broadcast receiver to monitor the sms
    private SmsReaderReceiver mSmsReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        showNoInternetConnectionDialog(this);
        initViews();

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 10) {
                    UtilitySingleton.getInstance(ActivityRegistration.this).hideSoftKeyBoard(etMobileNumber);
                }
            }
        });

        registerSmsListener();
    }

    //Methods for the progress dialog

    /**
     * Method to start the progress bar.
     *
     * @param message Starting message
     */
    public void startProgress(String message) {
        if (mProgressHUD == null || !mProgressHUD.isShowing()) {
            mProgressHUD = Utility.showLoadingDialog(this, message);
        }
    }

    /**
     * Method to update the progress bar with the new message
     */
    public void changeMessage(String msg) {
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            mProgressHUD.setMessage(msg);
        }
    }

    //Method to stop the progress bar
    public void StopProgress() {
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            mProgressHUD.dismiss();
        }
        if (true) {
            Intent intent = new Intent(this, ActivityRegistration.class);
            startActivity(intent);
            finish();
        }
    }


    //--End of progress dialog methods

    /**
     * Method to register the broadcast receiver for monitoring the sms
     */
    private void registerSmsListener() {
        if (mSmsReceiver == null) {
            mSmsReceiver = new SmsReaderReceiver();
            mSmsReceiver.setListener(this);
        }

        // Register a broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //Registering the broadcast receiver
        registerReceiver(mSmsReceiver, intentFilter);

    }

    @Override
    public void onSmsReceived(String msg, String callerNumber) {
        //null check
        if (msg == null) {
            return;
        }

        //Expected string to recognise the sender
        final String SMS_SENDER = "SrvDek";
        final String SMS_SENDER1 = "iGAADI";
        // Checking for the sender
        String otp = msg.replaceAll("\\D+", "");
        if (callerNumber != null && (callerNumber.contains(SMS_SENDER) || callerNumber.contains(SMS_SENDER1))) {
            //Setting the otp to the text view
            edtOtpCode.setText(otp);
            stopTimer();
        }

        //   resendMessage.setVisibility(View.GONE);
        Toast.makeText(this, "OTP Received: " + otp + " Sender: " + callerNumber, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExceptionOccurred(Exception e) {

    }

    /**
     * Method to remove the broadcast receiver from the activity.
     */
    private void removeSmsListener() {
        unregisterReceiver(mSmsReceiver);
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSmsReceiver);
    }

    public boolean showNoInternetConnectionDialog(final Context c) {

        if (!Utility.isNetworkAvailable(this)) {
            alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("No Connection");
            alertDialog.setMessage("Cannot connect to the internet!");
            alertDialog.setPositiveButton("Retry", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!Utility.isNetworkAvailable(c))
                        showNoInternetConnectionDialog(c);
                    else {
                        result = true;

                    }
                }
            });
            alertDialog.setNegativeButton("Exit", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ((Activity) c).finish();
                }
            });
            alertDialog.create().show();
        } else {
            result = true;

        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeSmsListener();
        stopTimer();
    }

    private void initViews() {
        etMobileNumber = (EditText) findViewById(R.id.et_mobile_number);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                requestOtp("Logging In");
                break;
            case R.id.tvBack:
                btnRegister.setEnabled(true);
                dismissOtpDialog();
                break;
            case R.id.txt_reSendOtp:
                requestOtp("Resending OTP..");
                break;
            case R.id.btn_verify:
                Utility.hideSoftKeyboard(this, v);
                String otpEntered = edtOtpCode.getText().toString();
                if (otpEntered.equals("")) {
                    Utility.showToast(this, "Please enter OTP");
                    return;
                }
                HashMap<String, String> params = new HashMap<>();

                params.put(Constants.KEY_MOBILE, etMobileNumber.getText().toString());
                params.put(Constants.KEY, "46a94e1a05ab04758d9b4597b5e375a2");
                params.put(Constants.KEY_OTP_CODE, otpEntered);

                startProgress("Verifying OTP..");
                RetrofitRequest.otpVerification(params, new MyCallback<DriverLoginModel>(this, this, false, null, "Loading...",
                        Constants.SERVICE_MODE.OTP_VERIFICATION));
                break;
            default:
                break;
        }
    }

    private void requestOtp(String msg) {
        if (isFieldsHaveValue()) {
            if (isFieldValid()) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_MOBILE, etMobileNumber.getText().toString());
                params.put(Constants.KEY, "46a94e1a05ab04758d9b4597b5e375a2");
                RetrofitRequest.OtpReceiver(params, new MyCallback<OtpModel>(this, this, true, null, msg,
                        Constants.SERVICE_MODE.OTP_RECEIVE));
            }
        }
    }

    private void showOtpDialog() {
        Utility.checkForPermission(this, new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
        mOtpDialog = new Dialog(ActivityRegistration.this);
        mOtpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mOtpDialog.setContentView(R.layout.dialog_otp_request);
        mOtpDialog.setCanceledOnTouchOutside(false);
        mOtpDialog.setCancelable(false);
        mOtpDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = mOtpDialog.getWindow();
        lp1.copyFrom(window1.getAttributes());
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);

        TextView txtBack = (TextView) mOtpDialog.findViewById(R.id.tvBack);
        txtResendOtp = (TextView) mOtpDialog.findViewById(R.id.txt_reSendOtp);
        TextView txtMobileNumber = (TextView) mOtpDialog.findViewById(R.id.txt_mobileNumber);
        edtOtpCode = (EditText) mOtpDialog.findViewById(R.id.edt_otpCode);
        btnOTPVerify = (Button) mOtpDialog.findViewById(R.id.btn_verify);
        resendMessage = (TextView) mOtpDialog.findViewById(R.id.resend_message);
        txtMobileNumber.setText(etMobileNumber.getText().toString());

        txtBack.setOnClickListener(this);
        txtResendOtp.setOnClickListener(this);
        btnOTPVerify.setOnClickListener(this);
        mOtpDialog.show();
    }

    private void dismissOtpDialog() {
        if (mOtpDialog != null && mOtpDialog.isShowing()) {
            mOtpDialog.dismiss();
            stopTimer();
        }
    }

    /* This method is used for the validating the correct number or not */
    private boolean isFieldValid() {
        if (Utility.isValidPhoneNumber(etMobileNumber) != null) {
            return true; // Please Remove if you are validating Pincode
        } else {
            etMobileNumber.setError(getResources().getString(R.string.please_enter_valid_number));
            etMobileNumber.requestFocus();
            return false;
        }
    }

    /* This method is used to check whether edit text has value or not */
    private boolean isFieldsHaveValue() {
        if (etMobileNumber.getText().toString().equals("")) {
            etMobileNumber.setError(getResources().getString(R.string.please_enter_your_phone_no));
            etMobileNumber.requestFocus();
            return false;
        }
        return true;

    }

    @Override
    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode) {
        if (Utility.alertDialogForNoInternet == null || (Utility.alertDialogForNoInternet != null && !Utility.alertDialogForNoInternet.isShowing())) {
            Utility.dialogOnFailure(this);
        }
        stopProgress();
        stopTimer();
    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {
        switch (mode) {
            case OTP_RECEIVE:
                stopProgress();
                otpModel = (OtpModel) model;
                if (!(otpModel.getStatus())) {
                    Utility.showToast(this, otpModel.getMessage());
                    return;
                }

                if (mOtpDialog == null || !mOtpDialog.isShowing()) {
                    showOtpDialog();
                    startTimer();
                }

//                otp = otpModel.getOtpId();
                Utility.showToast(ActivityRegistration.this, "OTP sent to your registered mobile number");
                break;


            case OTP_VERIFICATION:
                dealerLoginModel = (DriverLoginModel) model;
                if ((((DriverLoginModel) model).getStatus())) {
                    {
                        stopTimer();
                        mOtpDialog.dismiss();
                        saveDataInSharedPref(this, dealerLoginModel);
                    }


                } else {
                    Utility.showToast(ActivityRegistration.this, "Wrong OTP. Please enter correct OTP");
                    stopProgress();
                    edtOtpCode.setText("");
                    // stopTimer();

                }
                break;

        }
    }


    private void saveDataInSharedPref(Context context, DriverLoginModel model) {

        UtilitySingleton utilitySingleton = UtilitySingleton.getInstance(context);

        DriverLoginModel.Data data = model.getData();
        if (data == null) {
            return;
        }
        utilitySingleton.saveStringInSharedPref(Constants.USER_ID, data.getUserId());
        utilitySingleton.saveStringInSharedPref(Constants.USER_NAME, data.getName());
        utilitySingleton.saveStringInSharedPref(Constants.USER_EMAIL, data.getEmail());
        utilitySingleton.saveStringInSharedPref(Constants.USER_TOKEN, model.getUserToken());
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }


    public void stopProgress() {
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            mProgressHUD.dismiss();
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            resendMessage.setVisibility(View.GONE);

        }
    }

    private void startTimer() {
        txtResendOtp.setEnabled(false);
        txtResendOtp.setBackgroundResource(R.color.gray);
        mTimer = new CountDownTimer(time, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendMessage.setVisibility(View.VISIBLE);
                resendMessage.setText(String.format(getResources().getString(R.string.resend_otp_message), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                txtResendOtp.setEnabled(true);
                txtResendOtp.setBackgroundResource(R.drawable.selector_inform_cardekho_button);
                resendMessage.setVisibility(View.GONE);
            }
        }.start();
    }


}
