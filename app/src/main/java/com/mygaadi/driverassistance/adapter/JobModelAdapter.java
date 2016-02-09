package com.mygaadi.driverassistance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.model.JobDetail;

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
        holder.customerAddress.setText(jobDetailModel.getCustomerlocality());
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

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView customerName;
        TextView customerEmail;
        TextView customerNumber;
        TextView customerAddress;
        TextView requestTime;


        public CardViewHolder(View itemView) {
            super(itemView);

            requestTime = (TextView) itemView.findViewById(R.id.request_time);
            customerEmail = (TextView) itemView.findViewById(R.id.customer_email);
            customerNumber = (TextView) itemView.findViewById(R.id.customer_mobile);
            customerAddress = (TextView) itemView.findViewById(R.id.customer_address);
            customerName = (TextView) itemView.findViewById(R.id.customer_name);
        }

        @Override
        public void onClick(View v) {

            onItemClickListener.onItemClick(this.getAdapterPosition());
//            this.onItemClick(getAdapterPosition());
        }


    }

    public void addAll(List<JobDetail> data) {
        resultSet.addAll(data);
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

}
