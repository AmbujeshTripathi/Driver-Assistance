package com.mygaadi.driverassistance.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mygaadi.driverassistance.fragment.DashboardFragment;
import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.utils.Utility;

/**
 * Created by Ambujesh on 2/5/2016.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.navigateFragment(new DashboardFragment(), DashboardFragment.TAG, null, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
