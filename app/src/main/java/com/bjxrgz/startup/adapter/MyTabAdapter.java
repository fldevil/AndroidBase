package com.bjxrgz.startup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016-11-9.
 * describe 通用tab适配器
 */

public class MyTabAdapter extends FragmentPagerAdapter {

    private List<String> titleList;
    private List<Fragment> fragmentList;

    public MyTabAdapter(FragmentManager fm) {
        super(fm);
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
    }

    public void setData(List data) {
        if (data == null || data.size() == 0) {
            return;
        }
        // fragment
        fragmentList.clear();
        // ... 1. newFragment  2. addFragment
        // 标题
        titleList.clear();
        // ... 1. getTitle  2. addTitle
        notifyDataSetChanged();
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList != null && titleList.size() > position) {
            return titleList.get(position);
        } else {
            return "";
        }
    }
}
