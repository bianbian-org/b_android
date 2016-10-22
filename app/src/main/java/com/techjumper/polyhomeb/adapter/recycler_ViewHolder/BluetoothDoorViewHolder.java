package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.view.View;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallback;
import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.slide2unlock.interfaces.IUnLockViewState;
import com.techjumper.polyhome.slide2unlock.view.Slide2UnlockView;
import com.techjumper.polyhome.slide2unlock.view.enums.LockViewResult;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.BluetoothData;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, Integer> mBLEDvices = new HashMap<>(); //扫描比对后的map
    private Map<String, Integer> sortMap = new LinkedHashMap<>(); //排序后的map

    private Slide2UnlockView mView;

    String ekey = "";
    String mac = "";
    String name = "";
    String closedSn = "";

    //当前是否有门锁在周围(有并且key/sn/mac对的上,就可用;无/对不上则不可用),true就显示为可用的颜色,false则是控件默认的不可用颜色
    private boolean mIsUsable = false;


    public BluetoothDoorViewHolder(View itemView) {
        super(itemView);
        mView = getView(R.id.lock_view);

    }

    @Override
    public void setData(BluetoothData data) {
        if (data == null) return;
        //相当于Demo中,从云端获取到的"此账号"能开的锁的所有信息
        List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infosBeen = data.getInfosBeen();

        //蓝牙当前扫描到的锁的信息,并且将匹配的门锁存入map.
        scanBLEDevices(infosBeen);

        //得到最近的一个BLE设备的sn
//        closedSn = ;

        //将得到的closedSn拿到infosBeen中匹配，得到mac，ekey，name等信息
        for (BluetoothLockDoorInfoEntity.DataBean.InfosBean bean : infosBeen) {
            String sn = bean.getSn();
            if (sn.equals(getClosedBLEDevice())) {  //device中只有sn字段
                ekey = bean.getEkey();
                mac = bean.getMac();
                name = bean.getName();
                closedSn = sn;
                break;
            }
        }

        mView.setText(name);
        mView.setOnUnLockViewStateListener(this);


    }

    private void scanBLEDevices(List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infosBeen) {
        mBLEDvices.clear();
        ScanCallback callback = new ScanCallback() {
            @Override
            public void onScanResult(ArrayList<String> deviceList, ArrayList<Integer> rssi) {
                //此回调是LibDevModel.scanDevice(getContext(), true, 4, callback);中第二个参数为false时的回调
            }

            @Override
            public void onScanResultAtOnce(final String devSn, int rssi) {
                ((Activity) getContext()).runOnUiThread(() -> {
                    for (BluetoothLockDoorInfoEntity.DataBean.InfosBean device : infosBeen) {
                        //遍历已有的门锁信息,并且和扫描到的门锁信息相比对.
                        if (device.getSn().equalsIgnoreCase(devSn)) {
                            //sn相同说明当前sp中已有的信息能开这把锁
                            //如果有周围有多把锁,那么就要判断哪一把锁的信号更强
                            //所以要将比对后的锁存起来,等待扫描结束后,再从存起来的数据集合中比较锁的信号强度.选出最强的一把.
                            //rssi越接近0信号越强,越接近-100信号越弱
                            mBLEDvices.put(devSn, rssi);
                            mIsUsable = true;
                            mView.setUsable(true);
                        }
                        if (!mIsUsable) {
                            mIsUsable = false;
                            mView.setUsable(false);
                        }
                    }
                    //排序
                    if (mBLEDvices == null || mBLEDvices.size() == 0) {
                        sortMap.clear();
                        List<Map.Entry<String, Integer>> list = new ArrayList<>(mBLEDvices.entrySet());
                        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
                        for (Map.Entry<String, Integer> mapping : list) {
                            sortMap.put(mapping.getKey(), mapping.getValue());
                        }
                    }
                });
            }
        };
        //上下文,扫描到设备立即返回与否,扫描时间,扫描结束后的回调
        LibDevModel.scanDevice(getContext(), true, 4, callback);
    }

    //排序过后遍历map,得到信号最强的一个
    //此处使用的是LinkedHashMap,在sortMap存入数据的时候就已经是有序的了.并且排序是按照value(rssi)从小到大的顺序来排的
    //所以此处遍历完成之后,得到的sn就是距离最近的一个,并且也是sp中存在的.
    private String getClosedBLEDevice() {
        if (sortMap == null || sortMap.size() == 0) return null;
        String sn = "";
        for (Map.Entry<String, Integer> entry : sortMap.entrySet()) {
            sn = entry.getKey();
        }
        return sn;
    }

    @Override
    public void startUnLock(Slide2UnlockView view) {
        ToastUtils.show("开始解锁");
        final LibInterface.ManagerCallback callback = (result, bundle) -> ((Activity) getContext()).runOnUiThread(() -> {
            if (result == 0x00) {
                mView.unLockResult(LockViewResult.SUCCESS);
            } else {
                mView.unLockResult(LockViewResult.FAILED);
            }
        });
        LibDevModel device = new LibDevModel();
        device.devSn = closedSn;
        device.devMac = mac;
        device.eKey = ekey;
        try {
            int ret = LibDevModel.cleanCard(getContext(), device, callback);
            if (ret == 0x00) {
            } else {

            }
        } catch (NumberFormatException e) {
        }

    }

    @Override
    public void unLocking(Slide2UnlockView view) {
        ToastUtils.show("正在解锁中,表着急");
    }
}
