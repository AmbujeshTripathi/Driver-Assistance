package com.mygaadi.driverassistance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.model.JobDetail;
import com.mygaadi.driverassistance.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Satyanshu on 10/19/2015.
 */
public class JobModelAdapter extends RecyclerView.Adapter<JobModelAdapter.CardViewHolder> {


    Context context;
    List<JobDetail> resultSet;
    String modelName;
    final String TAG = "JobModelAdapter";
    static OnItemClickListener onItemClickListener;

    public JobModelAdapter(Context context, List<JobDetail> jobDetailList, OnItemClickListener onItemClickListener) {
        this.resultSet = jobDetailList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        final JobDetail jobDetailModel = resultSet.get(position);
        modelName = jobDetailModel.getMake() + " " + jobDetailModel.getModel();
        holder.customerEmail.setText(jobDetailModel.getCustomerEmail());
        holder.customerName.setText(jobDetailModel.getCustomerName());
        holder.customerNumber.setText(jobDetailModel.getCustomerMobile());
        holder.customerAddress.setText(jobDetailModel.getCustomerLocality());
        holder.hubAddress.setText(jobDetailModel.getHubAddress());
        holder.requestStartTime.setText(Utility.getTimeFromDate(jobDetailModel.getStartTime()));
        holder.requestEndTime.setText(Utility.getTimeFromDate(jobDetailModel.getEndTime()));
        try {
            if ((position == 0) && (checkTimeGap(jobDetailModel.getStartTime())))
                holder.buttonLayout.setVisibility(View.VISIBLE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.driver_cardlayout, parent, false);

        return new CardViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return resultSet.size();
    }

    public void setAnimation(View view, int position) {


        Animation animation = AnimationUtils.loadAnimation(context, R.anim.to_top);
        animation.setStartOffset(1000);
        view.startAnimation(animation);

    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        TextView customerName;
        TextView customerEmail;
        TextView customerNumber;
        TextView customerAddress;
        TextView hubAddress;
        TextView requestStartTime;
        TextView requestEndTime;
        Button startJob, cancelJob;
        LinearLayout buttonLayout, call_layout;


        public CardViewHolder(View itemView) {
            super(itemView);

            requestStartTime = (TextView) itemView.findViewById(R.id.request_start_time);
            requestEndTime = (TextView) itemView.findViewById(R.id.request_end_time);
            customerEmail = (TextView) itemView.findViewById(R.id.customer_email);
            customerNumber = (TextView) itemView.findViewById(R.id.customer_mobile);
            customerAddress = (TextView) itemView.findViewById(R.id.customer_address);
            hubAddress = (TextView) itemView.findViewById(R.id.service_address);
            customerName = (TextView) itemView.findViewById(R.id.customer_name);
            startJob = (Button) itemView.findViewById(R.id.start_job);
            cancelJob = (Button) itemView.findViewById(R.id.cancel_job);
            buttonLayout = (LinearLayout) itemView.findViewById(R.id.button_layout);
            call_layout = (LinearLayout) itemView.findViewById(R.id.call_layout);
            call_layout.setOnTouchListener(this);
            startJob.setOnClickListener(this);
            cancelJob.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            onItemClickListener.onItemClick(v, this.getAdapterPosition());
//            this.onItemClick(getAdapterPosition());
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.call_layout) {
                event.getX();

            }
            return true;
        }
    }

    public void addAll(List<JobDetail> data) {
        resultSet.addAll(data);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private boolean checkTimeGap(String startTime) throws ParseException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = dateFormatGmt.parse(startTime);
        Date nowDate = new Date();
        long diff = date.getTime() - nowDate.getTime();
        int hours = (int) (diff / (1000 * 60 * 60));
        return (hours <= 2);
    }
}
