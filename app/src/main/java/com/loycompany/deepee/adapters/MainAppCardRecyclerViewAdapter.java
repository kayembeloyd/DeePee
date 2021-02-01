package com.loycompany.deepee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.deepee.R;
import com.loycompany.deepee.classes.CustomApp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAppCardRecyclerViewAdapter extends RecyclerView.Adapter<MainAppCardRecyclerViewAdapter.ViewHolder> {

    public List<CustomApp> customAppList;
    Context mContext;

    public MainAppCardRecyclerViewAdapter(Context context, List<CustomApp> dataPlans) {
        this.customAppList = dataPlans;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAppCardRecyclerViewAdapter.ViewHolder holder, final int position) {
        // This is where stuff goes;

        holder.linearLayoutExpandable.setVisibility(View.GONE);

        holder.appName.setText(customAppList.get(position).name);

        holder.aSwitch.setChecked(customAppList.get(position).isEnabled);

        holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);

        holder.appIconCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearLayoutExpandable.getVisibility() == View.GONE){
                    holder.linearLayoutExpandable.setVisibility(View.VISIBLE);
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                } else{
                    holder.linearLayoutExpandable.setVisibility(View.GONE);
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                }
            }
        });

        holder.appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearLayoutExpandable.getVisibility() == View.GONE){
                    holder.linearLayoutExpandable.setVisibility(View.VISIBLE);
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                } else{
                    holder.linearLayoutExpandable.setVisibility(View.GONE);
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                }
            }
        });
    }

    @NonNull
    @Override
    public MainAppCardRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_main_app_card, parent, false);

        return new MainAppCardRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView appIconCircleImageView;
        TextView appName;
        LinearLayout linearLayoutExpandable;
        Switch aSwitch;

        public ViewHolder(View itemView) {
            super(itemView);

            appIconCircleImageView = itemView.findViewById(R.id.app_icon_circle_image_view);
            appName = itemView.findViewById(R.id.app_name);
            linearLayoutExpandable = itemView.findViewById(R.id.linear_layout_expandable);
            aSwitch = itemView.findViewById(R.id.switch1);
        }
    }

    @Override
    public int getItemCount(){
        return customAppList.size();
    }

}
