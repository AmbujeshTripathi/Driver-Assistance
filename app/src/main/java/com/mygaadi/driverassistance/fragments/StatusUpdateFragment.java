package com.mygaadi.driverassistance.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.mygaadi.driverassistance.services.GPSTrackerService;
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
    private static final int CAPTURE_IMAGE = 101;
    private ViewGroup rootView;
    private LinearLayout linearLayoutStatus;
    private List<SubStatusListModel.SubStatusModel> subStatusList;
    private boolean isPickUpJob;
    private String mJobId;
    private String mCustomerMobile;
    private static int mCurrentIndex = 0;
    private Dialog dialog;
    private String mSubStatusId;

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
            supportActionBar.setIcon(R.drawable.launcher_icon);
            supportActionBar.setTitle("UPDATE STATUS");
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
        mSubStatusId = bundle.getString(Constants.KEY_SUB_STATUS_ID);
        if (jobType.equals("1")) {
            isPickUpJob = true;
            tvPickUp.setText(customerAddress);
            tvDropOff.setText(hubAddress);
        } else {
            isPickUpJob = false;
            tvPickUp.setText(hubAddress);
            tvDropOff.setText(customerAddress);
        }


        String startTime = bundle.getString(Constants.START_TIME);
        ((TextView) rootView.findViewById(R.id.tvTime)).setText(Utility.getDateTimeInRequiredFormat(startTime));

//        String endTime = bundle.getString(Constants.END_TIME);
        mJobId = bundle.getString(Constants.JOB_ID);
        Utility.CURRENT_JOB_ID = mJobId;
        mCustomerMobile = bundle.getString(Constants.KEY_MOBILE);
    }

    private void setUpViews() {
        linearLayoutStatus = (LinearLayout) rootView.findViewById(R.id.layoutStatus);
        rootView.findViewById(R.id.btnUpdateStatus).setOnClickListener(this);
        rootView.findViewById(R.id.btnCall).setOnClickListener(this);
        rootView.findViewById(R.id.btnCancelJob).setOnClickListener(this);
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
            updateListAndSetThemOnListview(subStatusListModel);
            return;
        }
        if (mode.equals(Constants.SERVICE_MODE.UPDATE_STATUS_IN_PROGRESS)) {
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
            updateListOfStatusViews();
            return;
        }

        if (mode.equals(Constants.SERVICE_MODE.UPDATE_STATUS_COMPLETE_CANCEL)) {
            if (!(model instanceof Model)) {
                return;
            }
            Model responseModel = (Model) model;
            if (!responseModel.getStatus()) {
                Snackbar.make(getView(), responseModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                return;
            }
            mCurrentIndex = 0;
            Utility.CURRENT_JOB_ID = "";
            getActivity().getSupportFragmentManager().popBackStackImmediate();
            return;
        }

    }

    private void updateListAndSetThemOnListview(SubStatusListModel subStatusListModel) {
        List<SubStatusListModel.SubStatusModel> subStatusList;
        if (isPickUpJob) {
            subStatusList = subStatusListModel.getData().getPickup();
        } else {
            subStatusList = subStatusListModel.getData().getDrop();
        }
        if (subStatusList == null) {
            Snackbar.make(getView(), "Unable to load status at this moment", Snackbar.LENGTH_SHORT).show();
            return;
        }
        //Set the list to show on views , only sub-statuses with status 'in-progress i.e. 2' will be shown on views
        for (int i = 0; i < subStatusList.size(); i++) {
            SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(i);
            if (!subStatusModel.getStatusId().equals(Constants.STATUS_IN_PROGRESS)) {
                subStatusList.remove(i);
                i--;
            }
        }

        //Set the index to the sub-status from where we need to resume updating the sub-status
        for (int i = 0; i < subStatusList.size(); i++) {
            SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(i);
            if (Integer.parseInt(subStatusModel.getSubStatusId()) == Integer.parseInt(mSubStatusId)) {
                mCurrentIndex = i + 1;
                break;
            }
        }

        //If current index is greater than the number of sub-status shown on views , then there is a chance that after updating
        // all its sub-statuses, API to mark this job as complete was not called hence we need to mark this job as a complete job
        if (mCurrentIndex >= subStatusList.size()) {
            updateStatusOnServerForCompleteJob(Constants.STATUS_COMPLETE, Constants.SUB_STATUS_READY_FOR_NEXT_JOB, "");
            return;
        }

        this.subStatusList = subStatusList;
        setStatusUpdateListOnViews(this.subStatusList);
        updateListOfStatusViews();
    }

    private void updateListOfStatusViews() {
        if (mCurrentIndex > 2) {
            rootView.findViewById(R.id.btnCancelJob).setVisibility(View.GONE);
        }
        int childCount = linearLayoutStatus.getChildCount();
        for (int i = 0; i < childCount && i < mCurrentIndex; i++) {
            ((ImageView) linearLayoutStatus.getChildAt(i).findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_done);
        }
        if (childCount - 1 >= mCurrentIndex) {
            ((ImageView) linearLayoutStatus.getChildAt(mCurrentIndex).findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_in_progress);
            return;
        }

        if (mCurrentIndex > childCount - 1) {
            updateStatusOnServerForCompleteJob(Constants.STATUS_COMPLETE, Constants.SUB_STATUS_READY_FOR_NEXT_JOB, "");
        }
    }

    private void setStatusUpdateListOnViews(List<SubStatusListModel.SubStatusModel> list) {
        int size = list.size();
        if (size > 0) {
            View childView = LayoutInflater.from(getActivity()).inflate(R.layout.status_update_item, null);
            ((ImageView) childView.findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_in_progress);
            ((TextView) childView.findViewById(R.id.tvStatusText)).setText(list.get(0).getSubStatusName());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);

            linearLayoutStatus.addView(childView, layoutParams);
        }
        for (int i = 1; i < size; i++) {
            View childView = LayoutInflater.from(getActivity()).inflate(R.layout.status_update_item, null);
            ((ImageView) childView.findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_open_new);
            ((TextView) childView.findViewById(R.id.tvStatusText)).setText(list.get(i).getSubStatusName());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            linearLayoutStatus.addView(childView, layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        Animation mAnimationShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        switch (v.getId()) {
            case R.id.btnUpdateStatus:
                showDialogDependOnCase();
                break;

            case R.id.btnCall:
                rootView.findViewById(R.id.btnCall).startAnimation(mAnimationShake);
                makeCall();
                break;

            case R.id.btnCancelJob:
                rootView.findViewById(R.id.btnCancelJob).startAnimation(mAnimationShake);
                showDialogToCancelJob();
                break;

            default:
                showDialogToUpdateStatus(v.getId());
                break;
        }
    }

    private void showDialogToCancelJob() {
        final Dialog dialog = Utility.getDialog(getActivity(), R.layout.layout_dialog_with_message);
        ((TextView) dialog.findViewById(R.id.tvTitle)).setText("Cancel Job?");

        ((TextView) dialog.findViewById(R.id.tvMessage)).setText("Are you sure you want to cancel this job.");
        final EditText enterComment = (EditText) dialog.findViewById(R.id.enterComment);

        Button btnBack = (Button) dialog.findViewById(R.id.btnBack);
        btnBack.setText("No");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btSubmit = (Button) dialog.findViewById(R.id.btnSave);
        btSubmit.setText("Yes");
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateStatusOnServerForCompleteJob(Constants.STATUS_CANCEL, Constants.SUB_STATUS_CANCEL_BY_DRIVER, "" + enterComment.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }


    public void makeCall() {
        if (mCustomerMobile == null) {
            Utility.showToast(getActivity(), "No mobile number.");
            return;
        }
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        if (mCustomerMobile.startsWith("+91")) {
            phoneIntent.setData(Uri.parse("tel:" + mCustomerMobile));
        } else if (mCustomerMobile != null) {
            phoneIntent.setData(Uri.parse("tel:" + "+91" + mCustomerMobile));
        }
        startActivity(phoneIntent);
    }


    private void showDialogDependOnCase() {
        if ((subStatusList.size() - 1) < mCurrentIndex) {
            return;
        }
        SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(mCurrentIndex);
        String subStatusId = subStatusModel.getSubStatusId().trim();

        if (Integer.parseInt(subStatusId) == 5 || subStatusId.equals("14")) {
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
                    updateStatusOnServerForInProgressLead(index, enterComment.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void updateStatusOnServerForInProgressLead(int index, String comment) {
        SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(index);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.USER_ID));
        params.put(Constants.JOB_ID, mJobId);
        params.put(Constants.KEY_STATUS_ID, subStatusModel.getStatusId());
        params.put(Constants.KEY_SUB_STATUS_ID, subStatusModel.getSubStatusId());
        params.put(Constants.KEY_COMMENT, comment);
        params.put(Constants.KEY_DOC_ID, "");
        Location location = GPSTrackerService.location;
        if (location == null){
            params.put(Constants.KEY_LATITUDE, "" );
            params.put(Constants.KEY_LONGITUDE, "");
        }else {
            params.put(Constants.KEY_LATITUDE, "" + location.getLatitude());
            params.put(Constants.KEY_LONGITUDE, "" + location.getLongitude());
        }

        RetrofitRequest.updateStatus(params, new MyCallback<Model>(getActivity(), this, true, null, "",
                Constants.SERVICE_MODE.UPDATE_STATUS_IN_PROGRESS));
    }


    private void updateStatusOnServerForCompleteJob(String status, String subStatus, String comment) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.USER_ID));
        params.put(Constants.JOB_ID, mJobId);
        params.put(Constants.KEY_STATUS_ID, status);
        params.put(Constants.KEY_SUB_STATUS_ID, subStatus);
        params.put(Constants.KEY_COMMENT, comment);
        params.put(Constants.KEY_DOC_ID, "");
        Location location = GPSTrackerService.location;
        if (location == null){
            params.put(Constants.KEY_LATITUDE, "" );
            params.put(Constants.KEY_LONGITUDE, "");
        }else {
            params.put(Constants.KEY_LATITUDE, "" + location.getLatitude());
            params.put(Constants.KEY_LONGITUDE, "" + location.getLongitude());
        }
        RetrofitRequest.updateStatus(params, new MyCallback<Model>(getActivity(), this, true, null, "",
                Constants.SERVICE_MODE.UPDATE_STATUS_COMPLETE_CANCEL));
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

            DialogFragment newFragment = new UploadJobCardFragment();
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
                    boolean isUploadedSuccessfully = data.getBooleanExtra(UploadJobCardFragment.IS_UPLOADED_SUCCESSFULLY, false);
                    if (isUploadedSuccessfully) {
//                        mCurrentIndex = mCurrentIndex + 1;
//                        updateListOfStatusViews();
                        updateStatusOnServerForInProgressLead(mCurrentIndex, "Required images uploaded successfully.");
                    }
                    break;
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
