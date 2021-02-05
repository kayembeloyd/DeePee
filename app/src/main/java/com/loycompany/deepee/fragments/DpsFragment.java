package com.loycompany.deepee.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.loycompany.deepee.DataPlanActivity;
import com.loycompany.deepee.R;
import com.loycompany.deepee.adapters.MainDataPlanRecyclerViewAdapter;
import com.loycompany.deepee.classes.CustomApp;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.classes.DateTime;
import com.loycompany.deepee.database.DeePeeDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DpsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// #LEN331 Lengthy process

public class DpsFragment extends Fragment {

    List<DataPlan> dataPlanList;

    MainDataPlanRecyclerViewAdapter mainDataPlanRecyclerViewAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    Dialog createDpDialog;
    ExtendedFloatingActionButton createDpFabBtn;

    Spinner dialogDataTypeSpinner;
    Button dialogCreateDataPlanButton;

    TextInputEditText dialogDpName, dialogDpDataSize;
    TextInputEditText dialogStartTime, dialogEndTime;

    int day, month, year, hour, minute;
    int myday, mymonth, myyear, myhour, myminute;

    private DeePeeDatabase deePeeDatabase;
    private SQLiteDatabase mDb;

    DataPlan dataPlanToCreate;

    public DpsFragment(Context context) {
        // Required empty public constructor

        deePeeDatabase = new DeePeeDatabase(context);

        try {
            deePeeDatabase.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = deePeeDatabase.getWritableDatabase();

        // Make sure you load plans from database
        dataPlanList = new ArrayList<>();
        dataPlanList = deePeeDatabase.getDataPlans();

        if (dataPlanList == null || dataPlanList.size() == 0){
            dataPlanList = new ArrayList<>();
        }

        mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(getContext(), dataPlanList);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DpsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DpsFragment newInstance(Context context) {
        DpsFragment fragment = new DpsFragment(context);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update the DataPlanList;
        // Make sure you load plans from database
        dataPlanList = new ArrayList<>();
        dataPlanList = deePeeDatabase.getDataPlans();

        if (dataPlanList == null || dataPlanList.size() == 0){
            dataPlanList = new ArrayList<>();
        }

        mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(getContext(), dataPlanList);
        recyclerView.setAdapter(mainDataPlanRecyclerViewAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dps, container, false);

        mainDataPlanRecyclerViewAdapter = new MainDataPlanRecyclerViewAdapter(getContext(), dataPlanList);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mainDataPlanRecyclerViewAdapter);

        createDpFabBtn = rootView.findViewById(R.id.floatingActionButton);

        createDpFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataPlanToCreate = new DataPlan(getContext());

                createDpDialog = new Dialog(Objects.requireNonNull(getContext()));

                createDpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                createDpDialog.setCancelable(true);
                createDpDialog.setContentView(R.layout.dialog_create_dp);

                dialogDpName = createDpDialog.findViewById(R.id.dialog_dp_name);
                dialogDpDataSize = createDpDialog.findViewById(R.id.dialog_dp_datasize);

                dialogStartTime = createDpDialog.findViewById(R.id.dialog_start_time);
                dialogEndTime = createDpDialog.findViewById(R.id.dialog_end_time);

                dialogDataTypeSpinner = createDpDialog.findViewById(R.id.dialog_data_type_spinner);

                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                        R.array.data_type, android.R.layout.simple_spinner_item);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                dialogDataTypeSpinner.setAdapter(arrayAdapter);
                dialogDataTypeSpinner.setSelection(0);

                dialogCreateDataPlanButton = createDpDialog.findViewById(R.id.button);

                dialogCreateDataPlanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDpDialog.cancel();

                        dataPlanToCreate.id = -1;
                        dataPlanToCreate.name = Objects.requireNonNull(dialogDpName.getText()).toString();

                        dataPlanToCreate.startDateTime = DateTime.parseData(Objects.requireNonNull(dialogStartTime.getText()).toString());
                        dataPlanToCreate.endDateTime = DateTime.parseData(Objects.requireNonNull(dialogEndTime.getText()).toString());

                        dataPlanToCreate.totalData = Float.parseFloat(Objects.requireNonNull(dialogDpDataSize.getText()).toString());
                        dataPlanToCreate.totalUsedData = 0f;
                        dataPlanToCreate.totalAssignedData = 0f;

                        // Create the dataPlan.
                        if (dialogDataTypeSpinner.getSelectedItemPosition() == 0){
                            dataPlanToCreate.dataPlanType = DataPlan.DataPlanType.WIFI;
                        } else if (dialogDataTypeSpinner.getSelectedItemPosition() == 1){
                            dataPlanToCreate.dataPlanType = DataPlan.DataPlanType.MOBILE_DATA;
                        } else {
                            dataPlanToCreate.dataPlanType = DataPlan.DataPlanType.WIFI_MOBILE_DATA;
                        }

                        dataPlanToCreate.id = dataPlanToCreate.save();

                        final PackageManager pm = getContext().getPackageManager();
                        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);


                        Iterator<ApplicationInfo> it = packages.iterator();
                        while (it.hasNext()){
                            ApplicationInfo applicationInfo = it.next();

                            if (pm.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                                /*
                                if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
                                    // Updated System app
                                } else if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                                    // System Apps
                                } else {
                                    // User installed app
                                } */
                            } else {
                                it.remove();
                            }
                        }

                        for (int i = 0; i < packages.size(); i++){
                            CustomApp customApp = new CustomApp(getContext());

                            customApp.name = String.valueOf(pm.getApplicationLabel(packages.get(i))); // packages.get(i).packageName;
                            customApp.mPackageName = packages.get(i).packageName;

                            customApp.isEnabled = true;

                            customApp.dataPlanID = dataPlanToCreate.id;

                            customApp.isUnlimited = true;
                            customApp.totalData = 0f;
                            customApp.totalUsedData = 0f;

                            customApp.save();
                        }

                        Intent intent = new Intent(getContext(), DataPlanActivity.class);

                        // Transfer the contents of the new DataPlan.
                        intent.putExtra("id", dataPlanToCreate.id);

                        getContext().startActivity(intent);
                    }
                });

                dialogEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){

                            Calendar calendar = Calendar.getInstance();

                            year = calendar.get(Calendar.YEAR);
                            month = calendar.get(Calendar.MONTH);
                            day = calendar.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    myyear = year;
                                    myday = day;
                                    mymonth = month;

                                    Calendar c = Calendar.getInstance();
                                    hour = c.get(Calendar.HOUR);
                                    minute = c.get(Calendar.MINUTE);
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            myhour = hourOfDay;
                                            myminute = minute;

                                            DateTime dateTime = new DateTime(0, myminute, myhour, myyear,mymonth, myday);
                                            dialogEndTime.setText(dateTime.toString());
                                        }
                                    }, hour, minute, true);

                                    timePickerDialog.show();
                                }
                            }, year, month, day);

                            datePickerDialog.show();
                        }
                    }
                });

                dialogStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){

                            Calendar calendar = Calendar.getInstance();

                            year = calendar.get(Calendar.YEAR);
                            month = calendar.get(Calendar.MONTH);
                            day = calendar.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    myyear = year;
                                    myday = day;
                                    mymonth = month;

                                    Calendar c = Calendar.getInstance();
                                    hour = c.get(Calendar.HOUR);
                                    minute = c.get(Calendar.MINUTE);
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            myhour = hourOfDay;
                                            myminute = minute;

                                            DateTime dateTime = new DateTime(0, myminute, myhour, myyear,mymonth, myday);
                                            dialogStartTime.setText(dateTime.toString());
                                        }
                                    }, hour, minute, true);

                                    timePickerDialog.show();
                                }
                            }, year, month, day);

                            datePickerDialog.show();
                        }
                    }
                });

                createDpDialog.show();
            }
        });


        return rootView;
    }
}