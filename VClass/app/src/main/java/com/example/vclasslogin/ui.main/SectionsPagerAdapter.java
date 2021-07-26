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
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private String userType;
    private String courseName;
    private String userName;
    private ArrayList<String> studentTaskTitles;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String userType, String courseName, String userName, ArrayList<String> studentTaskTitles) {
        super(fm);
        mContext = context;
        this.userType = userType;
        this.courseName = courseName;
        this.userName = userName;
        this.studentTaskTitles = studentTaskTitles;
    }

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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}