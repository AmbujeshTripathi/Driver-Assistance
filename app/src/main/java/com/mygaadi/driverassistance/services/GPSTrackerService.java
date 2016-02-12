package com.mygaadi.driverassistance.services;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mygaadi.driverassistance.DBPackage.DatabaseVar.DriverLocationEntry;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.model.Model;
import com.mygaadi.driverassistance.providers.DriverContentProvider;
import com.mygaadi.driverassistance.retrofit.MyCallback;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;
import com.mygaadi.driverassistance.utils.Utility;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Aditya on 2/8/2016.
 */
public class GPSTrackerService extends Service implements RestCallback {

    private static final String TAG = GPSTrackerService.class.getCanonicalName();
    public static final int PERMISSION_REQUEST_CODE = 234;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    Thread triggerService;
    private boolean isBounded;
    private static final long MIN_TIME_INTERVAL = 1000 * 60 * 5; // 15 minutes for data send
    private Activity activity;
    private LocationManager locationManager;
    private CustomLocationListener mCustomLocationListener;
    private boolean isGPSEnabled;
    private boolean isDBCleared = true;
    private AlertDialog alertDialog;
    public static Location location;

    public void permissionGrantedMoveOn() {
        if (displayGpsStatus()) {
            fetchLocationThread();
        } else {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            alertBox(activity, "GPS is OFF");
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public GPSTrackerService getService() {
            return GPSTrackerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        isBounded = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "onUnbind");
        isBounded = false;
        return super.onUnbind(intent);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Broadcast received with action = " + intent.getAction());
            if (intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                if (displayGpsStatus()) {
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    fetchLocationThread();
                } else {
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    if (getApplicationContext() != null && isBounded == true)
                        alertBox(activity, "GPS is OFF");
                }
            } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (Utility.isNetworkAvailable(context)) {
                    Log.v(TAG, "Internet Available");
                    if (!isDBCleared) {
                        fetchLatLongFromDBAndUpload();
                    }
                }
            }
        }
    };

    public void removeGpsListener() {
        try {
            locationManager.removeUpdates(mCustomLocationListener);
            Log.v(TAG, "Location tracking off!!!");
        } catch (Exception ex) {
            Log.v(TAG, "Exception in GPSService --- " + ex);
        }
    }

    private void fetchLocationThread() {
        Log.v(TAG, "fetchLocationThread");
        triggerService = new Thread(new Runnable() {
            public void run() {
                try {
                    Looper.prepare();//Initialize the current thread as a looper.
                    mCustomLocationListener = new CustomLocationListener();
//                    TODO  - Need to change min time to MIN_INTERVAL_TIME
                    long minTime = 1000 * 60; // 60 seconds
                    float minDistance = 0; // distance in meters
//                    Criteria criteria = new Criteria();
//                    criteria.setAccuracy(Criteria.ACCURACY_HIGH);
//                    String bestProvider = locationManager.getBestProvider(criteria, false);
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        Log.v(TAG, "Using Network provider");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_INTERVAL,
                                minDistance, mCustomLocationListener);
                    } else {
                        Log.v(TAG, "Using GPS provider");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL,
                                minDistance, mCustomLocationListener);
                    }
//                    locationManager.requestLocationUpdates(bestProvider, 0, 1, mCustomLocationListener);
                    Looper.loop();
                } catch (Exception ex) {
                    Utility.showToast(getApplicationContext(), "Exception in triggerService Thread");
                }
            }
        }, "myLocationThread");
        triggerService.start();
    }

    private boolean checkAndAskForGPSPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


    public void startLocationFetching(Activity activity) {
        this.activity = activity;
        if (checkAndAskForGPSPermissions()) {
            isGPSEnabled = displayGpsStatus();
            if (isGPSEnabled) {
                fetchLocationThread();
            } else {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                alertBox(activity, "GPS is OFF");
            }
        }
    }

    public void alertBox(final Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                ((Activity) context).startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ((Activity) context).finish();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private Boolean displayGpsStatus() {
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }

    private void uploadLatLongData(String[] latitudeArray, String[] longitudeArray, String createdAt[], String jobId[]) {
        Log.v(TAG, "uploadLatLongData");
        JSONArray dataJSONArray = new JSONArray();

        for (int i = 0; i < latitudeArray.length; i++) {
            JSONObject sIdObject = new JSONObject();
            try {
                sIdObject.put("jobId", jobId[i]);
                sIdObject.put("latitude", latitudeArray[i]);
                sIdObject.put("longitude", longitudeArray[i]);
                sIdObject.put("created", createdAt[i]);
                sIdObject.put("send", Utility.getCurrentTimeIST());
                dataJSONArray.put(sIdObject);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
        }

        String dataString = dataJSONArray.toString();
        HashMap<String, String> mParams = new HashMap<String, String>();
        mParams.put(Constants.USER_ID, UtilitySingleton.getInstance(activity).getStringFromSharedPref(Constants.USER_ID));
        mParams.put("data", dataString);
        RetrofitRequest.sendLatLongs(mParams, new MyCallback<Model>(GPSTrackerService.this, this, false, null, null,
                Constants.SERVICE_MODE.SEND_LAT_LONGS));
        Log.v(TAG, "Retrofit call with locations = " + latitudeArray.length + " in Number");
        Log.v(TAG, "Retrofit call with locations data = " + dataString);
    }

    @Override
    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode) {
        Log.v(TAG, "Location sending Failure()" + mode);
    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {
        if (mode.equals(Constants.SERVICE_MODE.SEND_LAT_LONGS)) {
            if (!(model instanceof Model)) {
                return;
            }
            Model responseModel = (Model) model;
            if (!responseModel.getStatus()) {
                Log.v(TAG, "Lat Longs not uploaded successfully");
                return;
            } else {
                Log.v(TAG, "Lat Longs uploaded successfully");
            }
            //Lat Longs uploaded successfully now clear database...
            if (isDBCleared == false) {
                clearLatLongsFromDB();
            }
        }
    }

    private void clearLatLongsFromDB() {
        Log.v(TAG, "clearLatLongsFromDB");
        int rowDeleted;
        String filter = DriverLocationEntry.DRIVER_ID + "=?";
        String[] filterArgs = new String[]{UtilitySingleton.getInstance(activity).getStringFromSharedPref(Constants.USER_ID)};
        rowDeleted = getContentResolver().delete(DriverContentProvider.ALL_LOCATION_URI, filter, filterArgs);
        isDBCleared = true;
        Log.v(TAG, "database cleared for" + rowDeleted);
    }

    private void saveLocationToDatabase(Location location) {
        String longitude = location.getLongitude() + "";
        String latitude = location.getLatitude() + "";
        Log.v(TAG, longitude + " " + latitude);
        GPSTrackerService.location = location;
        Log.v(TAG, "saveLocationToDatabase latitude " + latitude + " longitude " + longitude + " isDBCleared " + isDBCleared);
        if (Utility.isNetworkAvailable(activity)) {
            if (isDBCleared) {
                //upload single location information...
                String latitudeArray[] = {latitude};
                String longitudeArray[] = {longitude};
                String createdAt[] = {Utility.getCurrentTimeIST()};
                String jobId[] = {Utility.CURRENT_JOB_ID};
                uploadLatLongData(latitudeArray, longitudeArray, createdAt, jobId);
            } else {
                //fetch location details from database and upload...
                saveLatLongEntry(latitude, longitude);
                fetchLatLongFromDBAndUpload();
            }
        } else {
            // Save data to database...
            saveLatLongEntry(latitude, longitude);
        }
    }

    private void saveLatLongEntry(String latitude, String longitude) {
        Log.v(TAG, "saveLatLongEntry single");
        ContentValues values = new ContentValues();
        values.put(DriverLocationEntry.DRIVER_ID, UtilitySingleton.getInstance(activity).getStringFromSharedPref(Constants.USER_ID));
        values.put(DriverLocationEntry.JOB_ID, Utility.CURRENT_JOB_ID);
        values.put(DriverLocationEntry.LATITUDE, latitude);
        values.put(DriverLocationEntry.LONGITUDE, longitude);
        values.put(DriverLocationEntry.CREATED_AT, Utility.getCurrentTimeIST());
        getContentResolver().insert(DriverContentProvider.ALL_LOCATION_URI, values);
        isDBCleared = false;
    }

    private void fetchLatLongFromDBAndUpload() {
        Log.v(TAG, "fetchLatLongFromDBAndUpload");
        Cursor cursor = getContentResolver().query(DriverContentProvider.ALL_LOCATION_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            String latitude[] = new String[cursor.getCount()];
            String longitude[] = new String[cursor.getCount()];
            String createdAt[] = new String[cursor.getCount()];
            String jobId[] = new String[cursor.getCount()];

            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()) {
                latitude[i] = cursor.getString(cursor.getColumnIndex(DriverLocationEntry.LATITUDE));
                longitude[i] = cursor.getString(cursor.getColumnIndex(DriverLocationEntry.LONGITUDE));
                createdAt[i] = cursor.getString(cursor.getColumnIndex(DriverLocationEntry.CREATED_AT));
                jobId[i] = cursor.getString(cursor.getColumnIndex(DriverLocationEntry.JOB_ID));
                i++;
                cursor.moveToNext();
            }
            //make a retrofit call to upload lat-long data...
            uploadLatLongData(latitude, longitude, createdAt, jobId);
        }
    }

    private class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //Save to location to database...
            saveLocationToDatabase(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        unregisterReceiver(mBroadcastReceiver);
        removeGpsListener();
        Utility.showToast(activity, "Service Stopped");
    }
}
