package com.mygaadi.driverassistance.retrofit;

import android.content.Context;
import android.view.View;

import com.google.gson.GsonBuilder;
import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.custom_view.CustomGearDialog;
import com.mygaadi.driverassistance.utils.Utility;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MyCallback<T> implements Callback<T> {
    private RestCallback restCallback;
    private static String defaultMessage = "Loading";
    private CustomGearDialog mProgressHUD;
    private View v;
    private Context baseActivity;
    private Constants.SERVICE_MODE mode;
    private UtilitySingleton utilitySingleton;

    /**
     * Description : Callback with custom message
     */
    @SuppressWarnings("unchecked")
    public MyCallback(Context context, RestCallback restCallback, boolean showProgress, View v, String message, Constants.SERVICE_MODE mode) {
        this.restCallback = restCallback;
        this.baseActivity = context;
        utilitySingleton = UtilitySingleton.getInstance(context);
        this.mode = mode;
        if (message != null)
            defaultMessage = message;
        if (v != null) {
            this.v = v;
            v.setClickable(true);
        }
        if (showProgress && Utility.isNetworkAvailable(context)) {
            startProgress(defaultMessage);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        stopProgress();
        if (error.getKind() == RetrofitError.Kind.NETWORK) {
            utilitySingleton.ShowToast(baseActivity.getResources().getString(R.string.toast_network_not_available), (Context) baseActivity);
            return;
        }
//        utilitySingleton.ShowToast(baseActivity.getResources().getString(R.string.something_went_wrong), (Context) baseActivity);
        if (restCallback != null)
            restCallback.onFailure(error, mode);
    }

    @Override
    public void success(T model, Response arg1) {
        stopProgress();
        ShowLog(model, arg1);
        if (restCallback != null)
            restCallback.onSuccess(model, arg1, mode);
    }

    private void ShowLog(T model, Response arg1) {
        String body = UtilitySingleton.getInstance(baseActivity).getResponse(arg1);
        RestLog.l(this, "URL==> " + arg1.getUrl());
        RestLog.l(this, (body.equalsIgnoreCase("null")) ? "Model==> " + new GsonBuilder().setPrettyPrinting().create().toJson(model)
                : "Body==> " + body);
    }

    public void startProgress(String message) {
//        mProgressHUD = ProgressHUD.show(baseActivity, defaultMessage, true, false, null);
        mProgressHUD = Utility.showLoadingDialog(baseActivity, message);
    }

    public void stopProgress() {
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            mProgressHUD.dismiss();
        }
    }




}