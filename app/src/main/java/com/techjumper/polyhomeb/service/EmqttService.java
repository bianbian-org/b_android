package com.techjumper.polyhomeb.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.emqtt.EmqttEntity;
import com.techjumper.polyhomeb.mvp.v.activity.JSInteractionActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity;
import com.techjumper.polyhomeb.user.UserManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class EmqttService extends Service {

    public static MqttAndroidClient mqttAndroidClient;
    private final String serverUri = "tcp://123.57.139.200:1883";//服务器地址
    private String subscribeTopic = "";//订阅的主题
    private static int NOTIFY = 0;//notification通知的标识符

    /**
     * 跳转的方式 【物业界面】【网页界面】
     */
    private static final String GO_APP = "go_app";
    private static final String GO_URL = "go_url";
    private static final String GO_ACTIVITY = "go_activity";
    /**
     * 判断跳转物业界面的key值
     */
    private static final String COMPLAINT_VALUE = "complaint";

    /**
     * 跳转h5界面所需要拼接的字段
     */
    private static final String QUESTION_MARK = "?";
    private static final String WITH = "&";
    private static final String EQUALS = "=";

    /**
     * 加密所用key值
     */
    private static final String SIGN_KEY = "6727fb0922117931bb65a48ee27dc0d9";


    public EmqttService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        subscribeTopic = getTopic();
        if (isConnected()) {
            JLog.d("MqttAndroidClient  【YES】连接 ，判断topic  ---->" + subscribeTopic);
            String localTopic = UserManager.INSTANCE.getUserInfo(UserManager.EMQTT_TOPIC);
            JLog.d("本地保存的topic  ---->" + localTopic);
            if (TextUtils.isEmpty(localTopic)) {
                JLog.d("本地没有存储topic，直接进行保存和订阅");
                UserManager.INSTANCE.saveUserInfo(UserManager.EMQTT_TOPIC, subscribeTopic);
                subscribeToTopic();
            } else {
                JLog.d("本地存储topic，直接进行比较");
                if (subscribeTopic.equals(localTopic)) {
                    /**
                     * 1.退出应用再进来
                     * 2.ticket变化重新登陆再进来
                     */
                    JLog.d("本地存储topic与当前一致，还需订阅的");
                    subscribeToTopic();
                } else {
                    JLog.d("本地存储topic与当前不一致，先取消本地的订阅，在进行本地保存，最后订阅");
                    unSubscribeTopic(localTopic);
                    UserManager.INSTANCE.saveUserInfo(UserManager.EMQTT_TOPIC, subscribeTopic);
                    subscribeToTopic();
                }
            }


        } else {
            JLog.d("MqttAndroidClient  【NO】连接 ,topic进行保存  ---->" + subscribeTopic);
            UserManager.INSTANCE.saveUserInfo(UserManager.EMQTT_TOPIC, subscribeTopic);
            init();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void init() {
        JLog.d("初始化emqtt数据  ---->" + getClientId());
        mqttAndroidClient = new MqttAndroidClient(Utils.appContext, serverUri, getClientId());
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    JLog.d("重新连接  ---->" + serverURI);
                    subscribeTopic = getTopic();
                    subscribeToTopic();
                } else {
                    JLog.d("连接  ---->" + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                JLog.d("emqtt连接丢失  ---->" + (cause == null ? "cause is null" : cause.toString()));//被顶掉时，重新连接几次后，会空指针
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String mes = new String(message.getPayload());
                notification(topic, mes);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName("admin");//账号
        mqttConnectOptions.setPassword("public".toCharArray());//密码
        // 设置为断开重连
        mqttConnectOptions.setAutomaticReconnect(true);
        // 清除Session
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new MqttActionListener());
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private class MqttActionListener implements IMqttActionListener {


        /**
         * This method is invoked when an action has completed successfully.
         *
         * @param asyncActionToken associated with the action that has completed
         */
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            JLog.d("isConnected  ---->" + isConnected());
            DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
            disconnectedBufferOptions.setBufferEnabled(true);
            disconnectedBufferOptions.setBufferSize(100);
            disconnectedBufferOptions.setPersistBuffer(false);
            disconnectedBufferOptions.setDeleteOldestMessages(false);
            mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
            /**
             * 连接成功订阅
             */
            subscribeToTopic();
        }

        /**
         * This method is invoked when an action fails.
         * If a client is disconnected while an action is in progress
         * onFailure will be called. For connections
         * that use cleanSession set to false, any QoS 1 and 2 messages that
         * are in the process of being delivered will be delivered to the requested
         * quality of service next time the client connects.
         *
         * @param asyncActionToken associated with the action that has failed
         * @param exception
         */
        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            JLog.d("连接失败  ---->" + serverUri);
            JLog.d("连接失败  ---->" + exception.toString());
        }
    }

    /**
     * 判断emqtt是否连接
     *
     * @return
     */
    private boolean isConnected() {
        if (mqttAndroidClient == null) {
            return false;
        }
        return mqttAndroidClient.isConnected();
    }

    /**
     * emqtt主动断开连接并把对象置为null
     */
    private void disconnect() {
        if (mqttAndroidClient != null) {
            try {
                mqttAndroidClient.disconnect();
                mqttAndroidClient = null;
                JLog.d("mqttAndroidClient断开连接操作");
            } catch (MqttException e) {
                e.printStackTrace();
            }

        } else {
            JLog.d("mqttAndroidClient已经为null，不用断开连接操作");
        }
    }

    /**
     * 取消订阅
     *
     * @param topic
     */
    private void unSubscribeTopic(String topic) {
        if (mqttAndroidClient == null) {
            JLog.d(" 无法取消订阅 ----> mqttAndroidClient==null ");
            return;
        }
        try {
            mqttAndroidClient.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    JLog.d(" 取消订阅成功");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    JLog.d(" 取消订阅失败");
                }
            });
        } catch (MqttException e) {
            JLog.d(" 取消订阅异常  ---->" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 订阅主题
     */
    private void subscribeToTopic() {

        if (mqttAndroidClient == null) {
            JLog.d("订阅失败，mqttAndroidClient==null");
            return;
        }
        try {
            mqttAndroidClient.subscribe(subscribeTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    JLog.d("订阅成功");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    JLog.d("订阅失败  ---->" + (exception == null ? "" : exception.toString()));//被顶掉时，重新连接几次后，会空指针
                }
            });
        } catch (MqttException e) {
            JLog.d("订阅异常  ---->" + e.toString());
            e.printStackTrace();
        }

    }

    private void notification(String topic, String message) {

        if (!topic.equals(subscribeTopic)) {
            return;
        } else {
            JLog.d("topic  ---->" + topic);
            JLog.d("message  ---->" + message);
            JLog.d("subscribeTopic  ---->" + subscribeTopic);
        }

        EmqttEntity emqttEntity = GsonUtils.fromJson(message, EmqttEntity.class);
        if (emqttEntity == null ||
                emqttEntity.getPayload() == null ||
                emqttEntity.getPayload().getBody() == null ||
                emqttEntity.getPayload().getExtra() == null) {
            JLog.d("数据为null");
            return;
        }

        if (System.currentTimeMillis() / 1000L - Long.parseLong(String.valueOf(emqttEntity.getTimestamp())) >= 60L * 10) {
            JLog.d("接受的时间大于10分钟，终止操作");
            return;
        }

        if (!emqttEntity.getTicket().equals(UserManager.INSTANCE.getTicket())) {
            JLog.d("ticket 已经改变，终止操作，断开连接");
            JLog.d(" 本地存储的ticket  ---->" + UserManager.INSTANCE.getTicket());
            JLog.d(" 本地存储的ticket  ---->" + emqttEntity.getTicket());
            if (isConnected()) {
                //disconnect();变成取消订阅
                unSubscribeTopic(subscribeTopic);
            }
            return;
        }

        if (!signIsEquals(emqttEntity)) {
            JLog.d("sign 值不一致，终止操作");
            return;
        }

        NotificationManager manager = (NotificationManager) Utils.appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent it = null;
        Bundle bundle = new Bundle();
        switch (emqttEntity.getPayload().getBody().getAfter_open()) {
            case GO_APP:
                JLog.d(" 收到的为【app信息】go_app");
                /**
                 *  跳转app的操作，还没遇到（广告类的信息）
                 */

                break;

            case GO_URL://主要为订单的相关操作
                JLog.d(" 收到的为【url信息】go_url");
                Object UrlExtraBean = emqttEntity.getPayload().getExtra();
                boolean isFirst = true;// 是否为第一个参数（第一个参数与后面的参数拼接的字段不一致，特此区分开）
                String url = Config.sHost.concat(emqttEntity.getPayload().getBody().getAfter_open_page());//拼接URl的基础路径
                StringBuilder urlBuilder = new StringBuilder(url);
                JSONObject jsonObjectUrl = null;
                try {
                    jsonObjectUrl = new JSONObject(GsonUtils.toJson(UrlExtraBean));
                    JLog.d(jsonObjectUrl.toString());
                    Iterator<String> iterator = jsonObjectUrl.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        if (isFirst) {
                            urlBuilder.append(QUESTION_MARK).append(key).append(EQUALS).append(jsonObjectUrl.getString(key));
                            isFirst = false;
                        } else {
                            urlBuilder.append(WITH).append(key).append(EQUALS).append(jsonObjectUrl.getString(key));
                        }
                    }
                } catch (JSONException e) {
                    JLog.d("go_url解析异常  ---->" + e.toString());
                    e.printStackTrace();
                }

                JLog.d("加载的url路径  ---->" + urlBuilder.toString());
                it = new Intent(Utils.appContext, JSInteractionActivity.class);
                bundle.putString(Constant.JS_PAGE_JUMP_URL, urlBuilder.toString());
                it.putExtras(bundle);

                break;
            case GO_ACTIVITY://一般为物业信息
                JLog.d(" 收到的为【物业信息】go_activity");
                int complaintValue = -1;
                Object acExtraBean = emqttEntity.getPayload().getExtra();
                JSONObject jsonObjectAc = null;
                try {
                    jsonObjectAc = new JSONObject(GsonUtils.toJson(acExtraBean));
                    JLog.d(jsonObjectAc.toString());
                    Iterator<String> iterator = jsonObjectAc.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        if (COMPLAINT_VALUE.equals(key)) {//key可以认定为是固定值
                            complaintValue = jsonObjectAc.getInt(key);
                        }

                    }
                } catch (JSONException e) {
                    JLog.d("go_activity 解析异常  ---->" + e.toString());
                    e.printStackTrace();
                }

                if (complaintValue != -1) {
                    /**
                     * 没必要这么写，只是为了清晰下；稳定之后进行优化
                     */
                    switch (complaintValue) {
                        case 0:
                            it = new Intent(Utils.appContext, PropertyDetailActivity.class);
                            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_PLACARD);
                            it.putExtras(bundle);
                            break;
                        case 1:
                            it = new Intent(Utils.appContext, PropertyDetailActivity.class);
                            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_REPAIR);
                            it.putExtras(bundle);
                            break;
                        case 2:
                            it = new Intent(Utils.appContext, PropertyDetailActivity.class);
                            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_COMPLAINT);
                            it.putExtras(bundle);
                            break;
                    }
                } else {
                    JLog.d("complaint==-1  值没有符合条件的");
                }

                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.appContext);
        builder.setAutoCancel(true)//通知设置不会自动显示
                .setShowWhen(true)//显示时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知的小图标
                .setContentTitle(emqttEntity.getPayload().getBody().getTitle())
                .setContentText(emqttEntity.getPayload().getBody().getText());

        PendingIntent pendingIntent = PendingIntent.getActivity(Utils.appContext, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setVisibility(Notification.VISIBILITY_PUBLIC);// 随时
//            builder.setFullScreenIntent(pendingIntent, true);// 横幅
        }
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;// 设置为默认的声音
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFY, notification);// 显示通知
        NOTIFY++;
    }

    /**
     * 判断sign值是否一致
     *
     * @param emqttEntity
     * @return
     */
    private boolean signIsEquals(EmqttEntity emqttEntity) {
        StringBuilder URL1 = new StringBuilder();
        String URL = "";
        Map<String, String> maps = new TreeMap<>();
        try {
            JSONObject json = new JSONObject(GsonUtils.toJson(emqttEntity.getPayload().getBody()));
            Iterator<String> iterator = json.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                maps.put(key, json.getString(key));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Set set = maps.keySet();
        for (Object object : set) {
            URL1.append(object).append("=").append(maps.get(object)).append("&");
        }
        URL = URL1.append("key").append("=").append(SIGN_KEY).toString();
        URL = StringUtils.md5(URL).toUpperCase();
        JLog.d("计算的sign值是否一致  ---->" + (URL.equals(emqttEntity.getSign())));
        return URL.equals(emqttEntity.getSign());
    }

    /**
     * ClientId 获取
     *
     * @return
     */
    private String getClientId() {
        return StringUtils.md5(AppUtils.getDeviceIMEI().
                concat(WITH).
                concat(EQUALS).
                concat(QUESTION_MARK));
    }

    private String getTopic() {
        return "/users/".concat(UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)).concat("/message");
    }


}
