package com.example.vclasslogin.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.vclasslogin.R;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};    // all fragments with names


    private final Context mContext;
    private final String userType;
    private final String courseName;
    private final String userName;
    private final ArrayList<String> studentTaskTitles;

    // constructor - getting app context and other require data
    public SectionsPagerAdapter(Context context, FragmentManager fm, String userType, String courseName, String userName, ArrayList<String> studentTaskTitles) {
        super(fm);
        mContext = context;
        this.userType = userType;
        this.courseName = courseName;
        this.userName = userName;
        this.studentTaskTitles = studentTaskTitles;
    }

    // to display all fragments 1-by-1
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new OngoingTaskFragment(userType, courseName, userName, studentTaskTitles);
                break;
            case 1:
                fragment = new SubmittedTaskFragment(userType, courseName, userName, studentTaskTitles);
                break;
        }
        return fragment;
    }

    // to get fragment name
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    // to get number of total fragments
    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}