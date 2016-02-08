package com.mygaadi.driverassistance;

import android.app.Application;
import android.content.Context;

import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.utils.FontOverrideUtils;
import com.mygaadi.driverassistance.utils.UtilitySingleton;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;


public class MyApplication extends Application {
    private static MyApplication instance;
    private static RestAdapter restAdapter;


    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //    if (!BuildConfig.IS_DEV_BUILD) //todo uncomment this in future.
        instance = this;
        //Override application Font with roboto
        overrideFont();

    }



    /**
     * Method to replace the current fonts of the application with the Roboto-Medium.ttf
     */
    private void overrideFont() {
        //Overriding the fonts using the reflection
        FontOverrideUtils.setDefaultFont(this, "DEFAULT", "Roboto-Regular.ttf");
        FontOverrideUtils.setDefaultFont(this, "MONOSPACE", "Roboto-Light.ttf");
        FontOverrideUtils.setDefaultFont(this, "SERIF", "Roboto-Medium.ttf");
        FontOverrideUtils.setDefaultFont(this, "SANS", "Roboto-Regular.ttf");
    }


    public static RestAdapter getRestAdapter() {
        return (restAdapter == null) ? setRestAdaptor() : restAdapter;
    }

    public static RestAdapter setRestAdaptor() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("x-auth-key ", "lms_service");
                request.addHeader("x-auth-apitoken", "wxThLeZoBXmKIDLeGsNeuuZwkhQqhGsXfI7ID5D1I1A=");

                //Adding required headers. If the value found then put the values other wise set with blank string
                Context context = MyApplication.getInstance().getApplicationContext();
                request.addHeader("x-auth-userid", UtilitySingleton.getInstance(context).getStringFromSharedPref(Constants.USERID, ""));
                request.addHeader("x-auth-usertoken", UtilitySingleton.getInstance(context).getStringFromSharedPref(Constants.USER_TOKEN, ""));
            }
        };

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1, TimeUnit.MINUTES);
        okHttpClient.setConnectTimeout(1, TimeUnit.MINUTES);

        restAdapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL_CONSTANT).setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL).setClient(new OkClient(okHttpClient)).build();

        return restAdapter;
    }




}
