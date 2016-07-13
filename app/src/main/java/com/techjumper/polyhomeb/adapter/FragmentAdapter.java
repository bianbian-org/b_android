package com.techjumper.polyhomeb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.techjumper.polyhomeb.mvp.v.activity.AppBaseActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.AppBaseFragment;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<AppBaseFragment> mDataList;
    private AppBaseActivity mAc;

    public FragmentAdapter(AppBaseActivity mAc, List<AppBaseFragment> mDataList) {
        super(mAc.getSupportFragmentManager());
        this.mDataList = mDataList;
        this.mAc = mAc;
    }

    @Override
    public Fragment getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }
}
