package com.techjumper.polyhomeb.mvp.p.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.doormaster.bluetoothEvent.BLEScanResultEvent;
import com.techjumper.polyhome.doormaster.bluetoothEvent.OpenDoorResult;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.BLEInfoChangedEvent;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.manager.PolyPluginManager;
import com.techjumper.polyhomeb.manager.ShakeManager;
import com.techjumper.polyhomeb.mvp.m.HomeFragmentModel;
import com.techjumper.polyhomeb.mvp.v.activity.CheckInActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeFragment;
import com.techjumper.polyhomeb.service.ScanBluetoothService;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;

import java.util.List;

import butterknife.OnClick;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeFragmentPresenter extends AppBaseFragmentPresenter<HomeFragment> {

    private HomeFragmentModel mModel = new HomeFragmentModel(this);

    private Subscription mSubs1;
    private PolyPluginManager mPluginManager;

    @Override
    public void initData(Bundle savedInstanceState) {
        mPluginManager = PolyPluginManager.with(getView().getActivity());
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        changeTitle();
        bleChangeInfo();
    }

    private void changeTitle() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE
                        .asObservable().subscribe(o -> {
                            if (o instanceof ChooseFamilyVillageEvent) {
                                getView().getTvTitle().setText(UserManager.INSTANCE.getCurrentTitle());
                            } else if (o instanceof LoginEvent) {  //主要是因为用户1直接点击退出,此时到了登录界面,用户2登陆了.如果不做这个操作,那么就会导致用户2登陆之后显示的依然是用户1的title
                                //这里和HomeMenuFragmentPresenter中一样的道理
                                LoginEvent event = (LoginEvent) o;
                                boolean login = event.isLogin();
                                if (login) {
                                    getView().getTvTitle().setText(UserManager.INSTANCE.getCurrentTitle());
                                }
                            }
                        }));
    }

    @OnClick({R.id.iv_left_icon, R.id.right_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_icon:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case R.id.right_tv:
                new AcHelper.Builder(getView().getActivity())
                        .target(CheckInActivity.class)
                        .start();
                break;
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.initPropertyData();
    }

    private void bleChangeInfo() {
        RxBus.INSTANCE.asObservable()
                .subscribe(o -> {
                    if (o instanceof BLEInfoChangedEvent) {
                        if (getView().getAdapter() != null) {
                            if (UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor()) {
                                JLog.d("侧边栏发来的消息：需要注册摇一摇或者开启定时扫描服务-------");
//                                if (getView().getActivity() != null) {
//                                    ShakeManager.with(getView().getActivity()).startShake(getView());
//                                    getView().getActivity().startService(new Intent(getView().getActivity(), ScanBluetoothService.class));
//                                }
                                startService();
                                registShakeManager();
                            } else {
                                JLog.d("侧边栏发来的消息：需要取消注册摇一摇或者取消定时扫描服务");
//                                if (getView().getActivity() != null) {
//                                    ShakeManager.with(getView().getActivity()).cancel();
//                                    getView().getActivity().stopService(new Intent(getView().getActivity(), ScanBluetoothService.class));
//                                }
                                stopService();
                                unRegistShakeManager();
                            }
                            getView().getAdapter().loadData(getDatas());
                        }
                    } else if (o instanceof OpenDoorResult) {
                        JLog.d("ViewHolder发来的消息：解锁成功了，需要注册摇一摇或者开启定时扫描服务-------");
//                        if (getView().getActivity() != null) {
//                            ShakeManager.with(getView().getActivity()).startShake(getView());
//                            getView().getActivity().startService(new Intent(getView().getActivity(), ScanBluetoothService.class));
//                        }
                        startService();
                        registShakeManager();
                    } else if (o instanceof BLEScanResultEvent) {
                        BLEScanResultEvent event = (BLEScanResultEvent) o;
                        if (event.isHasDevice()) {
                            JLog.d("ViewHolder发来的消息：搜索到设备了，需要注册摇一摇或者开启定时扫描服务-------");
//                            if (getView().getActivity() != null) {
//                                ShakeManager.with(getView().getActivity()).startShake(getView());
//                                getView().getActivity().startService(new Intent(getView().getActivity(), ScanBluetoothService.class));
//                            }
                            startService();
                            registShakeManager();
                        } else {
                            JLog.d("ViewHolder发来的消息：没有搜索到设备，需要取消注册摇一摇但是不关闭扫描服务");
//                            if (getView().getActivity() != null) {
//                                ShakeManager.with(getView().getActivity()).cancel();
//                            }
                            unRegistShakeManager();
                        }
                    }
                });
    }

    public void onSmartHomeClick() {
        if (!UserManager.INSTANCE.isFamily()) {
            ToastUtils.showLong(Utils.appContext.getString(R.string.error_to_open_smarthome_due_not_family));
            return;
        }

        mPluginManager.openSmartHome(new PolyPluginManager.IStartPluginListener() {
            @Override
            public void onPluginLoading() {
                getView().showLoading();
            }

            @Override
            public void onPluginInstalling() {
                getView().dismissLoading();
            }

            @Override
            public void onPluginStarted() {
                getView().dismissLoading();
            }

            @Override
            public void onPluginError(Throwable e) {
                getView().dismissLoading();
            }
        });
    }

    public void registShakeManager() {
        if (getView().getActivity() != null) {
            if (!AppUtils.isCellPhoneSupportBLE(getView().getActivity())) return;
            ShakeManager.with(getView().getActivity()).startShake(getView());
        }
    }

    public void unRegistShakeManager() {
        if (getView().getActivity() != null) {
            if (!AppUtils.isCellPhoneSupportBLE(getView().getActivity())) return;
            ShakeManager.with(getView().getActivity()).cancel();
        }
    }

    public void startService() {
        if (getView().getActivity() != null) {
            if (!AppUtils.isCellPhoneSupportBLE(getView().getActivity())) return;
            getView().getActivity().startService(new Intent(getView().getActivity(), ScanBluetoothService.class));
        }
    }

    public void stopService() {
        if (getView().getActivity() != null) {
            if (!AppUtils.isCellPhoneSupportBLE(getView().getActivity())) return;
            getView().getActivity().stopService(new Intent(getView().getActivity(), ScanBluetoothService.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPluginManager.quit();
    }

}
