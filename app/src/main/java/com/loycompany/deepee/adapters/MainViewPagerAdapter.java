package com.loycompany.deepee.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;

import com.loycompany.deepee.fragments.ActiveDpFragment;
import com.loycompany.deepee.fragments.DpsFragment;
import com.loycompany.deepee.fragments.SettingsFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    final int ITEM_COUNT = 3;

    ActiveDpFragment activeDpFragment;
    DpsFragment dpsFragment;
    SettingsFragment settingsFragment;

    /**
     * Constructor for {@link FragmentPagerAdapter}.
     * <p>
     * If {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT} is passed in, then only the current
     * Fragment is in the {@link Lifecycle.State#RESUMED} state. All other fragments are capped at
     * {@link Lifecycle.State#STARTED}. If {@link #BEHAVIOR_SET_USER_VISIBLE_HINT} is passed, all
     * fragments are in the {@link Lifecycle.State#RESUMED} state and there will be callbacks to
     * {@link Fragment#setUserVisibleHint(boolean)}.
     *
     * @param fm       fragment manager that will interact with this adapter
     * @param context context that will interact with this adapter. The default behaviour is
     *                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
     */
    public MainViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        activeDpFragment = ActiveDpFragment.newInstance("", "");
        dpsFragment = DpsFragment.newInstance("", "");
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
