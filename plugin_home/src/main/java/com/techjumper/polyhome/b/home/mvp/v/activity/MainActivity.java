package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.adapter.MyViewPagerAdapter;
import com.techjumper.polyhome.b.home.mvp.p.activity.MainActivityPresenter;
import com.techjumper.polyhome.b.home.mvp.v.fragment.InfoFragment;
import com.techjumper.polyhome.b.home.mvp.v.fragment.PloyhomeFragment;
import com.techjumper.polyhome.b.home.receiver.NoticeReceiver;
import com.techjumper.polyhome.b.home.widget.MyVideoView;
import com.techjumper.polyhome.b.home.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

@Presenter(MainActivityPresenter.class)
public class MainActivity extends AppBaseActivity {

    public static final String ACTION_HOME_HEARTBEAT = "action_home_heartbeat";
    public static final String ACTION_HOME_HEARTBEAT_RECEIVE = "action_home_heartbeat_receive";

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
    @Bind(R.id.main_ad_video)
    MyVideoView mainAdVideo;
    @Bind(R.id.main_ad_img)
    ImageView mainAdImg;
    @Bind(R.id.main_ad_layout)
    FrameLayout mainAdLayout;
    @Bind(R.id.main_content_layout)
    LinearLayout mainContentLayout;

    private MyViewPagerAdapter myViewPagerAdapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Timer timer = new Timer();
    private HeartbeatReceiver mheartbeatReceiver = new HeartbeatReceiver();

    public MyVideoView getMainAdVideo() {
        return mainAdVideo;
    }

    public void setMainAdVideo(MyVideoView mainAdVideo) {
        this.mainAdVideo = mainAdVideo;
    }

    public ImageView getMainAdImg() {
        return mainAdImg;
    }

    public void setMainAdImg(ImageView mainAdImg) {
        this.mainAdImg = mainAdImg;
    }

    public FrameLayout getMainAdLayout() {
        return mainAdLayout;
    }

    public void setMainAdLayout(FrameLayout mainAdLayout) {
        this.mainAdLayout = mainAdLayout;
    }

    public LinearLayout getMainContentLayout() {
        return mainContentLayout;
    }

    public void setMainContentLayout(LinearLayout mainContentLayout) {
        this.mainContentLayout = mainContentLayout;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_main);
    }

    public TextView getDate() {
        return date;
    }

    public static class HeartbeatReceiver extends BroadcastReceiver {
        private Intent mIntent = new Intent(ACTION_HOME_HEARTBEAT_RECEIVE);

        @Override
        public void onReceive(Context context, Intent intent) {
            context.sendBroadcast(mIntent);
        }
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

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RxBus.INSTANCE.send(new TimeEvent());
            }
        }, 5000, 60000);

        IntentFilter intentFilter = new IntentFilter(ACTION_HOME_HEARTBEAT);
        registerReceiver(mheartbeatReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }

        if (mheartbeatReceiver != null) {
            unregisterReceiver(mheartbeatReceiver);
        }

        System.exit(0);
    }
}

