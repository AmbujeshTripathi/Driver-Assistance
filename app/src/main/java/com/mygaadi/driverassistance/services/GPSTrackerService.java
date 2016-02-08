package com.mygaadi.driverassistance.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.mygaadi.driverassistance.utils.Utility;

/**
 * Created by Aditya on 2/8/2016.
 */
public class GPSTrackerService extends Service {

    String GPS_FILTER = "";
    Thread triggerService;
    LocationManager lm;
    GpsListener gpsLocationListener;
    boolean isRunning = true;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        GPS_FILTER = "GPS_FILTER";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        triggerService = new Thread(new Runnable() {
            public void run() {
                try {
                    Looper.prepare();//Initialize the current thread as a looper.
                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    gpsLocationListener = new GpsListener();
                    long minTime = 30000; // 5 sec...
                    float minDistance = 10; // distance in meters

                    //check if internet available or not...
                    if (Utility.isNetworkAvailable(getApplicationContext())) {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
                                minDistance, gpsLocationListener);
                    } else {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
                                minDistance, gpsLocationListener);
                    }
                    Looper.loop();
                } catch (Exception ex) {
                    Utility.showToast(getApplicationContext(), "Exception in triggerService Thread");
                    System.out.println("Exception in triggerService Thread -- " + ex);
                }
            }
        }, "myLocationThread");
        triggerService.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        removeGpsListener();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void removeGpsListener() {
        try {
            lm.removeUpdates(gpsLocationListener);
        } catch (Exception ex) {
            Utility.showToast(getApplicationContext(), "Exception in GPSService");
            System.out.println("Exception in GPSService --- " + ex);
        }
    }

    class GpsListener implements LocationListener {

        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            float speed = location.getSpeed();
            Log.v("Aditya Fetch : ", "Latitude : " + latitude + " Longitude : " + longitude + " Speed : " + speed);
            Intent positionIntent = new Intent(GPS_FILTER);
            positionIntent.putExtra("latitude", latitude);
            positionIntent.putExtra("longitude", longitude);
            positionIntent.putExtra("speed", speed);
            sendBroadcast(positionIntent);
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

    }

}