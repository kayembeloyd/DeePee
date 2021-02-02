package com.loycompany.deepee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.loycompany.deepee.adapters.MainViewPagerAdapter;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.database.DeePeeDatabase;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout =  findViewById(R.id.tabs);
        viewPager =  findViewById(R.id.view_pager);

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}