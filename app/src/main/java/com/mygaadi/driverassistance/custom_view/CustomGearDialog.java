package com.mygaadi.driverassistance.custom_view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygaadi.driverassistance.R;


//Implementation of custom progress dialog
public class CustomGearDialog extends ProgressDialog implements Animation.AnimationListener {

    private ImageView mImageView[];
    private Animation mRotateAnimation[];
    private TextView mMessageView;
    private Context mContext;
    private String mMessage;

    public CustomGearDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.loading_animation_gear);

    }

    public void onWindowFocusChanged(boolean hasFocus){
        mImageView = new ImageView[3];
        mRotateAnimation = new Animation[3];
        mImageView[0] = (ImageView) findViewById(R.id.gear1);
        mImageView[1] = (ImageView) findViewById(R.id.gear2);
        mImageView[2] = (ImageView) findViewById(R.id.gear3);
        mMessageView = (TextView) findViewById(R.id.textGearProgressDialog);
        //Set message to the view
        if(mMessage!= null){
            mMessageView.setText(mMessage);
        }

        mRotateAnimation[0] = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
        mRotateAnimation[1] = AnimationUtils.loadAnimation(mContext, R.anim.rotate_anti);
        mRotateAnimation[2] = AnimationUtils.loadAnimation(mContext, R.anim.rotate_anti);

        for (int i = 0; i < 3; i++) {
            mRotateAnimation[i].setAnimationListener(this);
            mImageView[i].startAnimation(mRotateAnimation[i]);
        }
    }

    public void setMessage(String text){
        mMessage = text;
        if(mMessageView!= null){
            mMessageView.setText(text);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

