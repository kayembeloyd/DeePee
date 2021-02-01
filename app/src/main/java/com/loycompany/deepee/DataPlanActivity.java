package com.loycompany.deepee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.database.DeePeeDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DataPlanActivity extends AppCompatActivity {

    Spinner dataTypeSpinner;

    PieChartView pieChartView;
    List<SliceValue> pieData;

    private DeePeeDatabase deePeeDatabase;
    private SQLiteDatabase mDb;

    Button activateDataPlan;
    TextView percentDataUsed, dataUsed;

    TextInputEditText dpName, dpDataSize;
    TextInputEditText dpStartTime, dpEndTime;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_plan);

        activateDataPlan = findViewById(R.id.activate_plan_btn);
        percentDataUsed = findViewById(R.id.percent_data_used);
        dataUsed = findViewById(R.id.data_used);

        dpName = findViewById(R.id.dp_name);
        dpDataSize = findViewById(R.id.dp_data_size);
        dpStartTime = findViewById(R.id.dp_start_time);
        dpEndTime = findViewById(R.id.dp_end_time);

        toolbar = findViewById(R.id.toolbar);

        // Get id of DataPlan
        int dataPlanId = Objects.requireNonNull(getIntent().getExtras()).getInt("id", 0);

        // Initialize Database
        deePeeDatabase = new DeePeeDatabase(getApplicationContext());

        try {
            deePeeDatabase.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = deePeeDatabase.getWritableDatabase();

        dataTypeSpinner = findViewById(R.id.data_type_spinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.data_type, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataTypeSpinner.setAdapter(arrayAdapter);
        dataTypeSpinner.setSelection(0);

        // Load the dataPlan from database
        if (dataPlanId != 0){
            DataPlan dataPlan = deePeeDatabase.dataPlan(dataPlanId);

            if (dataPlan != null){
                dpName.setText(dataPlan.name);
                dpDataSize.setText(String.valueOf(dataPlan.totalData));
                dpStartTime.setText(dataPlan.startDateTime.toString());
                dpEndTime.setText(dataPlan.endDateTime.toString());

                if (dataPlan.dataPlanType == DataPlan.DataPlanType.WIFI){
                    dataTypeSpinner.setSelection(0);
                } else if (dataPlan.dataPlanType == DataPlan.DataPlanType.MOBILE_DATA){
                    dataTypeSpinner.setSelection(1);
                } else {
                    dataTypeSpinner.setSelection(2);
                }

                toolbar.setTitle(dataPlan.name);

            } else {
                /// Unable to load dataPlan
            }
        } else {
            // That's a default
        }

        pieChartView = findViewById(R.id.pie_chart_view);

        pieData = new ArrayList<>();

        pieData.add(new SliceValue(60, Color.rgb(0,104,56)));
        pieData.add(new SliceValue(40, Color.rgb(230,231,232)));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartView.setPieChartData(pieChartData);
    }
}