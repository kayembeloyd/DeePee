package com.loycompany.deepee;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.loycompany.deepee.adapters.MainViewPagerAdapter;
import com.loycompany.deepee.services.MyVpnService;
import com.loycompany.deepee.threads.DatagramSocketThread;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    DatagramSocketThread mDatagramSocketThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatagramSocketThread = new DatagramSocketThread();
        mDatagramSocketThread.start();
        Intent intent1 = VpnService.prepare(this);
        if (intent1 != null){
            // Sizilibho
            startActivityForResult(intent1, 0);
        } else {
            onActivityResult(0, RESULT_OK, null);
        }

        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(),getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            Toast.makeText(getApplicationContext(), "Permission to see other app data usage has been granted", Toast.LENGTH_SHORT).show();

            tabLayout =  findViewById(R.id.tabs);
            viewPager =  findViewById(R.id.view_pager);

            mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(mainViewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            Toast.makeText(getApplicationContext(), "Permission denied to see other app data usage has been granted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
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
        if (tabLayout == null || viewPager == null){
            super.onBackPressed();
        } else {
            if (tabLayout.getSelectedTabPosition() == 2 || tabLayout.getSelectedTabPosition() == 1){
                viewPager.setCurrentItem(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Intent intent1 = new Intent(this, MyVpnService.class);

            startService(intent1);
        }
    }
}