package com.techjumper.polyhome.mvp.v.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.polyhome.R;
import com.techjumper.polyhome.adapter.MyViewPagerAdapter;
import com.techjumper.polyhome.mvp.v.fragment.InfoFragment;
import com.techjumper.polyhome.mvp.v.fragment.PloyhomeFragment;
import com.techjumper.polyhome.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppBaseActivity {

    @Bind(R.id.viewpager)
    MyViewPager viewpager;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.title_img)
    ImageView titleImg;
    @Bind(R.id.dot_home)
    ImageView dotHome;
    @Bind(R.id.dot_point)
    ImageView dotPoint;

    private MyViewPagerAdapter myViewPagerAdapter;
    private List<Fragment> fragments;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //去除虚拟栏
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setAttributes(params);

        InfoFragment infoFragment = new InfoFragment();
        PloyhomeFragment ployhomeFragment = new PloyhomeFragment();

        fragments.add(infoFragment);
        fragments.add(ployhomeFragment);

        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(myViewPagerAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setSlide(false);

        dotHome.setEnabled(true);
        dotPoint.setEnabled(false);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    titleImg.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                    dotHome.setEnabled(true);
                    dotPoint.setEnabled(false);
                } else if (position == 1) {
                    titleImg.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    dotHome.setEnabled(false);
                    dotPoint.setEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

