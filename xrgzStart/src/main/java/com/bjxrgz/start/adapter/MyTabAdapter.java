package com.bjxrgz.start.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016-11-9.
 * describe 通用tab适配器
 */
public class MyTabAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<String> titleList;
    private List<T> fragmentList;

    public MyTabAdapter(FragmentManager fm) {
        super(fm);
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
    }

    public void addData(String titles, T fragments) {
        // fragment
        fragmentList.add(fragments);
        // 标题
        titleList.add(titles);
        notifyDataSetChanged();
    }

    public void setData(List<String> titles, List<T> fragments) {
        // fragment
        fragmentList.clear();
        fragmentList.addAll(fragments);
        // 标题
        titleList.clear();
        titleList.addAll(titles);
        notifyDataSetChanged();
    }

    public List<T> getFragmentList() {
        return fragmentList;
    }

    public List<String> getTitleList() {
        return titleList;
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
