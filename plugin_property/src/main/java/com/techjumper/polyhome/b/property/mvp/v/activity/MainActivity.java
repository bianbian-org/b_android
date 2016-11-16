package com.techjumper.polyhome.b.property.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.entity.UserInfoEntity;
import com.techjumper.commonres.entity.HeartbeatTimeEntity;
import com.techjumper.commonres.entity.event.HeartbeatEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyMessageDetailEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.plugincommunicateengine.IPluginMessageReceiver;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.activity.MainActivityPresenter;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ListFragment;

import java.util.HashMap;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;

@Presenter(MainActivityPresenter.class)
public class MainActivity extends AppBaseActivity<MainActivityPresenter> {

    public static final int ANNOUNCEMENT = 0;
    public static final int REPAIR = 1;
    public static final int COMPLAINT = 2;

    @Bind(R.id.bottom_title)
    TextView bottomTitle;
    @Bind(R.id.bottom_date)
    TextView bottomDate;
    @Bind(R.id.title_date)
    TextView titleDate;

    private int showType = -1;
    private long infoId;
    private long time = 0L;

    public long getTime() {
        return time;
    }

    public TextView getBottomDate() {
        return bottomDate;
    }

    private IPluginMessageReceiver mIPluginMessageReceiver = (code, message, extras) -> {
        if (code == PluginEngine.CODE_GET_SAVE_INFO) {
            SaveInfoEntity saveInfoEntity = com.techjumper.plugincommunicateengine.utils.GsonUtils.fromJson(message, SaveInfoEntity.class);
            if (saveInfoEntity == null || saveInfoEntity.getData() == null)
                return;

            HashMap<String, String> hashMap = saveInfoEntity.getData().getValues();
            if (hashMap == null)
                return;

            if (hashMap == null || hashMap.size() == 0)
                return;

            String name = saveInfoEntity.getData().getName();

            if (name.equals(ComConstant.FILE_HEARTBEATTIME)) {
                Log.d("prosubmitOnline", "获取本地心跳时间为: " + (TextUtils.isEmpty(message) ? "没有文件" : message));

                HeartbeatTimeEntity entity = GsonUtils.fromJson(message, HeartbeatTimeEntity.class);
                if (entity != null &&
                        entity.getData() != null &&
                        entity.getData().getValues() != null &&
                        entity.getData().getValues().getTime() != null) {
                    time = Long.valueOf(entity.getData().getValues().getTime());
                    RxBus.INSTANCE.send(new HeartbeatEvent(time));
                }
            }
        }
    };

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomTitle.setText(R.string.property);

        PluginEngine.getInstance().
                registerReceiver(mIPluginMessageReceiver);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setId(bundle.getLong(PluginConstant.KEY_PRO_FAMILY_ID));
            userInfoEntity.setUser_id(bundle.getLong(PluginConstant.KEY_PRO_USER_ID));
            userInfoEntity.setTicket(bundle.getString(PluginConstant.KEY_PRO_TICKET));
            UserInfoManager.saveUserInfo(userInfoEntity);

            if (bundle.getInt(PluginConstant.KEY_PRO_TYPE) != -1) {
                showType = bundle.getInt(PluginConstant.KEY_PRO_TYPE);
                infoId = bundle.getLong(PluginConstant.KEY_PRO_ID);

                Log.d("wowo", "获取showType :" + showType + "获取infoId :" + infoId);
            }
        }

//        UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUser_id(362);
//        userInfoEntity.setTicket("745090aaa098235c03790ac8bbc597e222ce7031");
//        userInfoEntity.setId(434);
//        UserInfoManager.saveUserInfo(userInfoEntity);

        if (showType != -1) {
            if (showType == PropertyMessageDetailEvent.COMPLAINT) {
                switchFragment(R.id.container, ListFragment.getInstance(MainActivity.COMPLAINT, showType, infoId), false, false);
            } else {
                switchFragment(R.id.container, ListFragment.getInstance(MainActivity.REPAIR, showType, infoId), false, false);
            }
        } else {
            switchFragment(R.id.container, ListFragment.getInstance(MainActivity.ANNOUNCEMENT, showType, infoId), false, false);
        }

        PluginEngineUtil.getHeartbeatTime();

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof PropertyActionEvent) {
                        PropertyActionEvent event = (PropertyActionEvent) o;
                        if (event.isAction() == false) {
                            int listType = event.getListType();
                            replaceFragment(R.id.container, ListFragment.getInstance(listType, -1, -1L));
                        }
                    }
                }));
    }
}
