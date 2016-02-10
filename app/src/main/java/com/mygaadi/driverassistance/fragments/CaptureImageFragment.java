package com.mygaadi.driverassistance.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.model.Model;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;
import com.mygaadi.driverassistance.retrofit.UploadCallback;
import com.mygaadi.driverassistance.utils.UtilitySingleton;
import com.mygaadi.imageselectorlibrary.activity.ChooseImageActivity;
import com.mygaadi.imageselectorlibrary.crop.Crop;

import java.io.File;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ambujesh on 2/9/2016.
 */
public class CaptureImageFragment extends DialogFragment implements View.OnClickListener, RestCallback {

    private static final int CAPTURE_IMAGE = 101;
    public static final String TAG = "CaptureImageFragment";
    public static final String IS_UPLOADED_SUCCESSFULLY = "isImageUploadedSuccessfully";
    private View rootView;
    private String mImagePath;
    private ImageView mRCcapturedImage;
    private ImageView mRCcameraIcon;
    private ImageView mRCretryIcon;
    private ProgressBar mProgressDocumentCopy;
    private ImageView mRCuploadSuccess;
    private ImageView mRCdelete;
    private boolean isPolicyImageUploaded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_capture_image, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rootView.findViewById(R.id.btnSave).setOnClickListener(this);


        mRCcapturedImage = (ImageView) rootView.findViewById(R.id.imgCapturedImage);
        mRCcameraIcon = (ImageView) rootView.findViewById(R.id.imgCameraIcon);
        mRCretryIcon = (ImageView) rootView.findViewById(R.id.imgRetryIcon);
        mProgressDocumentCopy = (ProgressBar) rootView.findViewById(R.id.progressDocumentCopy);
        mRCuploadSuccess = (ImageView) rootView.findViewById(R.id.imgUploadSuccess);
        mRCdelete = (ImageView) rootView.findViewById(R.id.imgRCDelete);

        mRCcapturedImage.setOnClickListener(this);
        mRCcameraIcon.setOnClickListener(this);
        mRCretryIcon.setOnClickListener(this);
        mRCuploadSuccess.setOnClickListener(this);
        mRCdelete.setOnClickListener(this);


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
                    mImagePath = imageUri.getPath().toString();
                    Glide.with(this).load(mImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).into(mRCcapturedImage);
                    uploadImageOnServer(mImagePath, mProgressDocumentCopy, mRCcameraIcon, mRCuploadSuccess, mRCretryIcon, mRCdelete, Constants.SERVICE_MODE.UPLOAD_JOB_CARD);
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


            case R.id.btnSave:
                Intent intent = new Intent();
                intent.putExtra(IS_UPLOADED_SUCCESSFULLY, isPolicyImageUploaded);
                getTargetFragment().onActivityResult(CAPTURE_IMAGE, Activity.RESULT_OK, intent);
                dismiss();
                break;

            case R.id.imgCameraIcon:
                openCamera(CAPTURE_IMAGE);
                break;
            case R.id.imgRetryIcon:
                uploadImageOnServer(mImagePath, mProgressDocumentCopy, mRCcameraIcon, mRCuploadSuccess, mRCretryIcon, mRCdelete, Constants.SERVICE_MODE.UPLOAD_JOB_CARD);
                break;
            case R.id.imgUploadSuccess:
                break;
            case R.id.imgRCDelete:
                mRCcapturedImage.setImageResource(R.color.transparent);
                mRCdelete.setVisibility(View.GONE);
                mRCuploadSuccess.setVisibility(View.GONE);
                mRCretryIcon.setVisibility(View.GONE);
                mRCcameraIcon.setVisibility(View.VISIBLE);
                isPolicyImageUploaded = false;
                break;
        }
    }


    /**
     * Method to make the retrofit request for uploading the given image
     *
     * @param imagePath      Memory location of the image.
     * @param progressBar    progress bar to show on uploading the images
     * @param cameraIconView camera icon view
     * @param successView    view that need to be visible on the success of the image upload
     * @param retryView      view that needs to be visible for make retry to upload the image
     */
    private void uploadImageOnServer(String imagePath, ProgressBar progressBar,
                                     View cameraIconView, View successView, View retryView, View deleteView, Constants.SERVICE_MODE mode) {
        cameraIconView.setVisibility(View.GONE);
        //create a new file from the URI
        File fileToUpload = new File(imagePath);
        //if file not existed
        if (!fileToUpload.exists()) {
//            Utility.makeToast(getActivity(), "File does not exists. File Path : " + imagePath);
        }
        //upload the file to server
        else {
            Bundle bundle = getArguments();
            if (bundle == null) {
                Snackbar.make(getView(), "Unable to upload job card at this moment.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String jobId = bundle.getString(Constants.JOB_ID);
            String userId = UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.USER_ID);
            RetrofitRequest.uploadJobCard(new File(mImagePath), jobId, "doc", userId, new UploadCallback<Model>(this, mode,
                    progressBar, successView, retryView, deleteView));
        }
    }

    @Override
    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode) {

    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {

        if (mode == Constants.SERVICE_MODE.UPLOAD_JOB_CARD) {
            isPolicyImageUploaded = true;
        }
    }
}
