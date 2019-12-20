package com.wzg.core.uiKit;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wzg on 2015/12/1.
 */
public class FragAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private FragmentManager fragmentManager;
    private String[] titles;


    public FragAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentManager = fm;
        mFragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null) {
            return "";
        }
        return titles[position];
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void setList(List<Fragment> fragments) {
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }
}
