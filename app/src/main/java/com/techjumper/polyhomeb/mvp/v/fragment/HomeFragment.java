package com.techjumper.polyhomeb.mvp.v.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.HomePageAdapter;
import com.techjumper.polyhomeb.entity.event.ShakeToOpenDoorEvent;
import com.techjumper.polyhomeb.manager.ShakeManager;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeFragmentPresenter;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
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
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new HomePageAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());
        mAdapter.setClickListener(() -> getPresenter().onSmartHomeClick());
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new Handler().postDelayed(() -> stopRefresh(""), 3000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    @Override
    public String getTitle() {
        return UserManager.INSTANCE.getCurrentTitle();
    }

    private void stopRefresh(String msg) {
        if (mPtr != null && mPtr.isRefreshing()) {
            if (!TextUtils.isEmpty(msg))
                showHint(msg);
            mPtr.refreshComplete();
        }
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public HomePageAdapter getAdapter() {
        return mAdapter;
    }

    private long lastClickTime;

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onSensorChange(float force) {
        //如果View是不可用状态，那么直接return
        if (force > 35 && !isFastDoubleClick()) {
            if (getActivity() != null) {
                getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
            }
            ToastUtils.show("摇一摇开锁中...");
            RxBus.INSTANCE.send(new ShakeToOpenDoorEvent());
        }
    }

    @Override
    public void onDestroy() {
        JLog.d("需要取消注册摇一摇或者取消定时扫描服务");
        if (getActivity() != null) {
            ShakeManager.with(getActivity()).cancel();
            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        JLog.d("需要取消注册摇一摇或者取消定时扫描服务");
        if (getActivity() != null) {
            ShakeManager.with(getActivity()).cancel();
            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        JLog.d("需要取消注册摇一摇或者取消定时扫描服务");
        if (getActivity() != null) {
            ShakeManager.with(getActivity()).cancel();
            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        boolean isActivityVisible = ((TabHomeActivity) getActivity()).isTabHomeActivityVisible();
        boolean supportBLEDoor = UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor();
        if (getActivity() != null && isActivityVisible && supportBLEDoor && mIsFragmentVisible) {
            ShakeManager.with(getActivity()).startShake(this);
            JLog.d("需要注册摇一摇或者启动定时扫描服务---------");
            getActivity().startService(new Intent(getActivity(), ScanBluetoothService.class));
        } else {
            ShakeManager.with(getActivity()).cancel();
            JLog.d("需要取消注册摇一摇或者取消定时扫描服务");
            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
        }
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsFragmentVisible = isVisibleToUser;
        if (getActivity() != null) {
            boolean supportBLEDoor = UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor();
            boolean isActivityVisible = ((TabHomeActivity) getActivity()).isTabHomeActivityVisible();
            if (mIsFragmentVisible && isActivityVisible && supportBLEDoor) {
                JLog.d("需要注册摇一摇或者启动定时扫描服务---------");
                ShakeManager.with(getActivity()).startShake(this);
                getActivity().startService(new Intent(getActivity(), ScanBluetoothService.class));
            } else {
                JLog.d("需要取消注册摇一摇或者取消定时扫描服务");
                ShakeManager.with(getActivity()).cancel();
                getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
            }
        }
    }
}
