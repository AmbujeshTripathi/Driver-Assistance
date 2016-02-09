package com.mygaadi.driverassistance.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.activity.MainActivity;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.model.SubStatusListModel;
import com.mygaadi.driverassistance.retrofit.MyCallback;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;
import com.mygaadi.driverassistance.utils.Utility;

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
    private ImageView imageView;

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
        if (!mode.equals(Constants.SERVICE_MODE.SUB_STATUS_LIST)) {
            Snackbar.make(getView(), " Unable yo get List at this moment . Please try again later", Snackbar.LENGTH_SHORT).show();
            return;
        }
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
    }

    private void setStatusUpdateListOnViews(List<SubStatusListModel.SubStatusModel> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            View childView = LayoutInflater.from(getActivity()).inflate(R.layout.status_update_item, null);
            ((ImageView) childView.findViewById(R.id.statusIndicator)).setImageResource(R.drawable.status_open);
            ((TextView) childView.findViewById(R.id.tvStatusText)).setText(list.get(i).getSubStatusName());
//            childView.setId(i);
//            childView.setOnClickListener(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 5, 0, 5);
            linearLayoutStatus.addView(childView, layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateStatus:
//                showDialogToUpdateStatus(0);
                showDialogToUpLoadImage(0);
                break;

            default:
                showDialogToUpdateStatus(v.getId());
                break;
        }
    }

    private void showDialogToUpdateStatus(int id) {
        if ((subStatusList.size() - 1) < id) {
            return;
        }
        SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(id);

        final Dialog dialog = Utility.getDialog(getActivity(), R.layout.layout_dialog_with_message);
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
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }


    private void showDialogToUpLoadImage(int index) {


        {
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }

            DialogFragment newFragment = new CaptureImageFragment();
            newFragment.setTargetFragment(this, CAPTURE_IMAGE);
            newFragment.show(ft, "dialog");
        }

//        SubStatusListModel.SubStatusModel subStatusModel = subStatusList.get(index);
//
//        final Dialog dialog = Utility.getDialog(getActivity(), R.layout.dialog_capture_image);
//        View btnBack = dialog.findViewById(R.id.btnBack);
//
//        Button btSubmit = (Button) dialog.findViewById(R.id.btnSave);
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    dialog.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//        imageView = (ImageView) dialog.findViewById(R.id.imgCapturedImage);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCamera(CAPTURE_IMAGE);
//            }
//        });
//
//
//        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_IMAGE:
                    String hell = data.getStringExtra("Hell");
                    Toast.makeText(getActivity(), "" + hell, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
