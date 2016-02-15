package com.mygaadi.driverassistance.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.activity.MainActivity;
import com.mygaadi.driverassistance.adapter.JobModelAdapter;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.listeners.OnDateStripActionListener;
import com.mygaadi.driverassistance.model.JobDetail;
import com.mygaadi.driverassistance.model.JobListModel;
import com.mygaadi.driverassistance.model.Model;
import com.mygaadi.driverassistance.retrofit.MyCallback;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;
import com.mygaadi.driverassistance.retrofit.UploadCallback;
import com.mygaadi.driverassistance.services.GPSTrackerService;
import com.mygaadi.driverassistance.utils.DateStripController;
import com.mygaadi.driverassistance.utils.Utility;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ambujesh on 2/9/2016.
 */
public class DashboardFragment extends Fragment implements OnDateStripActionListener, RestCallback, JobModelAdapter.OnItemClickListener {
    public static final String TAG = "DashboardFragment";
    private ViewGroup rootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DateStripController mDateStripController;
    private JobModelAdapter customAdapter;
    private String dateSelected, fileName;
    private TextView noDataView;
    private ProgressBar mProgressBar;
    private Dialog dialog;
    JobListModel jobListModel;
    List<JobDetail> resultSet, filterSet;
    String[] values = {"a", "a", "v"};
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    Date lastUpdatedDate;
    String mJobId;
    SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
    String permissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int PERMISSION_REQUEST_CODE = 101;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        ActionBar supportActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("JOB LIST");
            supportActionBar.setIcon(R.drawable.launcher_icon);
        }
        if (savedInstanceState != null) {
            fileName = savedInstanceState.getString(Constants.FILE_NAME);
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        noDataView = (TextView) rootView.findViewById(R.id.noDataLayout);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarSelfie);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        this.mDateStripController = new DateStripController((MainActivity) getActivity(), this);
        mDateStripController.initialize(rootView);
//        try {
//            getJobDetailFromServer();
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return rootView;
    }


    @Override
    public void onDateSelected(String date) {
        dateSelected = date;
        Log.d(TAG, date);
        try {
            getJobDetailFromServer();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSelected(long date) {

    }

    private void getJobDetailFromServer() throws ParseException {
        HashMap<String, String> mParams = new HashMap<String, String>();
        mParams.put(Constants.START_TIME, getCurrentTime());
        mParams.put(Constants.USER_ID, UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.USER_ID));
        mParams.put(Constants.END_TIME, getEndTime());
        RetrofitRequest.getJobs(mParams, new MyCallback<JobListModel>(getActivity(), this, true, null, null,
                Constants.SERVICE_MODE.GET_JOBS));
    }

    @Override
    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode) {
        if (mode == Constants.SERVICE_MODE.UPLOAD_SELFIE) {
            if (new File(fileName).exists()) {
                uploadSelfie();
            }
        }
    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {
        if (mode == Constants.SERVICE_MODE.GET_JOBS) {
            jobListModel = (JobListModel) model;
            if (jobListModel.getStatus()) {
                filterData(jobListModel.getData());
                if ((filterSet.size() == 0) || (filterSet == null)) {
                    Toast.makeText(getActivity(), jobListModel.getMessage(), Toast.LENGTH_SHORT).show();
                    noDataView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Collections.sort(filterSet, new Comparator<JobDetail>() {
                        @Override
                        public int compare(JobDetail lhs, JobDetail rhs) {
                            return ((Utility.getDateFromTime(lhs.getStartTime())).compareTo(Utility.getDateFromTime(rhs.getStartTime())));
                        }
                    });
                    initData();
                }

            }
        } else if (mode == Constants.SERVICE_MODE.UPLOAD_SELFIE) {
            Toast.makeText(getActivity(), "Selfie uploaded", Toast.LENGTH_SHORT).show();
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
            UtilitySingleton.getInstance(getActivity()).saveStringInSharedPref(Constants.IS_SELFIE_UPLOADED, dateFormatGmt.format(new Date()).toString());
        } else if (mode == Constants.SERVICE_MODE.UPDATE_STATUS) {
            Toast.makeText(getActivity(), "Job cancelled", Toast.LENGTH_SHORT).show();
            try {
                getJobDetailFromServer();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    private String getCurrentTime() throws ParseException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat resultDateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        resultDateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = dateFormatGmt.parse(dateSelected);
        String startTime = resultDateFormatGmt.format(date);
        Log.d(TAG, startTime);
        return startTime;

    }

    private String getEndTime() throws ParseException {

        SimpleDateFormat resultDateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar calendar = Calendar.getInstance();
        // calendar.set(Calendar.DATE,dateSelected);
        Date date = dateFormatGmt.parse(dateSelected);
        date.setMinutes(59);
        date.setHours(23);
        date.setSeconds(59);
        calendar.setTime(date);
        String endTime = resultDateFormatGmt.format(calendar.getTime());
        Log.d(TAG, endTime);
        return endTime;

    }

    private void initData() {
        if (resultSet == null) {
            resultSet = new ArrayList<>();
        }
        resultSet.clear();
        resultSet.addAll(filterSet);

        customAdapter = new JobModelAdapter(getActivity(), resultSet, this);
        mRecyclerView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(View view, int position) {
//        JobDetail item = jobListModel.getData().get(position);
        JobDetail item = filterSet.get(position);
        mJobId = item.getJobId();
        if (view.getId() == R.id.start_job) {
            String dateUpdatedValue = UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.IS_SELFIE_UPLOADED);
            try {
                if ((dateUpdatedValue == null) || (checkUpdatedDate(dateUpdatedValue))) {
                    showDialogForSelfie("Please upload selfie first to continue");
                } else {
                    startNextFragment(item);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.cancel_job) {
            showDialogToUpdateStatus();
            //Todo cancel job api implement

        } else if (view.getId() == R.id.cardview) {
            Log.d(TAG, "card clicked");
            try {
                if ((Utility.checkDayGap(item.getStartTime())) || (!Utility.checkTimeGap(item.getStartTime())))
                    Toast.makeText(getActivity(), "Task cannot be started.", Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }


    private void takeSelfie() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (Utility.checkForPermission(getActivity(), permissions, PERMISSION_REQUEST_CODE)) {
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                fileName = photoFile.getAbsolutePath();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (new File(fileName).exists()) {
                Log.d(TAG, new Date().toString());
                uploadSelfie();
            }
        }
    }

    private void uploadSelfie() {
        String userId = UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.USER_ID);
        RetrofitRequest.uploadJobCard(new File(fileName), null, "selfie", userId, new UploadCallback<Model>(this, Constants.SERVICE_MODE.UPLOAD_SELFIE,
                mProgressBar, null, null, null));
    }

    private boolean checkUpdatedDate(String date) throws ParseException {
        lastUpdatedDate = dateFormatGmt.parse(date);
        Date currentDate = new Date();
        currentDate = dateFormatGmt.parse(dateFormatGmt.format(currentDate));
        if ((lastUpdatedDate.compareTo(currentDate) < 0))
            return true;
        return false;
    }

    private void showDialogForSelfie(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takeSelfie();
            }
        }).create();
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void filterData(List<JobDetail> data) {
        Iterator iterator = data.iterator();
        if (filterSet == null)
            filterSet = new ArrayList<>();
        filterSet.clear();
        while (iterator.hasNext()) {
            JobDetail item = (JobDetail) iterator.next();
            if ((item.getStatusName().equalsIgnoreCase("New")) || (item.getStatusName().equalsIgnoreCase("InProgress"))) {
                filterSet.add(item);
            }
        }
    }

    private boolean checkForSelfie() throws ParseException {
        String dateUpdatedValue = UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.IS_SELFIE_UPLOADED);
        if ((dateUpdatedValue == null) || (checkUpdatedDate(dateUpdatedValue))) {
            showDialogForSelfie("Please upload selfie first to continue");
            return true;
        }
        return false;
    }

    private void startNextFragment(JobDetail item) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CUSTOMER_ADDRESS, item.getCustomerLocality());
        bundle.putString(Constants.HUB_ADDRESS, item.getHubAddress());
        bundle.putString(Constants.JOB_ID, item.getJobId());
        bundle.putString(Constants.START_TIME, item.getStartTime());
        bundle.putString(Constants.END_TIME, item.getEndTime());
        bundle.putString(Constants.KEY_MOBILE, item.getCustomerMobile());
        bundle.putString(Constants.JOB_TYPE, item.getJobType());
        Utility.navigateFragment(new StatusUpdateFragment(), StatusUpdateFragment.TAG, bundle, getActivity());
    }

    private void showDialogToUpdateStatus() {

        dialog = Utility.getDialog(getActivity(), R.layout.layout_dialog_with_message);
        View btnBack = dialog.findViewById(R.id.btnBack);

        ((TextView) dialog.findViewById(R.id.tvMessage)).setText("Cancel Job");
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
                    updateStatus(enterComment.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void updateStatus(String comment) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, UtilitySingleton.getInstance(getActivity()).getStringFromSharedPref(Constants.USER_ID));
        params.put(Constants.JOB_ID, mJobId);
        params.put(Constants.KEY_STATUS_ID, Constants.STATUS_CANCEL);
        params.put(Constants.KEY_SUB_STATUS_ID, Constants.SUB_STATUS_CANCEL_BY_DRIVER);
        params.put(Constants.KEY_COMMENT, comment);
        params.put(Constants.KEY_DOC_ID, "");
        params.put(Constants.KEY_LATITUDE, GPSTrackerService.location.getLatitude() + "");
        params.put(Constants.KEY_LONGITUDE, GPSTrackerService.location.getLongitude() + "");
        RetrofitRequest.updateStatus(params, new MyCallback<Model>(getActivity(), this, true, null, "",
                Constants.SERVICE_MODE.UPDATE_STATUS));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.FILE_NAME, fileName);
    }
}
