package com.loycompany.deepee.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.deepee.R;
import com.loycompany.deepee.adapters.MainAppCardRecyclerViewAdapter;
import com.loycompany.deepee.adapters.MainDataPlanRecyclerViewAdapter;
import com.loycompany.deepee.classes.CustomApp;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.database.DeePeeDatabase;

import java.io.IOException;
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
    List<CustomApp> fastCustomAppList;
    MainAppCardRecyclerViewAdapter mainAppCardRecyclerViewAdapter;
    RecyclerView recyclerView2;
    RecyclerView.LayoutManager layoutManager2;

    NestedScrollView nestedScrollView;

    private DeePeeDatabase deePeeDatabase;
    private SQLiteDatabase mDb;

    Handler handler = new Handler();
    int oldDataPlanID = -2;

    public ActiveDpFragment(Context context) {        
        deePeeDatabase = new DeePeeDatabase(context);
        try { deePeeDatabase.updateDataBase(); } catch (IOException mIOException) { throw new Error("UnableToUpdateDatabase"); }
        mDb = deePeeDatabase.getWritableDatabase();

        // Make sure you load plans from database
        // FOR RECYCLER VIEW
        dataPlanList = new ArrayList<>();
        dataPlanList.add(deePeeDatabase.activeDataPlan());
        mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(context, dataPlanList);

        // FOR PAGER
        /*
        dataPlanList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            dataPlanList.add(new DataPlan());
        }
        mainDataPlanPagerAdapter = new MainDataPlanPagerAdapter(dataPlanList, getContext());*/

        customAppList = new ArrayList<>();
        fastCustomAppList = new ArrayList<>();
        mainAppCardRecyclerViewAdapter = new MainAppCardRecyclerViewAdapter(getContext(), fastCustomAppList);

        if (dataPlanList.size() > 0){
            customAppList.addAll(deePeeDatabase.getCustomApps(dataPlanList.get(0).id, 4, customAppList.size()));
            fastCustomAppList.addAll(customAppList);
        }

    }

    public static ActiveDpFragment newInstance(Context context) {
        ActiveDpFragment fragment = new ActiveDpFragment(context);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Need a way of detecting whether an active plan has been changed

        // Load the Active dataPlan
        // dataPlanList = new ArrayList<>();
        // dataPlanList.add(deePeeDatabase.activeDataPlan());
        // mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(getContext(), dataPlanList);
        // recyclerView1.setAdapter(mainDataPlanRecyclerViewAdapter);

        if (dataPlanList.size() > 0){

            // Load the Active dataPlan
            dataPlanList.set(0, deePeeDatabase.activeDataPlan());
            mainDataPlanRecyclerViewAdapter.notifyItemChanged(0);

            if (customAppList.size() > 3){
                fastCustomAppList = new ArrayList<>();

                for (int i = 0; i < 3; i++){
                    fastCustomAppList.add(customAppList.get(i));
                    mainAppCardRecyclerViewAdapter = new MainAppCardRecyclerViewAdapter(getContext(), fastCustomAppList);
                    recyclerView2.setAdapter(mainAppCardRecyclerViewAdapter);
                }
            }


            // This makes sure customAppList resets only when the ActiveDataPlan has been changed
            if (oldDataPlanID == -2){
                oldDataPlanID = dataPlanList.get(0).id;

                // Create a thread here
                onResumeLoadCustomAppListThread();
            } else if (oldDataPlanID != dataPlanList.get(0).id){
                oldDataPlanID = dataPlanList.get(0).id;

                customAppList = new ArrayList<>();
                fastCustomAppList = new ArrayList<>();
                customAppList.addAll(deePeeDatabase.getCustomApps(dataPlanList.get(0).id, 4, customAppList.size()));
                fastCustomAppList.addAll(customAppList);
                mainAppCardRecyclerViewAdapter = new MainAppCardRecyclerViewAdapter(getContext(), fastCustomAppList);
                recyclerView2.setAdapter(mainAppCardRecyclerViewAdapter);
                onResumeLoadCustomAppListThread();
            }
        }
    }

    public void onResumeLoadCustomAppListThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // SystemClock.sleep(5000);
                customAppList.addAll(deePeeDatabase.getCustomApps(dataPlanList.get(0).id, customAppList.size()));
            }
        }).start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_active_dp, container, false);

        // FOR RECYCLER VIEW
        // mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(getContext(), dataPlanList);

        recyclerView1 = rootView.findViewById(R.id.recycler_view1);

        layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(mainDataPlanRecyclerViewAdapter);

        // FOR PAGER
        /* mainDataPlanPagerAdapter = new MainDataPlanPagerAdapter(dataPlanList, getContext());
        viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(mainDataPlanPagerAdapter); */

        mainAppCardRecyclerViewAdapter = new MainAppCardRecyclerViewAdapter(getContext(), fastCustomAppList);

        recyclerView2 = rootView.findViewById(R.id.recycler_view2);

        layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mainAppCardRecyclerViewAdapter);

        nestedScrollView = rootView.findViewById(R.id.nested_scroll_view);

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!nestedScrollView.canScrollVertically(1)) {
                    // Load more
                    if (customAppList.size() > fastCustomAppList.size()){
                        int n = fastCustomAppList.size() + 3;
                        for (int i = fastCustomAppList.size() - 1; i < n; i++){
                            if (i < customAppList.size() - 1){
                                fastCustomAppList.add(customAppList.get(i));
                                mainAppCardRecyclerViewAdapter.notifyItemInserted(fastCustomAppList.size() - 1);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        });
        return rootView;
    }
}