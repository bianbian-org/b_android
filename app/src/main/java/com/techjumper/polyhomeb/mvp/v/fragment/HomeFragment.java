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
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.HomePageAdapter;
import com.techjumper.polyhomeb.entity.event.LifeCycleEvent;
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
                getPresenter().refreshData();
                new Handler().postDelayed(() -> stopRefresh(""), NetHelper.GLOBAL_TIMEOUT);
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

    public void stopRefresh(String msg) {
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
//            ShakeManager.with(getActivity()).startShake(this);
//            getActivity().startService(new Intent(getActivity(), ScanBluetoothService.class));
            getPresenter().startService();
            getPresenter().registShakeManager();
        } else {
            JLog.d("onResume:需要取消注册摇一摇或者取消定时扫描服务");
//            ShakeManager.with(getActivity()).cancel();
//            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
            getPresenter().stopService();
            getPresenter().unRegistShakeManager();
            sendMessage2ADBanner();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        JLog.d("onPause:需要取消注册摇一摇或者取消定时扫描服务");
//        if (getActivity() != null) {
//            ShakeManager.with(getActivity()).cancel();
//            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
//        }
        getPresenter().stopService();
        getPresenter().unRegistShakeManager();
        sendMessage2ADBanner();
        super.onPause();
    }

    @Override
    public void onStop() {
        JLog.d("onStop:需要取消注册摇一摇或者取消定时扫描服务");
//        if (getActivity() != null) {
//            ShakeManager.with(getActivity()).cancel();
//            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
//        }
        getPresenter().stopService();
        getPresenter().unRegistShakeManager();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        JLog.d("onDestroy:需要取消注册摇一摇或者取消定时扫描服务");
//        if (getActivity() != null) {
//            ShakeManager.with(getActivity()).cancel();
//            getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
//        }
        getPresenter().stopService();
        getPresenter().unRegistShakeManager();
        sendMessage2ADBanner();
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
//                ShakeManager.with(getActivity()).startShake(this);
//                getActivity().startService(new Intent(getActivity(), ScanBluetoothService.class));
                getPresenter().startService();
                getPresenter().registShakeManager();
            } else {
                JLog.d("setUserVisibleHint:需要取消注册摇一摇或者取消定时扫描服务");
//                ShakeManager.with(getActivity()).cancel();
//                getActivity().stopService(new Intent(getActivity(), ScanBluetoothService.class));
                getPresenter().stopService();
                getPresenter().unRegistShakeManager();
                sendMessage2ADBanner();
            }
        }
    }

    private void sendMessage2ADBanner() {
        RxBus.INSTANCE.send(new LifeCycleEvent());
    }
}
