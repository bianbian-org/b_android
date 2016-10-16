package com.techjumper.polyhomeb.mvp.m;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.PaymentsEntity;
import com.techjumper.polyhomeb.mvp.p.activity.AdjustAccountsActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdjustAccountsActivityModel extends BaseModel<AdjustAccountsActivityPresenter> {

    public AdjustAccountsActivityModel(AdjustAccountsActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<PaymentsEntity> payments(String category) {
        KeyValuePair keyValuePair = KeyValueCreator.payments(
                getIp()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , category
                , getOrderNum());
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .payments(map)
                .compose(CommonWrap.wrap());
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    /**
     * 订单号 334209320948023
     */
    public String getOrderNum() {
        return getExtra().getString(Constant.KEY_ORDER_NUMBER, "");
    }

    /**
     * 费用名称  2月水费
     */
    public String getPayName() {
        return getExtra().getString(Constant.KEY_PAY_NAME, "");
    }

    /**
     * 收费对象  2栋2楼202
     */
    public String getPayObj() {
        return getExtra().getString(Constant.KEY_PAY_OBJECT, "");
    }

    /**
     * 截止日期 2016-6-6
     */
    public String getDeathLine() {
        return getExtra().getString(Constant.KEY_PAY_DEATH_LINE, "");
    }

    /**
     * 总价(不包括滞纳金)
     */
    public double getTotal() {
        return getExtra().getDouble(Constant.KEY_PAY_TOTAL, 0);
    }

    /**
     * 滞纳金
     */
    public double getExpiryPrice() {
        return getExtra().getDouble(Constant.KEY_PAY_EXPIRY, 0);
    }

    /**
     * 费用类型  1-物业费 2-水费 3-电费 4-燃气费 5-其他
     */
    public int getPayType() {
        return getExtra().getInt(Constant.KEY_PAY_TYPE, 0);
    }

    /**
     * //是否逾期 0-没逾期, 1-逾期
     */
    public int getIsLate() {
        return getExtra().getInt(Constant.KEY_PAY_IS_LATE, 0);
    }

    /**
     * 超过X天,还剩下10天,是+10天,逾期超过了10天则是-10天
     */
    public int getDay() {
        return getExtra().getInt(Constant.KEY_PAY_DAY, 0);
    }

    /**
     * 费用类型  1-物业费 2-水费 3-电费 4-燃气费 5-其他
     */
    public String getPayTypeString() {
        switch (getPayType()) {
            case 1:
                return getPresenter().getView().getString(R.string.pop_property_pay);
            case 2:
                return getPresenter().getView().getString(R.string.pop_water_pay);
            case 3:
                return getPresenter().getView().getString(R.string.pop_elec_pay);
            case 4:
                return getPresenter().getView().getString(R.string.pop_gas_pay);
            case 5:
                return getPresenter().getView().getString(R.string.pop_other);
        }
        return "";
    }

    private String getIp() {
        String ip = "";
        ConnectivityManager conMann = (ConnectivityManager)
                getPresenter().getView().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobileNetworkInfo.isConnected()) {
            ip = getLocalIp();
        } else if (wifiNetworkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) getPresenter().getView().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        }
        return ip;
    }

    //获取wifi网络ip
    private String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    //获取移动网络ip
//    private String getLocalIpAddress() {
//        try {
//            String ipv4;
//            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface ni : nilist) {
//                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
//                for (InetAddress address : ialist) {
//                    if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
//                        return ipv4;
//                    }
//                }
//
//            }
//
//        } catch (SocketException ex) {
//        }
//        return null;
//    }

    private String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

}
