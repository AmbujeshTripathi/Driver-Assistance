package com.mygaadi.driverassistance.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.listeners.OnDateStripActionListener;
import com.mygaadi.driverassistance.model.JobDetailModel;
import com.mygaadi.driverassistance.retrofit.MyCallback;
import com.mygaadi.driverassistance.retrofit.RestCallback;
import com.mygaadi.driverassistance.retrofit.RetrofitRequest;

import java.util.HashMap;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements OnDateStripActionListener, RestCallback {
    public static final String TAG = "DashboardFragment";
    private ViewGroup rootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

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
        getJobDetailFromServer();
        return rootView;
    }

    @Override
    public void onDateSelected(String date) {

    }

    @Override
    public void onDateSelected(long date) {

    }

    private void getJobDetailFromServer() {
        HashMap<String, String> mParams = new HashMap<String, String>();
        mParams.put(Constants.START_TIME, "");
        mParams.put(Constants.END_TIME, "");
        RetrofitRequest.getJobs(mParams, new MyCallback<JobDetailModel>(getActivity(), this, true, null, null,
                Constants.SERVICE_MODE.GET_JOBS));
    }

    @Override
    public void onFailure(RetrofitError e, Constants.SERVICE_MODE mode) {

    }

    @Override
    public void onSuccess(Object model, Response response, Constants.SERVICE_MODE mode) {

    }
}
