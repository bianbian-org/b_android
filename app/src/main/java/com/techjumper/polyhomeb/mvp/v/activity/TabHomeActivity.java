package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.TabHomeActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.fragment.AppBaseFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.FriendFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.ShoppingFragment;
import com.techjumper.polyhomeb.widget.HomeViewPager;
import com.techjumper.polyhomeb.widget.PolyTab;
import com.techjumper.slidingmenulib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(TabHomeActivityPresenter.class)
public class TabHomeActivity extends AppBaseActivity<TabHomeActivityPresenter> {

    @Bind(R.id.view_tab_strip)
    PolyTab mTab;
    @Bind(R.id.vp)
    HomeViewPager mVp;


    public SlidingMenu mSlidingMenu;
    private List<AppBaseFragment> mFragments = new ArrayList<>();


    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_tab_home);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int offset = (int) (RuleUtils.getScreenWidth() * 0.374F);
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setBehindOffset(offset);
        mSlidingMenu.setFadeDegree(0.55f);
        mSlidingMenu.setTouchmodeMarginThreshold(RuleUtils.dp2Px(30));
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        mSlidingMenu.setMenu(R.layout.activity_home_menu);

        mTab.setOnTabClickListener(getPresenter());

        HomeFragment homeFragment = new HomeFragment();
        FriendFragment friendFragment = new FriendFragment();
        ShoppingFragment shoppingFragment = new ShoppingFragment();

        mFragments.add(homeFragment);
        mFragments.add(friendFragment);
        mFragments.add(shoppingFragment);

        FragmentAdapter adapter = new FragmentAdapter();
        mVp.setAdapter(adapter);
        mVp.setScanScroll(false);

        mVp.setCurrentItem(0);
        mTab.check(0);

    }

    @Override
    public String getLayoutTitle() {
        return "逸成东苑";
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
    }

    public void toggleMenu() {
        mSlidingMenu.toggle();
    }

    public HomeViewPager getHomeViewPager() {
        return mVp;
    }

    private class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
