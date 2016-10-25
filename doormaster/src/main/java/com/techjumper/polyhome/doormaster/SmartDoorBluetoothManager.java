package com.techjumper.polyhome.doormaster;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallback;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.doormaster.bluetoothEvent.BLEScanResultEvent;
import com.techjumper.polyhome.doormaster.bluetoothEvent.OpenDoorResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SmartDoorBluetoothManager {

    private static SmartDoorBluetoothManager sSmartDoorBluetoothManager;

    private Map<String, Integer> scanDevices = new HashMap<>();
    private Map<String, Integer> canOpenDevices = new TreeMap<>();

    private SmartDoorBluetoothManager() {
    }

    public static SmartDoorBluetoothManager with() {
        if (sSmartDoorBluetoothManager == null) {
            sSmartDoorBluetoothManager = new SmartDoorBluetoothManager();
        }
        return sSmartDoorBluetoothManager;
    }

    public void scanBLEDevices(final Activity context, final List<LibDevModel> existsDatas) {
        scanDevices.clear();
        canOpenDevices.clear();
        ScanCallback callback = new ScanCallback() {
            @Override
            public void onScanResult(final ArrayList<String> deviceList, final ArrayList<Integer> rssis) {

                //如果null或者=0，说明没有扫描到可用设备
                if (deviceList == null || deviceList.size() == 0 || rssis == null || rssis.size() == 0)
                    return;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //1将扫描完后所有被扫描到的设备装进map
                        //得到scanDevices
                        for (int i = 0; i < deviceList.size(); i++) {
                            scanDevices.put(deviceList.get(i), rssis.get(i));
                        }
                        //2将用户已有的(sp)数据遍历，比对刚才的map中的sn;如果有sn相同，说明用户能开扫到的这把锁，反之则不能开
                        //得到canOpenDevices
                        for (LibDevModel model : existsDatas) {
                            for (String scanDevice : scanDevices.keySet()) {
                                if (model.devSn.equalsIgnoreCase(scanDevice)) {
                                    canOpenDevices.put(scanDevice, scanDevices.get(scanDevice));
                                }
                            }
                        }
                        //3将所有能开的锁存下来，拿去比对信号强弱，得到信号最强的那把锁的sn，则就是距离用户最近的锁.
                        //得到距离用户最近的门锁的sn
                        String closedDevice = getClosedDeviceSn();
                        RxBus.INSTANCE.send(new BLEScanResultEvent(!TextUtils.isEmpty(closedDevice), closedDevice));
                    }
                });
            }

            @Override
            public void onScanResultAtOnce(final String devSn, final int rssi) {

            }
        };
        //上下文,扫描到设备立即返回与否,扫描时间,扫描结束后的回调
        int ret = LibDevModel.scanDevice(context, false, 4, callback);
        if (ret == 0x00) {
            //"扫描"这个消息 发送成功
            JLog.e("\"扫描\"这个消息 发送成功 开始执行扫描设备的指令");
        } else {
            //"扫描"这个消息 发送失败
            JLog.e("\"扫描\"这个消息 发送失败 没有执行扫描设备的指令");
        }
    }

    public void unlock(final Activity context, String sn, String mac, String ekey) {
        LibInterface.ManagerCallback callback = new LibInterface.ManagerCallback() {
            @Override
            public void setResult(final int result, Bundle bundle) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result == 0x00) {
                            //开门成功
                            RxBus.INSTANCE.send(new OpenDoorResult(true));
                            JLog.e("hehe");
                        } else {
                            //开门失败
                            RxBus.INSTANCE.send(new OpenDoorResult(false));
                            ToastUtils.show("result"+result);
                            JLog.e("hahahaah");
                        }
                    }
                });
            }
        };
        LibDevModel device = new LibDevModel();
        device.devSn = sn;
        device.devType = 0;
        device.devMac = mac;
        device.eKey = ekey;
        try {
            int ret = LibDevModel.cleanCard(context, device, callback);
            if (ret == 0x00) {
                JLog.e("\"开门\"这个消息 发送成功 开始执行开门的指令");
            } else {
                JLog.e("\"开门\"这个消息 发送失败 没有执行开门的指令");
                RxBus.INSTANCE.send(new OpenDoorResult(false));
                ToastUtils.show("ret"+ret);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private String getClosedDeviceSn() {
        //比较canOpenDevices中value的值
        String sn_ = "";
        int rssiMax = 0;
        List<Integer> rssiList = new ArrayList<>();
        List<Integer> rssiList_ = new ArrayList<>();
        List<String> snList = new ArrayList<>();
        for (Integer integer : canOpenDevices.values()) {
            rssiList.add(integer);
        }
        rssiList_.addAll(rssiList);
        for (String s : canOpenDevices.keySet()) {
            snList.add(s);
        }
        Collections.sort(rssiList); //从小到大
        for (Integer integer : rssiList) {
            rssiMax = integer;
        }
        for (int i = 0; i < rssiList_.size(); i++) {
            if (rssiList_.get(i) == rssiMax) {
                sn_ = snList.get(i);
                break;
            }
        }

        return sn_;
    }

}
