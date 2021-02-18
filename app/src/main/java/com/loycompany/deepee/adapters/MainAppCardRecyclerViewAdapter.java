package com.loycompany.deepee.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.loycompany.deepee.R;
import com.loycompany.deepee.classes.CustomApp;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.services.MyVpnService;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAppCardRecyclerViewAdapter extends RecyclerView.Adapter<MainAppCardRecyclerViewAdapter.ViewHolder> {

    public List<CustomApp> customAppList;
    Context mContext;

    DataPlan dataPlan;
    Handler handler;

    public MainAppCardRecyclerViewAdapter(Context context, List<CustomApp> dataPlans) {
        this.customAppList = dataPlans;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAppCardRecyclerViewAdapter.ViewHolder holder, final int position) {
        // This is where stuff goes;

        holder.linearLayoutExpandable.setVisibility(View.GONE);

        holder.appName.setText(customAppList.get(position).name);
        String appDataUsedS;
        
        handler = new Handler();

        if (customAppList.get(position).isUnlimited){

            appDataUsedS = "Data used (" + "nan " + "bytes) (Unlimited)";

            /*
            // for high API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PackageManager pm = mContext.getPackageManager();

                        NetworkStatsManager networkStatsManager;
                        networkStatsManager = (NetworkStatsManager) mContext.getSystemService(Context.NETWORK_STATS_SERVICE);
                        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.HOUR, 1);

                            // The next line needs to disable mobile data/wifi for smooth app flow (isamajame)
                            NetworkStats networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, null, 0, calendar.getTimeInMillis(), pm.getApplicationInfo(customAppList.get(position).mPackageName, 0).uid);

                            long rx = 0L;
                            long tx = 0L;
                            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                            while (networkStats.hasNextBucket()){
                                networkStats.getNextBucket(bucket);
                                rx += bucket.getRxBytes();
                                tx += bucket.getTxBytes();
                            }
                            networkStats.close();
                            final float total = (float) (rx + tx) / (1024f * 1024f);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    customAppList.get(position).totalUsedData = total;
                                    String appDataUsedS_ = "Data used (" + total + " mb) (Unlimited)";
                                    holder.appDataUsed.setText(appDataUsedS_);
                                }
                            });

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                // for less API
                try {
                    PackageManager pm = mContext.getPackageManager();

                    long rx = TrafficStats.getUidRxBytes(pm.getApplicationInfo(customAppList.get(position).mPackageName, 0).uid);
                    long tx = TrafficStats.getUidTxBytes(pm.getApplicationInfo(customAppList.get(position).mPackageName, 0).uid);
                    long total = (rx + tx)/1024;

                    appDataUsedS = "Data used (" + total + " mb) (Unlimited)";
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

             */

            try {
                PackageManager pm = mContext.getPackageManager();

                long rx = TrafficStats.getUidRxBytes(pm.getApplicationInfo(customAppList.get(position).mPackageName, 0).uid);
                long tx = TrafficStats.getUidTxBytes(pm.getApplicationInfo(customAppList.get(position).mPackageName, 0).uid);
                long total = (rx + tx)/1024;

                appDataUsedS = "Data used (" + total + " mb) (Unlimited)";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            // Old
            //appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
        } else {
            appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
        }

        holder.appDataUsed.setText(appDataUsedS);

        // Get a dataPlan in the database.

        dataPlan = customAppList.get(position).getDataPlan();
        String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
        holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
        Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
        String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
        holder.unallocatedDataSize.setText(unallocatedDataSizeS);

        holder.aSwitch.setChecked(customAppList.get(position).isEnabled);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                customAppList.get(position).isEnabled = isChecked;
                customAppList.get(position).save();

                Intent intent1 = new Intent(mContext, MyVpnService.class);
                // intent1.putExtra("MODE", "RESTART");
                mContext.startService(intent1);

                if (customAppList.get(position).isEnabled){
                    holder.appCard.setBackgroundResource(R.color.my_grey);
                } else {
                    holder.appCard.setBackgroundResource(R.color.my_white);
                }
            }
        });

        if (customAppList.get(position).isEnabled){
            holder.appCard.setBackgroundResource(R.color.my_grey);
        } else {
            holder.appCard.setBackgroundResource(R.color.my_white);
        }

        holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);

        holder.appIconCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearLayoutExpandable.getVisibility() == View.GONE){
                    holder.linearLayoutExpandable.setVisibility(View.VISIBLE);
                    dataPlan = customAppList.get(position).getDataPlan();
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);

                    // Update values
                    String appDataUsedS;

                    if (customAppList.get(position).isUnlimited){
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
                    } else {
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
                    }

                    holder.appDataUsed.setText(appDataUsedS);
                    String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
                    holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
                    Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
                    String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
                    holder.unallocatedDataSize.setText(unallocatedDataSizeS);

                    holder.aSwitch.setChecked(customAppList.get(position).isEnabled);

                } else{
                    holder.linearLayoutExpandable.setVisibility(View.GONE);
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                }
            }
        });

        try {
            final PackageManager pm = mContext.getPackageManager();
            // Picasso here
            holder.appIconCircleImageView.setImageDrawable(pm.getApplicationIcon(customAppList.get(position).mPackageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        holder.appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearLayoutExpandable.getVisibility() == View.GONE){
                    holder.linearLayoutExpandable.setVisibility(View.VISIBLE);
                    dataPlan = customAppList.get(position).getDataPlan();
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);

                    // Update values
                    String appDataUsedS;

                    if (customAppList.get(position).isUnlimited){
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
                    } else {
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
                    }

                    holder.appDataUsed.setText(appDataUsedS);
                    String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
                    holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
                    Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
                    String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
                    holder.unallocatedDataSize.setText(unallocatedDataSizeS);

                    holder.aSwitch.setChecked(customAppList.get(position).isEnabled);
                } else{
                    holder.linearLayoutExpandable.setVisibility(View.GONE);
                    holder.appName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                }
            }
        });

        holder.appSetUnlimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float dataSize = (customAppList.get(position).totalData - customAppList.get(position).totalUsedData);
                if (dataSize > 0){
                    dataPlan.totalAssignedData -= dataSize;
                    customAppList.get(position).totalData -= dataSize;
                    customAppList.get(position).isUnlimited = true;

                    dataPlan.save();
                    customAppList.get(position).save();

                    // Update values
                    String appDataUsedS;

                    if (customAppList.get(position).isUnlimited){
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
                    } else {
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
                    }

                    holder.appDataUsed.setText(appDataUsedS);
                    String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
                    holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
                    Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
                    String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
                    holder.unallocatedDataSize.setText(unallocatedDataSizeS);

                    holder.aSwitch.setChecked(customAppList.get(position).isEnabled);
                }
            }
        });

        holder.appRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float dataSize = (customAppList.get(position).totalData - customAppList.get(position).totalUsedData);
                if (dataSize > 0){
                    dataPlan.totalAssignedData -= dataSize;
                    customAppList.get(position).totalData -= dataSize;

                    dataPlan.save();
                    customAppList.get(position).save();

                    // Update values
                    String appDataUsedS;

                    if (customAppList.get(position).isUnlimited){
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
                    } else {
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
                    }

                    holder.appDataUsed.setText(appDataUsedS);
                    String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
                    holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
                    Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
                    String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
                    holder.unallocatedDataSize.setText(unallocatedDataSizeS);

                    holder.aSwitch.setChecked(customAppList.get(position).isEnabled);
                }
            }
        });

        holder.appRemoveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float dataSize = Float.parseFloat(Objects.requireNonNull(holder.appDataSize.getText()).toString());
                if (dataSize <= (customAppList.get(position).totalData - customAppList.get(position).totalUsedData)){
                    dataPlan.totalAssignedData -= dataSize;
                    customAppList.get(position).totalData -= dataSize;

                    dataPlan.save();
                    customAppList.get(position).save();

                    // Update values
                    String appDataUsedS;

                    if (customAppList.get(position).isUnlimited){
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
                    } else {
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
                    }

                    holder.appDataUsed.setText(appDataUsedS);
                    String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
                    holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
                    Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
                    String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
                    holder.unallocatedDataSize.setText(unallocatedDataSizeS);

                    holder.aSwitch.setChecked(customAppList.get(position).isEnabled);
                }
            }
        });

        holder.appAddAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float dataSize = dataPlan.totalData - dataPlan.totalAssignedData;

                if (dataSize > 0){
                    dataPlan.totalAssignedData += dataSize;
                    customAppList.get(position).totalData += dataSize;
                    customAppList.get(position).isUnlimited = false;

                    dataPlan.save();
                    customAppList.get(position).save();

                    // Update values
                    String appDataUsedS;

                    if (customAppList.get(position).isUnlimited){
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
                    } else {
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
                    }

                    holder.appDataUsed.setText(appDataUsedS);
                    String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
                    holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
                    Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
                    String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
                    holder.unallocatedDataSize.setText(unallocatedDataSizeS);

                    holder.aSwitch.setChecked(customAppList.get(position).isEnabled);
                }
            }
        });

        holder.appAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float dataSize = Float.parseFloat(Objects.requireNonNull(holder.appDataSize.getText()).toString());
                if (dataSize <= dataPlan.totalData - dataPlan.totalAssignedData ){
                    dataPlan.totalAssignedData += dataSize;
                    customAppList.get(position).totalData += dataSize;
                    customAppList.get(position).isUnlimited = false;

                    dataPlan.save();
                    customAppList.get(position).save();

                    // Update values
                    String appDataUsedS;

                    if (customAppList.get(position).isUnlimited){
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb) (Unlimited)";
                    } else {
                        appDataUsedS = "Data used (" + customAppList.get(position).totalUsedData + "mb of " + customAppList.get(position).totalData + "mb)";
                    }

                    holder.appDataUsed.setText(appDataUsedS);
                    String unallocatedDataInPlanS = "Unallocated data in " + dataPlan.name ;
                    holder.unallocatedDataInPlan.setText(unallocatedDataInPlanS);
                    Float unallocatedDataSizeF = dataPlan.totalData - dataPlan.totalAssignedData;
                    String unallocatedDataSizeS = unallocatedDataSizeF + "mb";
                    holder.unallocatedDataSize.setText(unallocatedDataSizeS);

                    holder.aSwitch.setChecked(customAppList.get(position).isEnabled);
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
        TextView appName, appDataUsed, unallocatedDataInPlan, unallocatedDataSize;
        LinearLayout linearLayoutExpandable;
        Switch aSwitch;
        Button appAddData, appRemoveData, appAddAll, appRemoveAll, appSetUnlimited;
        TextInputEditText appDataSize;
        CardView appCard;

        public ViewHolder(View itemView) {
            super(itemView);

            appIconCircleImageView = itemView.findViewById(R.id.app_icon_circle_image_view);
            appName = itemView.findViewById(R.id.app_name);
            appDataUsed = itemView.findViewById(R.id.app_data_used);
            unallocatedDataInPlan = itemView.findViewById(R.id.unallocated_data_in_plan);
            unallocatedDataSize = itemView.findViewById(R.id.unallocated_data_size);
            linearLayoutExpandable = itemView.findViewById(R.id.linear_layout_expandable);
            aSwitch = itemView.findViewById(R.id.switch1);

            appAddData = itemView.findViewById(R.id.app_add_data);
            appAddAll = itemView.findViewById(R.id.app_add_all);
            appRemoveData = itemView.findViewById(R.id.app_remove_data);
            appRemoveAll = itemView.findViewById(R.id.app_remove_all);
            appSetUnlimited = itemView.findViewById(R.id.app_set_unlimited);

            appDataSize = itemView.findViewById(R.id.app_data_size);
            appCard = itemView.findViewById(R.id.app_card);
        }
    }

    @Override
    public int getItemCount(){
        return customAppList.size();
    }

}
