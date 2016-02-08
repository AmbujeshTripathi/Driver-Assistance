package com.mygaadi.driverassistance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.fragments.StatusUpdateFragment;
import com.mygaadi.driverassistance.utils.Utility;

/**
 * Created by Ambujesh on 2/5/2016.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.navigateFragment(new StatusUpdateFragment() , StatusUpdateFragment.TAG , null , this);
    }
}
