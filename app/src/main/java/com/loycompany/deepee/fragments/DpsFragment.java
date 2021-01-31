package com.loycompany.deepee.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loycompany.deepee.DataPlanActivity;
import com.loycompany.deepee.R;
import com.loycompany.deepee.adapters.MainDataPlanRecyclerViewAdapter;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.classes.DateTime;
import com.loycompany.deepee.database.DeePeeDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DpsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DpsFragment extends Fragment {

    List<DataPlan> dataPlanList;

    MainDataPlanRecyclerViewAdapter mainDataPlanRecyclerViewAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    Dialog createDpDialog;
    ExtendedFloatingActionButton createDpFabBtn;

    Spinner dialogDataTypeSpinner;
    Button dialogCreateDataPlanButton;

    TextInputEditText dialogStartTime, dialogEndTime;

    int day, month, year, hour, minute;
    int myday, mymonth, myyear, myhour, myminute;

    private DeePeeDatabase deePeeDatabase;
    private SQLiteDatabase mDb;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        dataPlanList = deePeeDatabase.getDataPlans();
        if (dataPlanList == null) dataPlanList = new ArrayList<>();

        // for (int i = 0; i < 10; i++){
            /*DataPlan dataPlan = new DataPlan(context);

            dataPlan.name = "Sample DataPlan - " + i;
            dataPlan.totalData = 200;
            dataPlan.totalAssignedData = 123;
            dataPlan.totalUsedData = 43;

            dataPlan.startDateTime = new DateTime(0,34,1,2021,4,3);
            dataPlan.endDateTime = new DateTime(45,30,3,2021,4,3);

            dataPlan.save();

            dataPlanList.add(dataPlan);*/
        // }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                createDpDialog = new Dialog(Objects.requireNonNull(getContext()));

                createDpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                createDpDialog.setCancelable(true);
                createDpDialog.setContentView(R.layout.dialog_create_dp);

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

                        Intent intent = new Intent(getContext(), DataPlanActivity.class);
                        getContext().startActivity(intent);
                    }
                });

                dialogStartTime = createDpDialog.findViewById(R.id.dialog_start_time_button);
                dialogEndTime = createDpDialog.findViewById(R.id.dialog_end_time_button);

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
                                        }
                                    }, hour, minute, true);
                                    timePickerDialog.show();
                                }
                            }, year, month, day);

                            datePickerDialog.show();
                        }
                    }
                });

                /*dialogStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                    }
                                }, hour, minute, true);
                                timePickerDialog.show();
                            }
                        }, year, month, day);

                        datePickerDialog.show();
                    }
                });*/

                createDpDialog.show();
            }
        });


        return rootView;
    }
}