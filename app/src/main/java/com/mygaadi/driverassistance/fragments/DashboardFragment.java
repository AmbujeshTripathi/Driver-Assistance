package com.mygaadi.driverassistance.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.activity.MainActivity;
import com.mygaadi.driverassistance.adapter.JobModelAdapter;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.listeners.OnDateStripActionListener;
import com.mygaadi.driverassistance.model.JobDetail;
import com.mygaadi.driverassistance.model.JobListModel;
import com.mygaadi.driverassistance.retrofit.MyCallback;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;
import com.mygaadi.driverassistance.utils.DateStripController;
import com.mygaadi.driverassistance.utils.Utility;
import com.mygaadi.driverassistance.utils.UtilitySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

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
    private String dateSelected;
    String[] values = {"a", "a", "v"};

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        this.mDateStripController = new DateStripController((MainActivity) getActivity(), this);
        mDateStripController.initialize(rootView);
        try {
            getJobDetailFromServer();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onDateSelected(String date) {
        dateSelected = date;
        Log.d(TAG, date);
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

    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {
        JobListModel jobListModel = (JobListModel) model;
        if (jobListModel.getStatus()) {
            if ((jobListModel.getData().size() == 0) || (jobListModel.getData() == null)) {
                Toast.makeText(getActivity(), jobListModel.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Collections.sort(jobListModel.getData(), new Comparator<JobDetail>() {
                    @Override
                    public int compare(JobDetail lhs, JobDetail rhs) {
                        return ((Utility.getDateFromTime(lhs.getStartTime())).compareTo(Utility.getDateFromTime(rhs.getStartTime())));
                    }
                });
                initData(jobListModel.getData());
            }

        }


    }

    private String getCurrentTime() throws ParseException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat resultDateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = dateFormatGmt.parse(dateSelected);
        String startTime = resultDateFormatGmt.format(date);
        Log.d(TAG, startTime);
        return startTime;

    }

    private String getEndTime() throws ParseException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat resultDateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
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

    private void initData(List<JobDetail> data) {
        if (customAdapter == null) {
            customAdapter = new JobModelAdapter(getActivity(), data,this);
            mRecyclerView.setAdapter(customAdapter);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (position == 1) {

        }
    }
}
