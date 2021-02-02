package com.loycompany.deepee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

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

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        if (tabLayout.getSelectedTabPosition() == 2 || tabLayout.getSelectedTabPosition() == 1){
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
}