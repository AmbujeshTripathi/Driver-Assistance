package com.mygaadi.driverassistance.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.activity.MainActivity;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.model.Model;
import com.mygaadi.driverassistance.model.SubStatusListModel;
import com.mygaadi.driverassistance.retrofit.MyCallback;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;
import com.mygaadi.driverassistance.utils.Utility;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

import java.util.HashMap;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ambujesh on 2/8/2016.
 */
public class StatusUpdateFragment extends Fragment implements RestCallback, View.OnClickListener {

    public static final String TAG = "StatusUpdateFragment";
    private static final java.lang.String IS_PICK_UP = "isPickUpJob";
    private static final int CAPTURE_IMAGE = 101;
    private ViewGroup rootView;
    private LinearLayout linearLayoutStatus;
    private List<SubStatusListModel.SubStatusModel> subStatusList;
    private boolean isPickUpJob;
    private String mJobId;
    private String mCustomerMobile;
    private static int mCurrentIndex = 0;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.layout_status_update, container, false);

        ActionBar supportActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("UPDATE STATUS");
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            isPickUpJob = bundle.getBoolean(IS_PICK_UP);
        }
        setUpViews();
        getSubStatusListFromServer();
        setDataOnViews();

        return rootView;
    }

    private void getSubStatusListFromServer() {
        HashMap<String, String> params = new HashMap<>();
        RetrofitRequest.getSubStatus(params, new MyCallback<SubStatusListModel>(getActivity(), this, true, null, "",
                Constants.SERVICE_MODE.SUB_STATUS_LIST));
    }

    private void setDataOnViews() {

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        String jobType = bundle.getString(Constants.JOB_TYPE).trim();
        TextView tvPickUp = (TextView) rootView.findViewById(R.id.tvAddressPickup);
        TextView tvDropOff = (TextView) rootView.findViewById(R.id.tvAddressDropOff);
        String customerAddress = bundle.getString(Constants.CUSTOMER_ADDRESS);
        String hubAddress = bundle.getString(Constants.HUB_ADDRESS);
        if (jobType == "1") {
            tvPickUp.setText(customerAddress);
            tvDropOff.setText(hubAddress);
        } else {
            tvPickUp.setText(hubAddress);
            tvDropOff.setText(customerAddress);
        }


        String startTime = bundle.getString(Constants.START_TIME);
        ((TextView) rootView.findViewById(R.id.tvTime)).setText(startTime);

//        String endTime = bundle.getString(Constants.END_TIME);
        mJobId = bundle.getString(Constants.JOB_ID);
        mCustomerMobile = bundle.getString(Constants.KEY_MOBILE);
    }

    private void setUpViews() {
        linearLayoutStatus = (LinearLayout) rootView.findViewById(R.id.layoutStatus);
        rootView.findViewById(R.id.btnUpdateStatus).setOnClickListener(this);
    }


    @Override
    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode) {

    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {
        if (mode.equals(Constants.SERVICE_MODE.SUB_STATUS_LIST)) {
            if (model == null) {
                Snackbar.make(getView(), " Unable yo get List at this moment . Please try again later", Snackbar.LENGTH_SHORT).show();
                return;
            }

            SubStatusListModel subStatusListModel = (SubStatusListModel) model;
            if (!subStatusListModel.getStatus()) {
                Snackbar.make(getView(), subStatusListModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                return;
            }
            //TODO need to integrate the check to select any of two lists (Pick up or Drop)
            if (isPickUpJob) {
                subStatusList = subStatusListModel.getData().getPickUp();
            } else {
                subStatusList = subStatusListModel.getData().getDrop();
            }
            setStatusUpdateListOnViews(subStatusList);
            return;
        }
        if (mode.equals(Constants.SERVICE_MODE.UPDATE_STATUS)) {
            if (!(model instanceof Model)) {
                return;
            }
            Model responseModel = (Model) model;
            if (!responseModel.getStatus()) {
                Snackbar.make(getView(), responseModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                return;
            }
            Snackbar.make(getView(), responseModel.getMessage(), Snackbar.LENGTH_SHORT).show();
            mCurrentIndex = mCurrentIndex + 1;
            updateStatusListOnViews();
        }

    }

    private void updateStatusListOnViews() {
        int childCount = linearLayoutStatus.getChildCount();
        for (int i = 0; i < childCount && i < mCurrentIndex; i++) {
            ((ImageView) linearLayoutStatus.getChildAt(i).findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_done);
        }
        if (childCount - 1 >= mCurrentIndex) {
            ((ImageView) linearLayoutStatus.getChildAt(mCurrentIndex).findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_in_progress);
        }

        if (mCurrentIndex >= childCount - 1) {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void setStatusUpdateListOnViews(List<SubStatusListModel.SubStatusModel> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            View childView = LayoutInflater.from(getActivity()).inflate(R.layout.status_update_item, null);
            ((ImageView) childView.findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_open);
            ((TextView) childView.findViewById(R.id.tvStatusText)).setText(list.get(i).getSubStatusName());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 5, 0, 5);
            linearLayoutStatus.addView(childView, layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateStatus:
                showDialogDependOnCase();
                break;

            default:
                showDialogToUpdateStatus(v.getId());
                break;
        }
    }

    private void showDialogDependOnCase() {
        Log.d(TAG, "mCurrentIndex  =  " + mCurrentIndex);
        if ((subStatusList.size() - 1) < mCurrentIndex) {
            return;
        }
        SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(mCurrentIndex);
        String subStatusId = subStatusModel.getSubStatusId().trim();
        Log.d(TAG, "subStatusId = " + subStatusId);
        if (subStatusId.equals("7") || subStatusId.equals("13")) {
            showDialogToUploadImage(mCurrentIndex);
            return;
        }
        showDialogToUpdateStatus(mCurrentIndex);

    }

    private void showDialogToUpdateStatus(final int index) {
        if ((subStatusList.size() - 1) < index) {
            return;
        }
        SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(index);

        dialog = Utility.getDialog(getActivity(), R.layout.layout_dialog_with_message);
        View btnBack = dialog.findViewById(R.id.btnBack);

        ((TextView) dialog.findViewById(R.id.tvMessage)).setText(subStatusModel.getDialog());
        final EditText enterComment = (EditText) dialog.findViewById(R.id.enterComment);
        Button btSubmit = (Button) dialog.findViewById(R.id.btnSave);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateStatusOnServer(index, enterComment.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void updateStatusOnServer(int index, String comment) {
        SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(index);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.USER_ID));
        params.put(Constants.JOB_ID, mJobId);
        params.put(Constants.KEY_STATUS_ID, subStatusModel.getStatusId());
        params.put(Constants.KEY_SUB_STATUS_ID, subStatusModel.getSubStatusId());
        params.put(Constants.KEY_COMMENT, comment);
        params.put(Constants.KEY_DOC_ID, "");
        params.put(Constants.KEY_LATITUDE, "");
        params.put(Constants.KEY_LONGITUDE, "");
        RetrofitRequest.updateStatus(params, new MyCallback<Model>(getActivity(), this, true, null, "",
                Constants.SERVICE_MODE.UPDATE_STATUS));
    }


    private void showDialogToUploadImage(int index) {
        {
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constants.JOB_ID, mJobId);
            bundle.putSerializable(Constants.KEY_SUB_STATUS_MODEL, subStatusList.get(index));

            DialogFragment newFragment = new CaptureImageFragment();
            newFragment.setArguments(bundle);
            newFragment.setTargetFragment(this, CAPTURE_IMAGE);
            newFragment.show(ft, "dialog");
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_IMAGE:
                    boolean isUploadedSuccessfully = data.getBooleanExtra(CaptureImageFragment.IS_UPLOADED_SUCCESSFULLY, false);
                    if (isUploadedSuccessfully) {
                        mCurrentIndex = mCurrentIndex + 1;
                        updateStatusListOnViews();
                    }
                    break;
            }
        }
    }
}
