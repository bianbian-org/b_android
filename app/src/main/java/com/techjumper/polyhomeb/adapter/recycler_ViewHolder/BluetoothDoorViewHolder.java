package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.intelligoo.sdk.LibDevModel;
import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.doormaster.SmartDoorBluetoothManager;
import com.techjumper.polyhome.doormaster.bluetoothEvent.BLEScanResultEvent;
import com.techjumper.polyhome.doormaster.bluetoothEvent.OpenDoorResult;
import com.techjumper.polyhome.slide2unlock.interfaces.IUnLockViewState;
import com.techjumper.polyhome.slide2unlock.view.Slide2UnlockView;
import com.techjumper.polyhome.slide2unlock.view.enums.LockViewResult;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.BluetoothData;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.event.ScanDeviceEvent;
import com.techjumper.polyhomeb.entity.event.ShakeToOpenDoorEvent;
import com.techjumper.polyhomeb.manager.ShakeManager;
import com.techjumper.polyhomeb.service.ScanBluetoothService;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = BluetoothData.class, beanName = "BluetoothBean")
public class BluetoothDoorViewHolder extends BaseRecyclerViewHolder<BluetoothData>
        implements IUnLockViewState {

    public static final int LAYOUT_ID = R.layout.item_bluetooth_door;
    private Slide2UnlockView mView;
    private String sn;
    private List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> bleInfo = UserManager.INSTANCE.getBLEInfo();

    public BluetoothDoorViewHolder(View itemView) {
        super(itemView);
        mView = getView(R.id.lock_view);
        mView.setOnUnLockViewStateListener(this);
        mView.setDelayTime(1000);
        mView.setWaitTime(5000);
        mView.setTextUnusable("附近暂没有可用门锁");
        mView.setTextDefault("附近暂没有可用门锁");
        mView.setTextSuccess("解锁成功");
        mView.setTextFailed("解锁失败");
        mView.postDelayed(() -> {
            mView.setUsable(false);
            if (getContext() != null) {
                ShakeManager.with((getContext())).cancel();
                JLog.d("ViewHolder的消息：setUsable(false),需要取消注册摇一摇");
                getContext().stopService(new Intent(getContext(), ScanBluetoothService.class));
            }
        }, 200);
        //设备扫描的回调
        RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof BLEScanResultEvent) {
                BLEScanResultEvent event = (BLEScanResultEvent) o;
                if (event.isHasDevice()) {
                    sn = event.getSn();
                    mView.setText("滑动或摇一摇开门(" + getDoorNameBySn(sn) + ")");
                    mView.setUsable(true);
                } else {
                    mView.setUsable(false);
                    JLog.d("ViewHolder的消息：扫描设备之后的回调,需要取消注册摇一摇");
                    if (getContext() != null) {
                        ShakeManager.with((getContext())).cancel();
                        getContext().stopService(new Intent(getContext(), ScanBluetoothService.class));
                    }
                }
            }
        });

        //接收开锁成功或者失败的回调
        RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof OpenDoorResult) {
                OpenDoorResult result = (OpenDoorResult) o;
                boolean result1 = result.isResult();
                if (result1) {
                    mView.unLockResult(LockViewResult.SUCCESS);
                } else {
                    mView.unLockResult(LockViewResult.FAILED);
                }
            }
        });

        //摇一摇时候接收到的消息
        RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof ShakeToOpenDoorEvent) {
                if (mView != null && mView.isUsable()) {
                    mView.autoUnlock(1000, null);
                }
            }
        });

        RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof ScanDeviceEvent) {
                List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> bleInfo = UserManager.INSTANCE.getBLEInfo();
                if (bleInfo == null || bleInfo.size() == 0) return;
                List<LibDevModel> list = new ArrayList<>();
                for (BluetoothLockDoorInfoEntity.DataBean.InfosBean bean : bleInfo) {
                    LibDevModel model = new LibDevModel();
                    model.eKey = bean.getEkey();
                    model.devMac = bean.getMac();
                    model.devSn = bean.getSn();
                    model.devType = 0;
                    list.add(model);
                }
                SmartDoorBluetoothManager.with().scanBLEDevices((Activity) getContext(), list);
            }
        });
    }

    @Override
    public void setData(BluetoothData data) {
        if (data == null) return;
        //相当于Demo中,从云端获取到的"此账号"能开的锁的所有信息
        List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infosBeen = data.getInfosBeen();
        if (infosBeen == null || infosBeen.size() == 0) return;
        List<LibDevModel> list = new ArrayList<>();
        for (BluetoothLockDoorInfoEntity.DataBean.InfosBean bean : infosBeen) {
            LibDevModel model = new LibDevModel();
            model.eKey = bean.getEkey();
            model.devMac = bean.getMac();
            model.devSn = bean.getSn();
            model.devType = 0;
            list.add(model);
        }
        SmartDoorBluetoothManager.with().scanBLEDevices((Activity) getContext(), list);
    }

    @Override
    public void startUnLock(Slide2UnlockView view) {
        if (getContext() != null) {
            ShakeManager.with((getContext())).cancel();
            getContext().stopService(new Intent(getContext(), ScanBluetoothService.class));
        }
        JLog.d("ViewHolder的消息：开始解锁,需要取消注册摇一摇");
        ToastUtils.show("开始解锁");
        String mac = "";
        String ekey = "";
        if (bleInfo != null && bleInfo.size() != 0) {
            for (BluetoothLockDoorInfoEntity.DataBean.InfosBean bean : bleInfo) {
                if (bean.getSn().equalsIgnoreCase(sn)) {
                    ekey = bean.getEkey();
                    mac = bean.getMac();
                    break;
                }
            }
        }

        if (!isFastDoubleClick()) {
            SmartDoorBluetoothManager.with().unlock((Activity) getContext(), sn, mac, ekey);
        }

    }

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private long lastClickTime;

    @Override
    public void unLocking(Slide2UnlockView view) {
        ToastUtils.show("正在解锁中");
        JLog.d("ViewHolder的消息：正在解锁中,需要取消注册摇一摇");
        if (getContext() != null) {
            getContext().stopService(new Intent(getContext(), ScanBluetoothService.class));
            ShakeManager.with((getContext())).cancel();
        }
    }

    private String getDoorNameBySn(String sn) {
        String name = "";
        if (bleInfo != null && bleInfo.size() != 0) {
            for (BluetoothLockDoorInfoEntity.DataBean.InfosBean bean : bleInfo) {
                if (bean.getSn().equalsIgnoreCase(sn)) {
                    name = bean.getName();
                    break;
                }
            }
        }
        return name;
    }
}
