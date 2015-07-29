package com.tuesda.watch.activities.homefragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglei on 15/7/28.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<HomeFragment> fragmentList = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm, List<HomeFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
