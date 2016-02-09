package com.mygaadi.driverassistance.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mygaadi.driverassistance.R;
import com.mygaadi.imageselectorlibrary.activity.ChooseImageActivity;
import com.mygaadi.imageselectorlibrary.crop.Crop;

/**
 * Created by Ambujesh on 2/9/2016.
 */
public class CaptureImageFragment extends DialogFragment implements View.OnClickListener {

    private static final int CAPTURE_IMAGE = 101;
    public static final String TAG = "CaptureImageFragment";
    private View rootView;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_capture_image, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rootView.findViewById(R.id.btnBack).setOnClickListener(this);
        rootView.findViewById(R.id.btnSave).setOnClickListener(this);


        imageView = (ImageView) rootView.findViewById(R.id.imgCapturedImage);
        imageView.setOnClickListener(this);

        return rootView;
    }

    private void openCamera(int requestCode) {
        Intent intent = new Intent(getActivity(), ChooseImageActivity.class);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = Crop.getOutput(data);
            switch (requestCode) {
                case CAPTURE_IMAGE:
                    Glide.with(this).load(imageUri.getPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//                    mRcImagePath = imageUri.getPath();
//                    uploadImageOnServer(imageUri.getPath().toString(), Constants.CaptureLeadImagesTYPE.RC_COPY,1,mProgressDocumentCopy,mRCcameraIcon,mRCuploadSuccess,mRCretryIcon,mRCdelete,GlobalVariables.SERVICE_MODE.CAPTURE_LEAD_UPLOAD_RC_IMAGES);
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCapturedImage:
                openCamera(CAPTURE_IMAGE);
                break;

            case R.id.btnBack:
                dismiss();
                break;

            case R.id.btnSave:
                Intent intent = new Intent();
                intent.putExtra("Hell", "hell no");
                getTargetFragment().onActivityResult(CAPTURE_IMAGE, Activity.RESULT_OK, intent);
                dismiss();
                break;
        }
    }
}
