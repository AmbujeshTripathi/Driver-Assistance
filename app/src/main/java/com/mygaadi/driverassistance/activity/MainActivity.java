package com.mygaadi.driverassistance.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.fragments.DashboardFragment;
import com.mygaadi.driverassistance.fragments.StatusUpdateFragment;
import com.mygaadi.driverassistance.services.GPSTrackerService;
import com.mygaadi.driverassistance.utils.Utility;

/**
 * Created by Ambujesh on 2/5/2016.
 */
public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private Boolean bounded;
    private GPSTrackerService mService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind service...
        bindService(new Intent(this, GPSTrackerService.class), mServiceConnection, Context.BIND_AUTO_CREATE);

        Utility.navigateFragment(new DashboardFragment(), DashboardFragment.TAG, null, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == GPSTrackerService.PERMISSION_REQUEST_CODE) {
            Utility.showToast(MainActivity.this, "GPS Permission granted!!!");
            if (mService != null) {
                mService.permissionGrantedMoveOn();
            }
        } else {
            Utility.showToast(MainActivity.this, "GPS Permission denied!!!");
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bounded == true && mService != null) {
            mService.removeGpsListener();
            unbindService(mServiceConnection);
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GPSTrackerService.LocalBinder binder = (GPSTrackerService.LocalBinder) service;
            mService = (GPSTrackerService) binder.getService();
            bounded = true;
            mService.startLocationFetching(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bounded = false;
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
    }

}
