package com.joe.base.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.joe.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/***
 * Joe
 */
public class MyPagerAdapter<F extends Fragment> extends FragmentPagerAdapter {
    private List<F> mFragments;
    private final String[] mTitles;
    private F mCurrentFragment; // 当前的Fragment

    public MyPagerAdapter(FragmentManager fm, List<F> mFragments, String[] mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public F getItem(int position) {
        return mFragments.get(position);
    }
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            // 记录当前的Fragment对象
            mCurrentFragment = (F) object;
        }
        super.setPrimaryItem(container, position, object);
    }
    /**
     * 获取当前的Fragment
     */
    public F getCurrentFragment() {
        return mCurrentFragment;
    }
}
