package com.mygaadi.driverassistance.retrofit;

import com.mygaadi.driverassistance.constants.Constants;

import retrofit.RetrofitError;
import retrofit.client.Response;

public interface RestCallback<T> {

    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode);
    public void onSuccess(T model, Response response, Constants.SERVICE_MODE mode);

}