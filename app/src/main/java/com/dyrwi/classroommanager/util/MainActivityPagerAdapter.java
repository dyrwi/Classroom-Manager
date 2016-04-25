package com.dyrwi.classroommanager.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.fragments.AdvancedClassroomListFrag;
import com.dyrwi.classroommanager.fragments.AdvancedStudentListFrag;
import com.dyrwi.classroommanager.fragments.FeedFrag;

/**
 * Created by Ben on 14-Oct-15.
 */
public class MainActivityPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{
    public static int NUM_OF_ITEMS = 3;
    private String[] tabTitles = new String[]{"Feed", "Students", "Classrooms"};
    private int[] iconIdList = new int[]{R.drawable.feed, R.drawable.students, R.drawable.classroom};

    public MainActivityPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_OF_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FeedFrag.newInstance();
            case 1:
                return AdvancedStudentListFrag.newInstance();
            case 2:
                return AdvancedClassroomListFrag.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getPageIconResId(int i) {
        return iconIdList[i];
    }
}
