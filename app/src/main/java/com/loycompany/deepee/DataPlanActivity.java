package com.loycompany.deepee;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DataPlanActivity extends AppCompatActivity {

    Spinner dataTypeSpinner;

    PieChartView pieChartView;
    List<SliceValue> pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_plan);

        dataTypeSpinner = findViewById(R.id.data_type_spinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.data_type, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataTypeSpinner.setAdapter(arrayAdapter);
        dataTypeSpinner.setSelection(0);

        pieChartView = findViewById(R.id.pie_chart_view);

        pieData = new ArrayList<>();

        pieData.add(new SliceValue(15, Color.BLUE));
        pieData.add(new SliceValue(45, Color.GREEN));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartView.setPieChartData(pieChartData);
    }
}