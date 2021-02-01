package com.loycompany.deepee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.deepee.DataPlanActivity;
import com.loycompany.deepee.R;
import com.loycompany.deepee.classes.DataPlan;

import java.util.List;

public class MainDataPlanRecyclerViewAdapter extends RecyclerView.Adapter<MainDataPlanRecyclerViewAdapter.ViewHolder> {

    public List<DataPlan> dataPlanList;
    Context mContext;

    public MainDataPlanRecyclerViewAdapter(Context context, List<DataPlan> dataPlans) {
        this.dataPlanList = dataPlans;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(final MainDataPlanRecyclerViewAdapter.ViewHolder holder, final int position) {
        // This is where stuff goes;
        holder.dpName.setText(this.dataPlanList.get(position).name);

        holder.dpName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DataPlanActivity.class);

                // Transfer the contents of the new DataPlan.
                intent.putExtra("id", dataPlanList.get(position).id);

                mContext.startActivity(intent);
            }
        });

        if (this.dataPlanList.get(position).dataPlanType == DataPlan.DataPlanType.MOBILE_DATA){
            holder.dpDataPlanType.setText("MOBILE_DATA");
        } else if (this.dataPlanList.get(position).dataPlanType == DataPlan.DataPlanType.WIFI){
            holder.dpDataPlanType.setText("WIFI");
        } else {
            holder.dpDataPlanType.setText("WIFI_MOBILE_DATA");
        }

        String dpDataUsedTotalData = this.dataPlanList.get(position).totalUsedData + "mb of " + this.dataPlanList.get(position).totalData + "mb used.";
        holder.dpDataUsedTotalData.setText(dpDataUsedTotalData);

        holder.dpTimeRemaining.setText("X days X hours X minutes remaining");

        holder.dpEndDate.setText(this.dataPlanList.get(position).endDateTime.toString());
    }

    @NonNull
    @Override
    public MainDataPlanRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_main_data_plan_card, parent, false);

        return new MainDataPlanRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dpName, dpDataPlanType, dpDataUsedTotalData, dpTimeRemaining, dpEndDate;

        public ViewHolder(View itemView) {
            super(itemView);

            dpName = itemView.findViewById(R.id.dp_name);
            dpDataPlanType = itemView.findViewById(R.id.dp_data_plan_type);
            dpDataUsedTotalData = itemView.findViewById(R.id.dp_data_used_total_data);
            dpTimeRemaining = itemView.findViewById(R.id.dp_time_remaining);
            dpEndDate = itemView.findViewById(R.id.dp_end_date);
        }
    }

    @Override
    public int getItemCount(){
        return dataPlanList.size();
    }

}
