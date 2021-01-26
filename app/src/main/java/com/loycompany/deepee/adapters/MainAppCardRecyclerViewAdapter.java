package com.loycompany.deepee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.deepee.R;
import com.loycompany.deepee.classes.AppCard;

import java.util.List;

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
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount(){
        return appCardList.size();
    }

}
