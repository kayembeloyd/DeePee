package com.loycompany.deepee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.dpName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DataPlanActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public MainDataPlanRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_main_data_plan_card, parent, false);

        return new MainDataPlanRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dpName;
        public ViewHolder(View itemView) {
            super(itemView);

            dpName = itemView.findViewById(R.id.dp_name);
        }
    }

    @Override
    public int getItemCount(){
        return dataPlanList.size();
    }

}
