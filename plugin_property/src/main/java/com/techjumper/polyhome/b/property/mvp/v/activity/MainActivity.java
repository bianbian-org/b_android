package com.techjumper.polyhome.b.property.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.entity.HeartbeatTimeEntity;
import com.techjumper.commonres.entity.UserInfoEntity;
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
import com.techjumper.polyhome.b.property.mvp.v.fragment.AnnouncementFragment;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ComplaintFragment;
import com.techjumper.polyhome.b.property.mvp.v.fragment.PayFragment;
import com.techjumper.polyhome.b.property.mvp.v.fragment.RepairFragment;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import rx.android.schedulers.AndroidSchedulers;

@Presenter(MainActivityPresenter.class)
public class MainActivity extends AppBaseActivity<MainActivityPresenter> {
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String SHOWTYPE = "showType";

    public static final int ANNOUNCEMENT = 0;
    public static final int REPAIR = 1;
    public static final int COMPLAINT = 2;
    public static final int PAY = 3;

    @Bind(R.id.bottom_title)
    TextView bottomTitle;
    @Bind(R.id.bottom_date)
    TextView bottomDate;
    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.fl_title_announcement)
    RadioButton flTitleAnnouncement;
    @Bind(R.id.fl_title_repair)
    RadioButton flTitleRepair;
    @Bind(R.id.fl_title_complaint)
    RadioButton flTitleComplaint;

    @OnCheckedChanged(R.id.fl_title_announcement)
    void checkAnnouncement(boolean check) {
        if (check && isCheckAndUpdate) {
            replaceFragment(R.id.container, AnnouncementFragment.getInstance(ANNOUNCEMENT));
        }
        isCheckAndUpdate = true;
    }

    @OnCheckedChanged(R.id.fl_title_repair)
    void checkRepair(boolean check) {
        if (check && isCheckAndUpdate) {
            replaceFragment(R.id.container, RepairFragment.getInstance(REPAIR, -1, -1));
        }
        isCheckAndUpdate = true;
    }

    @OnCheckedChanged(R.id.fl_title_complaint)
    void checkComplaint(boolean check) {
        if (check && isCheckAndUpdate) {
            replaceFragment(R.id.container, ComplaintFragment.getInstance(COMPLAINT, -1, -1));
        }
        isCheckAndUpdate = true;
    }

    @OnCheckedChanged(R.id.fl_title_pay)
    void checkPay(boolean check) {
        if (check && isCheckAndUpdate) {
            replaceFragment(R.id.container, PayFragment.getInstance(PAY));
        }
        isCheckAndUpdate = true;
    }

    private int showType = -1;
    private long infoId = -1L;
    private long time = 0L;
    private boolean isCheckAndUpdate = true;

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
//        userInfoEntity.setTicket("5ff890cc01d54c975daadc1979d6d5c2a77e4586");
//        userInfoEntity.setId(434);
//        UserInfoManager.saveUserInfo(userInfoEntity);

        if (showType != -1) {
            if (showType == PropertyMessageDetailEvent.COMPLAINT) {
                isCheckAndUpdate = false;
                flTitleComplaint.setChecked(true);
                switchFragment(R.id.container, ComplaintFragment.getInstance(COMPLAINT, showType, infoId), false, false);
                RxBus.INSTANCE.send(new PropertyMessageDetailEvent(infoId, PropertyMessageDetailEvent.COMPLAINT));
            } else {
                isCheckAndUpdate = false;
                flTitleRepair.setChecked(true);
                switchFragment(R.id.container, RepairFragment.getInstance(REPAIR, showType, infoId), false, false);
                RxBus.INSTANCE.send(new PropertyMessageDetailEvent(infoId, PropertyMessageDetailEvent.REPAIR));
            }
        } else {
            switchFragment(R.id.container, AnnouncementFragment.getInstance(ANNOUNCEMENT), false, false);
        }

        PluginEngineUtil.getHeartbeatTime();

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof PropertyActionEvent) {
                        PropertyActionEvent event = (PropertyActionEvent) o;
                        if (event.isAction() == false) {
                            int listType = event.getListType();
                            if (listType == REPAIR) {
                                replaceFragment(R.id.container, RepairFragment.getInstance(REPAIR, -1, -1));
                            } else if (listType == COMPLAINT) {
                                replaceFragment(R.id.container, ComplaintFragment.getInstance(COMPLAINT, -1, -1));
                            }
                        }
                    }
                    if (o instanceof PropertyMessageDetailEvent) {
                        isCheckAndUpdate = true;
                    }
                }));
    }
}
