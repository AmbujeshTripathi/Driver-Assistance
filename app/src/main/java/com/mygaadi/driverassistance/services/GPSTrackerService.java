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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.mygaadi.driverassistance.providers.DriverContentProvider;
import com.mygaadi.driverassistance.utils.Utility;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

/**
 * Created by Aditya on 2/8/2016.
 */
public class GPSTrackerService extends Service {

    private static final String TAG = GPSTrackerService.class.getCanonicalName();
    public static final int PERMISSION_REQUEST_CODE = 234;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    Thread triggerService;
    private Activity activity;
    private LocationManager locationManager;
    private CustomLocationListener mCustomLocationListener;
    private boolean isGPSEnabled;
    private AlertDialog alertDialog;
    private static String latitude;
    private static Location location;
    private static String longitude;

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
    public IBinder onBind(Intent intent) {
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        registerReceiver(mBroadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(mBroadcastReceiver);
        return super.onUnbind(intent);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                if (displayGpsStatus()) {
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    fetchLocationThread();
                } else {
                    removeGpsListener();
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    alertBox(activity, "GPS is OFF");
                }
            }
        }

    };

    public void removeGpsListener() {
        try {
            locationManager.removeUpdates(mCustomLocationListener);
            Log.v(TAG, "GPS tracking off!!!");
        } catch (Exception ex) {
            Log.v(TAG, "Exception in GPSService --- " + ex);
        }
    }

    private void fetchLocationThread() {
        triggerService = new Thread(new Runnable() {
            public void run() {
                try {
                    Looper.prepare();//Initialize the current thread as a looper.
                    mCustomLocationListener = new CustomLocationListener();
                    long minTime = 1000; // 60 seconds
                    float minDistance = 0; // distance in meters
//                    Criteria criteria = new Criteria();
//                    criteria.setAccuracy(Criteria.ACCURACY_HIGH);
//                    String bestProvider = locationManager.getBestProvider(criteria, false);
                    if (Utility.isNetworkAvailable(activity)) {
                        Log.v(TAG, "Using Network provider");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime,
                                minDistance, mCustomLocationListener);
                    } else {
                        Log.v(TAG, "Using GPS provider");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
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
                if (alertDialog != null && !alertDialog.isShowing()) {
                    alertDialog.show();
                } else {
                    alertBox(activity, "GPS is OFF");
                }
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

    private void saveLocationToDatabase(Location location) {
        String longitude = "Longitude: " + location.getLongitude();
        String latitude = "Latitude: " + location.getLatitude();
        Log.v(TAG, longitude + " " + latitude);
        GPSTrackerService.latitude = latitude;
        GPSTrackerService.longitude = latitude;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DriverLocationEntry.DRIVER_ID, UtilitySingleton.getInstance(activity).getStringFromSharedPref(Constants.USER_ID));
        values.put(DriverLocationEntry.LATITUDE, latitude);
        values.put(DriverLocationEntry.LONGITUDE, longitude);
        values.put(DriverLocationEntry.CREATED_AT, longitude);

        getContentResolver().insert(DriverContentProvider.ALL_LOCATION_URI, values);
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
        Utility.showToast(activity, "Service Stopped");
    }
}
