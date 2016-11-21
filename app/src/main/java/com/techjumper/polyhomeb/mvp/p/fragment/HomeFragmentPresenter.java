package com.techjumper.polyhomeb.mvp.p.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.doormaster.bluetoothEvent.BLEScanResultEvent;
import com.techjumper.polyhome.doormaster.bluetoothEvent.OpenDoorResult;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.HomePageAdapter;
import com.techjumper.polyhomeb.adapter.recycler_Data.BluetoothData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.BluetoothBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyDataBean;
import com.techjumper.polyhomeb.entity.MarqueeTextInfoEntity;
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
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeFragmentPresenter extends AppBaseFragmentPresenter<HomeFragment> {

    private HomeFragmentModel mModel = new HomeFragmentModel(this);

    private Subscription mSubs1, mSubs2;
    private PolyPluginManager mPluginManager;

    @Override
    public void initData(Bundle savedInstanceState) {
        mPluginManager = PolyPluginManager.with(getView().getActivity());
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        changeTitle();
        bleChangeInfo();
        getMarqueeTextData();
    }

    private void changeTitle() {
//        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE
                        .asObservable().subscribe(o -> {
                            if (o instanceof ChooseFamilyVillageEvent) {
                                getView().getTvTitle().setText(UserManager.INSTANCE.getCurrentTitle());
                                reloadPropertyData();
                                reloadMarqueeTextInfo();
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

    private void reloadPropertyData() {
        if (getView().getAdapter() == null) return;
        HomePageAdapter adapter = getView().getAdapter();
        List<DisplayBean> data = adapter.getData();
        if (data == null || data.size() == 0) return;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) instanceof PropertyDataBean) {
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    private void reloadMarqueeTextInfo() {
        getMarqueeTextData();
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

    public void refreshData() {
        getMarqueeTextData();
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
                                startService();
                                registShakeManager();
                            } else {
                                JLog.d("侧边栏发来的消息：需要取消注册摇一摇或者取消定时扫描服务");
                                stopService();
                                unRegistShakeManager();
                            }
                            refreshBLEView();
                        }
                    } else if (o instanceof OpenDoorResult) {
                        JLog.d("ViewHolder发来的消息：解锁成功了，需要注册摇一摇或者开启定时扫描服务-------");
                        startService();
                        registShakeManager();
                    } else if (o instanceof BLEScanResultEvent) {
                        BLEScanResultEvent event = (BLEScanResultEvent) o;
                        if (event.isHasDevice()) {
                            JLog.d("ViewHolder发来的消息：搜索到设备了，需要注册摇一摇或者开启定时扫描服务-------");
                            startService();
                            registShakeManager();
                        } else {
                            JLog.d("ViewHolder发来的消息：没有搜索到设备，需要取消注册摇一摇但是不关闭扫描服务");
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

    private void refreshBLEView() {
        HomePageAdapter adapter = getView().getAdapter();
        List<DisplayBean> data = adapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) instanceof BluetoothBean) {
                BluetoothBean bluetoothBean = (BluetoothBean) data.get(i);
                BluetoothData data1 = bluetoothBean.getData();
                data1.setInfosBeen(UserManager.INSTANCE.getBLEInfo());
                data1.setCommunitySupportBleDoor(UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor());
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPluginManager.quit();
    }

    private void getMarqueeTextData() {
//        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.getMarqueeText()
                        .subscribe(new Observer<MarqueeTextInfoEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                JLog.e(e.getMessage().toString());
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onNext(MarqueeTextInfoEntity marqueeTextInfoEntity) {
                                if (!processNetworkResult(marqueeTextInfoEntity)) return;
                                if (marqueeTextInfoEntity == null || marqueeTextInfoEntity.getData() == null
                                        || marqueeTextInfoEntity.getData().getMessages() == null
                                        || marqueeTextInfoEntity.getData().getMessages().size() == 0)
                                    return;
                                refreshMarqueeTextInfo(marqueeTextInfoEntity);
                            }
                        }));
    }

    private void refreshMarqueeTextInfo(MarqueeTextInfoEntity marqueeTextInfoEntity) {
        if (getView().getAdapter() == null) return;
        HomePageAdapter adapter = getView().getAdapter();
        List<DisplayBean> data = adapter.getData();
        if (data == null || data.size() == 0) return;
        for (int i = 0; i < data.size(); i++) {
            DisplayBean displayBean = data.get(i);
            if (displayBean instanceof PropertyDataBean) {
                PropertyData propertyData = ((PropertyDataBean) displayBean).getData();
                propertyData.setNotice(marqueeTextInfoEntity);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

}
