package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.corelib.utils.window.StatusbarHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.FragmentAdapter;
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
        View contentRoot = findViewById(android.R.id.content);
        //适配虚拟按键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !AppUtils.isMeizu()) {
            contentRoot.setPadding(contentRoot.getLeft(), contentRoot.getPaddingTop(), contentRoot.getRight()
                    , contentRoot.getBottom() + StatusbarHelper.getNavigationBarHeight(this));
        }
        return inflate(R.layout.activity_tab_home);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initSlidingMenu();
        initFragmentsAndPager();

    }

    @Override
    public String getLayoutTitle() {
        return "逸成东苑";
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
    }

    private void initSlidingMenu() {
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
    }

    private void initFragmentsAndPager() {
        mTab.setOnTabClickListener(getPresenter());

        mFragments.add(HomeFragment.getInstance());
        mFragments.add(FriendFragment.getInstance());
        mFragments.add(ShoppingFragment.getInstance());

        FragmentAdapter adapter = new FragmentAdapter(this, mFragments);
        mVp.setAdapter(adapter);
        mVp.setScanScroll(false);

        mVp.setCurrentItem(0);
        mTab.check(0);
    }

    public void toggleMenu() {
        mSlidingMenu.toggle();
    }

    public HomeViewPager getHomeViewPager() {
        return mVp;
    }

}
