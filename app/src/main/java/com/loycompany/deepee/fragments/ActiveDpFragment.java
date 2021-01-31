package com.loycompany.deepee.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loycompany.deepee.R;
import com.loycompany.deepee.adapters.MainAppCardRecyclerViewAdapter;
import com.loycompany.deepee.adapters.MainDataPlanRecyclerViewAdapter;
import com.loycompany.deepee.classes.CustomApp;
import com.loycompany.deepee.classes.DataPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActiveDpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActiveDpFragment extends Fragment {

    // FOR RECYCLER VIEW
    List<DataPlan> dataPlanList;
    MainDataPlanRecyclerViewAdapter mainDataPlanRecyclerViewAdapter;
    RecyclerView recyclerView1;
    RecyclerView.LayoutManager layoutManager1;

    // FOR PAGER
    /*List<DataPlan> dataPlanList;
    MainDataPlanPagerAdapter mainDataPlanPagerAdapter;
    ViewPager viewPager;*/

    List<CustomApp> customAppList;
    MainAppCardRecyclerViewAdapter mainAppCardRecyclerViewAdapter;
    RecyclerView recyclerView2;
    RecyclerView.LayoutManager layoutManager2;

    public ActiveDpFragment(Context context) {
        // Required empty public constructor

        // Make sure you load plans from database
        // FOR RECYCLER VIEW
        dataPlanList = new ArrayList<>();
        for (int i = 0; i < 1; i++){
            dataPlanList.add(new DataPlan(context));
        }
        mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(getContext(), dataPlanList);


        // FOR PAGER
        /*
        dataPlanList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            dataPlanList.add(new DataPlan());
        }
        mainDataPlanPagerAdapter = new MainDataPlanPagerAdapter(dataPlanList, getContext());*/

        customAppList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            customAppList.add(new CustomApp(context));
        }
        mainAppCardRecyclerViewAdapter = new MainAppCardRecyclerViewAdapter(getContext(), customAppList);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ActiveDpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActiveDpFragment newInstance(Context context) {
        ActiveDpFragment fragment = new ActiveDpFragment(context);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_dp, container, false);

        // FOR RECYCLER VIEW
        mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(getContext(), dataPlanList);

        recyclerView1 = rootView.findViewById(R.id.recycler_view1);

        layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(mainDataPlanRecyclerViewAdapter);

        // FOR PAGER
        /* mainDataPlanPagerAdapter = new MainDataPlanPagerAdapter(dataPlanList, getContext());
        viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(mainDataPlanPagerAdapter); */


        mainAppCardRecyclerViewAdapter = new MainAppCardRecyclerViewAdapter(getContext(), customAppList);

        recyclerView2 = rootView.findViewById(R.id.recycler_view2);

        layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mainAppCardRecyclerViewAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }
}