package com.mygaadi.driverassistance.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
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
    private BitmapDrawable frames[] = new BitmapDrawable[17];

    private Handler mHandler;
    private TimerTask time;
    private Timer timer;
    ImageView carImage;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        initViewAndAnimate();

        //Creating the application account in the device and register for the sync adapter
      //  SyncUtils.CreateSyncAccount(this);

    }

    private void initViewAndAnimate() {
        splashImage = (ImageView) findViewById(R.id.imageview_splash);
        launchMainActivity();
      /*  carImage = (ImageView)findViewById(R.id.imageview_car);
        animation = AnimationUtils.loadAnimation(this, R.anim.movecar);
        carImage.setVisibility(View.VISIBLE);
        carImage.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
              //  carImage.setImageResource(R.drawable.carwithlight);
                launchMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
        /*try {

            timer = new Timer();
            time = new TimerTask() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for(int i = 0 ; i < splashMipMaps.length; i++)
                    {
                        final int drawableId =  splashMipMaps[i];
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
                        if(i == splashMipMaps.length-1) {
                            i=0;
                        }
                    }
                }
            };
            timer.schedule(time,0);*/

            /*for(int i = 0 ; i < splashMipMaps.length; i++)
            {
                frames[i] =  (BitmapDrawable) getResources().getDrawable(splashMipMaps[i]);
            }

            for(int i = 0 ; i < frames.length; i++)
            {
                anim = new AnimationDrawable();
                anim.addFrame(frames[i], 100);
            }

            anim.setOneShot(false);
            iv1.setBackgroundDrawable(anim);
            anim.start();

            // for recycling the bitmap(clear the cache)
            for(int i = 0 ; i < frames.length; i++)
            {
                frames[i] = null;
            }*/

//            BitmapDrawable frame1 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_17);
//            BitmapDrawable frame2 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_02);
//            BitmapDrawable frame3 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_03);
//            BitmapDrawable frame4 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_04);
//            BitmapDrawable frame5 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_05);
//            BitmapDrawable frame6 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_06);
//            BitmapDrawable frame7 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_07);
//            BitmapDrawable frame8 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_08);
//            BitmapDrawable frame9 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_09);
//            BitmapDrawable frame10 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_10);
//            BitmapDrawable frame11 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_11);
//
//            BitmapDrawable frame12 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_12);
//            BitmapDrawable frame13 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_13);
//
//            BitmapDrawable frame14 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_14);
//            BitmapDrawable frame15 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_15);
//            BitmapDrawable frame16 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_16);
//
//            BitmapDrawable frame17 = (BitmapDrawable) getResources()
//                    .getDrawable(R.mipmap.splash_frame_17);
//
//            anim = new AnimationDrawable();
//
//            anim.addFrame(frame17, 100);
//            anim.addFrame(frame1, 100);
//            anim.addFrame(frame2, 100);
//            anim.addFrame(frame3, 100);
//            anim.addFrame(frame4, 100);
//            anim.addFrame(frame5, 100);
//            anim.addFrame(frame6, 100);
//            anim.addFrame(frame7, 100);
//            anim.addFrame(frame8, 100);
//
//            anim.addFrame(frame9, 100);
//            anim.addFrame(frame10, 100);
//            anim.addFrame(frame11, 100);
//
//            anim.addFrame(frame12, 100);
//            anim.addFrame(frame13, 100);
//            anim.addFrame(frame14, 100);
//            anim.addFrame(frame15, 100);
//            anim.addFrame(frame16, 100);
//
//            anim.setOneShot(false);
//            iv1.setBackgroundDrawable(anim);
//            anim.start();
//
////             for recycling the bitmap(clear the cache)
//            frame1 = null;
//            frame2 = null;
//            frame3 = null;
//            frame4 = null;
//            frame5 = null;
//            frame6 = null;
//            frame7 = null;
//            frame8 = null;
//            frame9 = null;
//            frame10 = null;
//            frame11 = null;
//            frame12 = null;
//            frame13 = null;
//            frame14 = null;
//            frame15 = null;
//            frame16 = null;
//            frame17 = null;

           /* r = new Runnable() {
                @Override
                public void run() {
                    if(UtilitySingleton.getInstance(getApplicationContext()).getStringFromSharedPref(Constants.USER_ID) == null) {
                        startActivity(new Intent(SplashActivity.this, ActivityRegistration.class));
                    }
                    else{
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                }
            };
            mHandler.postDelayed(r, 3400);*/

       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
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
                        startActivity(new Intent(SplashActivity.this, ActivityRegistration.class));
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




