package com.mygaadi.driverassistance.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

import java.util.Timer;
import java.util.TimerTask;

import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Ambujesh on 6/19/2015.
 */
public class SplashActivity extends AppCompatActivity implements RestCallback {
    private ImageView splashImage;
    private AnimationDrawable anim;
    private Handler handler;
    private Runnable r;
    private int splashMipMaps[] = new int[]{R.mipmap.splash_frame_01, R.mipmap.splash_frame_02, R.mipmap.splash_frame_03,
            R.mipmap.splash_frame_04, R.mipmap.splash_frame_05, R.mipmap.splash_frame_06,
            R.mipmap.splash_frame_07, R.mipmap.splash_frame_08, R.mipmap.splash_frame_09,
            R.mipmap.splash_frame_10, R.mipmap.splash_frame_11, R.mipmap.splash_frame_12,
            R.mipmap.splash_frame_13, R.mipmap.splash_frame_14, R.mipmap.splash_frame_15,
            R.mipmap.splash_frame_16, R.mipmap.splash_frame_17};

    private Handler mHandler;
    private TimerTask time;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        initViewAndAnimate();
    }

    private void initViewAndAnimate() {
        splashImage = (ImageView) findViewById(R.id.imageview_splash);
        launchMainActivity();
    }

    public void launchMainActivity() {
        try {

            timer = new Timer();
            time = new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < splashMipMaps.length; i++) {
                        final int drawableId = splashMipMaps[i];
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                splashImage.setImageResource(drawableId);
                            }
                        });
                        if (i == splashMipMaps.length - 1) {
                            i = 0;
                        }
                    }
                }
            };
            timer.schedule(time, 0);
            r = new Runnable() {
                @Override
                public void run() {
                    String userId = UtilitySingleton.getInstance(getApplicationContext()).getStringFromSharedPref(Constants.USER_ID);
                    if (userId == null || userId.equals("")) {
                        startActivity(new Intent(SplashActivity.this, ActivityRegistration.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                }
            };
            mHandler.postDelayed(r, 3400);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != timer) {
            timer.cancel();
        }
        if (null != time) {
            time.cancel();
        }
    }

    @Override
    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode) {

    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {

    }
}




