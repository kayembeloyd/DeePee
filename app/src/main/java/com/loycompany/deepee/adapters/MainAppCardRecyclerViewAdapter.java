package com.loycompany.deepee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.deepee.R;
import com.loycompany.deepee.classes.AppCard;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAppCardRecyclerViewAdapter extends RecyclerView.Adapter<MainAppCardRecyclerViewAdapter.ViewHolder> {

    public List<AppCard> appCardList;
    Context mContext;

    public MainAppCardRecyclerViewAdapter(Context context, List<AppCard> dataPlans) {
        this.appCardList = dataPlans;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAppCardRecyclerViewAdapter.ViewHolder holder, final int position) {
        // This is where stuff goes;
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
        TextView appNameTextView;
        LinearLayout linearLayoutExpandable;
        public ViewHolder(View itemView) {
            super(itemView);

            appIconCircleImageView = itemView.findViewById(R.id.app_icon_circle_image_view);
            appNameTextView = itemView.findViewById(R.id.app_name_textView);
            linearLayoutExpandable = itemView.findViewById(R.id.linear_layout_expandable);

            appIconCircleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayoutExpandable.getVisibility() == View.GONE){
                        linearLayoutExpandable.setVisibility(View.VISIBLE);
                        appNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                    } else{
                        linearLayoutExpandable.setVisibility(View.GONE);
                        appNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                    }
                }
            });

            appNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayoutExpandable.getVisibility() == View.GONE){
                        linearLayoutExpandable.setVisibility(View.VISIBLE);
                        appNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                    } else{
                        linearLayoutExpandable.setVisibility(View.GONE);
                        appNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return appCardList.size();
    }

}
