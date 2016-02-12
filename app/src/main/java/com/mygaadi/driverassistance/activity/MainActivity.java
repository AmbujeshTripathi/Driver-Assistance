package com.mygaadi.driverassistance.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.fragments.CalendarFragment;
import com.mygaadi.driverassistance.fragments.DashboardFragment;
import com.mygaadi.driverassistance.fragments.StatusUpdateFragment;
import com.mygaadi.driverassistance.fragments.UploadJobCardFragment;
import com.mygaadi.driverassistance.services.GPSTrackerService;
import com.mygaadi.driverassistance.utils.Utility;

/**
 * Created by Ambujesh on 2/5/2016.
 */
public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MainActivity.class.getCanonicalName();
    public static String filename;
    private Boolean bounded;
    private GPSTrackerService mService;
    private boolean doubleBackToExitPressedOnce;
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        Intent serviceIntent = new Intent(this, GPSTrackerService.class);
        startService(serviceIntent);
        //bind service...
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        Utility.navigateFragment(new DashboardFragment(), DashboardFragment.TAG, null, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case GPSTrackerService.PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mService != null)
                        mService.permissionGrantedMoveOn();

                } else {
                    Utility.showToast(MainActivity.this, "GPS Permission denied!!!");
                    this.finish();
                }
                break;
            case DashboardFragment.PERMISSION_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Utility.showToast(MainActivity.this, "Permission denied!!!");
                    this.finish();
                }
                break;
        }
//
//
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == GPSTrackerService.PERMISSION_REQUEST_CODE) {
//            Utility.showToast(MainActivity.this, "GPS Permission granted!!!");
//            if (mService != null) {
//                mService.permissionGrantedMoveOn();
//            }
//        } else {
//            Utility.showToast(MainActivity.this, "GPS Permission denied!!!");
//            this.finish();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bounded == true && mService != null) {
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


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        if (backStackEntryCount <= 0) {
            super.onBackPressed();
            return;
        }

        /*
         * Below code has been written to pop back the previous fragment whenever back button has been pressed.
         * P.S. whenever a new fragment is added and we need to add that fragment here to pop-back to previous
         * fragment on onBackPressed.
		 */
        String backStackEntryName = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1).getName();
        if (backStackEntryName.equalsIgnoreCase(DashboardFragment.TAG)) {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Utility.showToast(this, "Press again to exit");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 10000);
            return;
        }

        if (backStackEntryName.equalsIgnoreCase(StatusUpdateFragment.TAG)) {
            Utility.CURRENT_JOB_ID = "";
            Utility.navigateFragment(new DashboardFragment(), DashboardFragment.TAG, null, this);
            return;
        }


        if (backStackEntryName.equalsIgnoreCase(UploadJobCardFragment.TAG)) {
            Utility.navigateFragment(new StatusUpdateFragment(), StatusUpdateFragment.TAG, null, this);
            return;
        }

        if (backStackEntryName.equalsIgnoreCase(CalendarFragment.TAG)) {
            Utility.navigateFragment(new DashboardFragment(), DashboardFragment.TAG, null, this);
            return;
        }
    }
}
