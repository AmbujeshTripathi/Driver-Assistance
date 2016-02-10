package com.mygaadi.driverassistance.retrofit;

import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.GsonBuilder;
import com.mygaadi.driverassistance.MyApplication;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.model.Model;
import com.mygaadi.driverassistance.utils.Utility;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Class to handle the call back for the image upload
 *
 * @param <T>
 */
public class UploadCallback<T> implements Callback<T> {

    private final Constants.SERVICE_MODE mode;
    private RestCallback restCallback;
    private ProgressBar mProgressBar;
    private View mSuccessView;
    private View mRetryView;
    private View mDeleteView;

    /**
     * Description : Callback with custom message
     */
    @SuppressWarnings("unchecked")
    public UploadCallback(RestCallback restCallback, Constants.SERVICE_MODE mode,
                          ProgressBar progressBar, View successView, View retryView, View deleteView) {
        this.restCallback = restCallback;
        this.mode = mode;

        this.mProgressBar = progressBar;
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        this.mSuccessView = successView;
        if (successView != null) {
            successView.setVisibility(View.GONE);
        }

        this.mRetryView = retryView;
        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }

        this.mDeleteView = deleteView;

        if (deleteView != null) {
            deleteView.setVisibility(View.GONE);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        performOnFailureTask();

        if (error.getKind() == RetrofitError.Kind.NETWORK) {
            Utility.makeToast(MyApplication.getAppContext(), "Network no available !!");
        }
        if (restCallback != null) {
            restCallback.onFailure(error, mode);
        }

    }

    private void performOnFailureTask() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mSuccessView != null) {
            mSuccessView.setVisibility(View.GONE);
        }
        if (mRetryView != null) {
            mRetryView.setVisibility(View.VISIBLE);
        }
        if (mDeleteView != null) {
            mDeleteView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void success(T model, Response arg1) {
        showLog(model, arg1);

        if (arg1 != null && Utility.getResponse(arg1) != null) {
            //for checking the status
            String body = Utility.getResponse(arg1);
            Model commonModel = new GsonBuilder().setPrettyPrinting().create().fromJson(body, Model.class);
            if (commonModel != null && commonModel.getStatus()) {
                performOnSuccessTask();
            } else {
                performOnFailureTask();
            }
        } else {
            performOnFailureTask();
        }
        //making rest callback
        if (restCallback != null) {
            restCallback.onSuccess(model, arg1, mode);
        }
    }

    private void performOnSuccessTask() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mRetryView != null) {
            mRetryView.setVisibility(View.GONE);
        }
        if (mSuccessView != null) {
            mSuccessView.setVisibility(View.VISIBLE);
        }
        if (mDeleteView != null) {
            mDeleteView.setVisibility(View.VISIBLE);
        }

    }

    private void showLog(T model, Response arg1) {
        String body = Utility.getResponse(arg1);
        RestLog.l(this, "URL==> " + arg1.getUrl());
        RestLog.l(this, (body.equalsIgnoreCase("null")) ? "Model==> " + new GsonBuilder().setPrettyPrinting().create().toJson(model)
                : "Body==> " + body);
    }
}