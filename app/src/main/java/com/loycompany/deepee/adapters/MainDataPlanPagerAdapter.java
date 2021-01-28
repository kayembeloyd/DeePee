package com.loycompany.deepee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.loycompany.deepee.R;
import com.loycompany.deepee.classes.DataPlan;

import java.util.List;

public class MainDataPlanPagerAdapter extends PagerAdapter {
    List<DataPlan> dataPlanList;
    Context mContext;

    private LayoutInflater inflater;


    public MainDataPlanPagerAdapter(List<DataPlan> dataPlanList, Context mContext) {
        this.dataPlanList = dataPlanList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataPlanList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.card_main_data_plan_card, container, false);

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}
