package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.adapter.MyViewPagerAdapter;
import com.techjumper.polyhome.b.home.mvp.p.activity.MainActivityPresenter;
import com.techjumper.polyhome.b.home.mvp.v.fragment.InfoFragment;
import com.techjumper.polyhome.b.home.mvp.v.fragment.PloyhomeFragment;
import com.techjumper.polyhome.b.home.receiver.NoticeReceiver;
import com.techjumper.polyhome.b.home.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@Presenter(MainActivityPresenter.class)
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
    @Bind(R.id.date)
    TextView date;

    private MyViewPagerAdapter myViewPagerAdapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private NoticeReceiver receiver;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        date.setText(CommonDateUtil.getTitleDate());

        InfoFragment infoFragment = InfoFragment.getInstance();
        PloyhomeFragment ployhomeFragment = PloyhomeFragment.getInstance();

        fragments.add(ployhomeFragment);
        fragments.add(infoFragment);

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

