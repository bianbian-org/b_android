package com.techjumper.polyhomeb.mvp.v.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.HomePageAdapter;
import com.techjumper.polyhomeb.entity.event.ShakeToOpenDoorEvent;
import com.techjumper.polyhomeb.manager.ShakeManager;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeFragmentPresenter;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.service.ScanBluetoothService;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.widget.HomePtrClassicFrameLayout;
import com.techjumper.ptr_lib.PtrDefaultHandler;
import com.techjumper.ptr_lib.PtrFrameLayout;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(HomeFragmentPresenter.class)
public class HomeFragment extends AppBaseFragment<HomeFragmentPresenter>
        implements ShakeManager.ISensor {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.ptr)
    HomePtrClassicFrameLayout mPtr;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.right_tv)
    TextView mTvRight;

    private HomePageAdapter mAdapter;
    private boolean mIsFragmentVisible = true;
    private LinearLayoutManager mManager;

    private long mLastClickTime;

    /**
     * RV在Y轴上的滑动偏移量
     */
    private int mRvScrollYOffsetNew = 0;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvRight.setVisibility(View.VISIBLE);
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(mManager);
        mAdapter = new HomePageAdapter();
        mRv.setAdapter(mAdapter);
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //0表示停止滑动的状态 SCROLL_STATE_IDLE
                //1表示正在滚动，用户手指在屏幕上 SCROLL_STATE_TOUCH_SCROLL
                //2表示正在滑动。用户手指已经离开屏幕 SCROLL_STATE_FLING
                super.onScrollStateChanged(recyclerView, newState);
//                switch (newState) {
//                    case 1:
//                    case 2:
//                        JLog.e("暂停加载" + newState);
//                        break;
//                    case 0:
//                        JLog.e("恢复加载" + newState);
//                        break;
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = mManager.findFirstVisibleItemPosition();

                //position=0是因为设计稿上ViewPager就在第一个,定死的不会变,所以这里直接用0,否则需要根据数据来取位置
                int height = mManager.getChildAt(0).getHeight();

                mRvScrollYOffsetNew += dy;

                //发送停止播放的消息前提是：
                //滑动偏移量 == View高度 * 50%;此时发送RxBus，Item接收之后处理接下来的逻辑.
                //注意一个问题：以上条件满足的话，可能有两种情况，一种是向下滑，一种是向上滑，而像上滑的情况下是不需要发送RxBus
                if (dy > 0 && firstVisibleItemPosition == 0 && mRvScrollYOffsetNew > height * 0.5) {
                    getPresenter().sendMessage2ADBannerWhenRVScroll();
                }

            }
        });
        mAdapter.loadData(getPresenter().getDatas());
        mAdapter.setClickListener(() -> getPresenter().onSmartHomeClick());
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getPresenter().refreshData();
                new Handler().postDelayed(() -> stopRefresh(""), NetHelper.GLOBAL_TIMEOUT);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    /**
     * 重置RV在Y轴上的滑动偏移量.因为rv在item数量变化的时候，这个值会产生偏差，所以一旦item数目发生变化后，需要将此值重置
     */
    private void resetRvScrollYOffset() {
        mRvScrollYOffsetNew = 0;
    }

    @Override
    public String getTitle() {
        return UserManager.INSTANCE.getCurrentTitle();
    }

    public void stopRefresh(String msg) {
        if (mPtr != null && mPtr.isRefreshing()) {
            if (!TextUtils.isEmpty(msg))
                showHint(msg);
            mPtr.refreshComplete();
        }
    }

    public void notifyItemChanged(int position) {
        if (mAdapter != null)
            mAdapter.notifyItemChanged(position);
        resetRvScrollYOffset();
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public HomePageAdapter getAdapter() {
        return mAdapter;
    }

    public LinearLayoutManager getManager() {
        return mManager;
    }

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    @Override
    public void onSensorChange(float force) {
        if (force > 45 && !isFastDoubleClick()) {
            if (getActivity() != null) {
                getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
            }
            RxBus.INSTANCE.send(new ShakeToOpenDoorEvent());
        }
    }

    @Override
    public void onResume() {
        boolean isActivityVisible = ((TabHomeActivity) getActivity()).isTabHomeActivityVisible();
        boolean supportBLEDoor = UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor();
        if (isActivityVisible && supportBLEDoor && mIsFragmentVisible) {
            JLog.d("onResume:需要注册摇一摇或者启动定时扫描服务---------");
            getPresenter().startService();
            getPresenter().registShakeManager();
        } else {
            JLog.d("onResume:需要取消注册摇一摇或者取消定时扫描服务");
            getPresenter().stopService();
            getPresenter().unRegistShakeManager();
            getPresenter().sendMessage2ADBanner();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        JLog.d("onPause:需要取消注册摇一摇或者取消定时扫描服务");
        getPresenter().stopService();
        getPresenter().unRegistShakeManager();
        getPresenter().sendMessage2ADBanner();
        super.onPause();
    }

    @Override
    public void onStop() {
        JLog.d("onStop:需要取消注册摇一摇或者取消定时扫描服务");
        getPresenter().stopService();
        getPresenter().unRegistShakeManager();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        JLog.d("onDestroy:需要取消注册摇一摇或者取消定时扫描服务");
        getPresenter().stopService();
        getPresenter().unRegistShakeManager();
        getPresenter().sendMessage2ADBanner();
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsFragmentVisible = isVisibleToUser;
        if (getActivity() != null) {
            boolean supportBLEDoor = UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor();
            boolean isActivityVisible = ((TabHomeActivity) getActivity()).isTabHomeActivityVisible();
            if (mIsFragmentVisible && isActivityVisible && supportBLEDoor) {
                JLog.d("setUserVisibleHint:需要注册摇一摇或者启动定时扫描服务---------");
                getPresenter().startService();
                getPresenter().registShakeManager();
            } else {
                JLog.d("setUserVisibleHint:需要取消注册摇一摇或者取消定时扫描服务");
                getPresenter().stopService();
                getPresenter().unRegistShakeManager();
                getPresenter().sendMessage2ADBanner();
            }
        }
    }

}
