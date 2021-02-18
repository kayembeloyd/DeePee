package com.loycompany.deepee.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.loycompany.deepee.fragments.ActiveDpFragment;
import com.loycompany.deepee.fragments.DpsFragment;
import com.loycompany.deepee.fragments.SettingsFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    final int ITEM_COUNT = 3;

    ActiveDpFragment activeDpFragment;
    DpsFragment dpsFragment;
    SettingsFragment settingsFragment;

    public MainViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        activeDpFragment = ActiveDpFragment.newInstance(context);
        dpsFragment = DpsFragment.newInstance(context);
        settingsFragment = SettingsFragment.newInstance("", "");
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return activeDpFragment;
            case 1:
                return dpsFragment;
            case 2:
                return settingsFragment;
            default:
                break;
        }

        return activeDpFragment;
    }

    @Override
    public int getCount() {
        return this.ITEM_COUNT;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Active DP";
            case 1:
                return "DPS";
            case 2:
                return "Settings";
        }
        return null;
    }
}
