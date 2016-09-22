package com.techjumper.polyhomeb.mvp.p.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.NumberPicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalUserInfoData;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserInfoEntity;
import com.techjumper.polyhomeb.entity.event.MedicalChangeUserInfoEvent;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalUserInfoActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalUserInfoActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

import static com.techjumper.polyhomeb.R.id.picker;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalUserInfoActivityPresenter extends AppBaseActivityPresenter<MedicalUserInfoActivity>
        implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter {

    private MedicalUserInfoActivityModel mModel = new MedicalUserInfoActivityModel(this);

    private Subscription mSubs1, mSubs2, mSubs3;

    private int mClickType = 0;

    private String mHeight = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_HEIGHT);
    private String mWeight = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_WEIGHT);

    public static final int NICKNAME = 1;
    public static final int NAME = 2;
    public static final int IDCARD = 3;
    public static final int BIRTHDAY = 4;
    public static final int SEX = 5;
    public static final int HOMEPHONE = 6;
    //    public static final int MOBILEPHONE = 7;
    public static final int EMAIL = 8;
    public static final int HEIGHT = 9;
    public static final int WEIGHT = 10;

    public static final String TYPE = "type";
    public static final String POSITION = "position";
    public static final String DATA = "data";

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getUserInfo();
        onUserInfoEdited();
    }

    //加载本地SP中存储的用户信息
    private void getUserInfo() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getInfo()
                        .subscribe(new Subscriber<List<DisplayBean>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<DisplayBean> dataList) {
                                getView().onDataReceived(dataList);
                            }
                        })
        );
    }

    //点击或者修改了某个用户item的时候的回调
    private void onUserInfoEdited() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable().subscribe(o -> {
                            if (o instanceof MedicalChangeUserInfoEvent) {
                                MedicalChangeUserInfoEvent event = (MedicalChangeUserInfoEvent) o;
                                changeUserInfo(event);
                            }
                        }));
    }

    public List<DisplayBean> getData(MedicalUserInfoEntity entity) {
        return mModel.getData(entity);
    }

    //根据item点击事件,做相应操作
    private void changeUserInfo(MedicalChangeUserInfoEvent event) {
        int type = event.getType();
        String content = event.getContent();
        int position = event.getPosition();
        List<DisplayBean> data = getView().getAdapter().getData();

        switch (type) {
            case NICKNAME:
            case NAME:
            case IDCARD:
            case HOMEPHONE:
            case SEX:
                updateData(data, position, content);
                break;
            case BIRTHDAY:
                showDatePicker();
                break;
            case HEIGHT:
                mClickType = 1;
                showNumberPicker();
                break;
            case WEIGHT:
                mClickType = 2;
                showNumberPicker();
                break;
//            case MOBILEPHONE:
//                break;
            case EMAIL:
                getUserInfo();
                break;
        }
    }

    //点击事件通用更新item方法
    private void updateData(List<DisplayBean> data, int position, String content) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) instanceof MedicalUserInfoData) {
                if (i == position) {
                    ((MedicalUserInfoData) data.get(i)).setContent(content);
                    break;
                }
            }
        }
//        getView().getAdapter().notifyDataSetChanged();  //不起作用了,所以只好调用下面这个方法
//        所以上面那个循环和if都可以不要了,因为反正都是调用下面这个方法,不存在刷新RV的item
        getUserInfo();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getView(), (view, year, monthOfYear, dayOfMonth) -> {
            String month = monthOfYear + 1 >= 10 ? (monthOfYear + 1) + "" : "0" + (monthOfYear + 1);
            String day = dayOfMonth >= 10 ? day = dayOfMonth + "" : "0" + dayOfMonth;
            String birthday = year + "-" + month + "-" + day;

            Map<String, String> map = new HashMap<>();
            map.put("birthday", birthday);
            changeUserInfo(map, BIRTHDAY);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void showNumberPicker() {
        MaterialDialog materialDialog = DialogUtils.getBuilder(getView())
                .customView(R.layout.layout_picker_dialog, true)
                .title(mClickType == 1 ? getView().getString(R.string.medical_dialog_choose_height) : getView().getString(R.string.medical_dialog_choose_weight))
                .positiveText(getView().getString(R.string.ok))
                .negativeText(getView().getString(R.string.cancel))
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            Map<String, String> map = new HashMap<>();
                            map.put(mClickType == 1 ? "height" : "weight", mClickType == 1 ? mHeight : mWeight);
                            changeUserInfo(map, mClickType == 1 ? HEIGHT : WEIGHT);
                            break;
                        case NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }).build();

        NumberPicker view = (NumberPicker) materialDialog.findViewById(picker);
        view.setOnValueChangedListener(this);
        view.setWrapSelectorWheel(false);
        view.setFormatter(this);
        //以下反射是为了解决NumberPicker第一次进入的时候,没有走format的逻辑
        try {
            Method method = view.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(view, true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (mClickType == 1) {
            view.setMaxValue(200);
            view.setMinValue(0);
            int value;
            if (TextUtils.isEmpty(mHeight)) {
                value = 160;
            } else {
                value = (int) NumberUtil.convertTofloat(mHeight, 0f);
            }
            view.setValue(value);
        } else if (mClickType == 2) {
            view.setMaxValue(100);
            view.setMinValue(0);
            int value;
            if (TextUtils.isEmpty(mWeight)) {
                value = 50;
            } else {
                value = (int) NumberUtil.convertTofloat(mWeight, 0f);
            }
            view.setValue(value);
        }
        materialDialog.show();
    }

    //修改生日,身高和体重
    private void changeUserInfo(Map<String, String> map, int type) {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.changeUserInfo(map)
                        .subscribe(new Observer<Result<MedicalStatusEntity>>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                            }

                            @Override
                            public void onNext(Result<MedicalStatusEntity> result) {
                                MedicalStatusEntity medicalStatusEntity = result.response().body();
                                if (449 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_token_overdue));
                                    new AcHelper.Builder(getView()).closeCurrent(true).target(MedicalLoginActivity.class).start();
                                }
                                if (medicalStatusEntity == null) return;
                                int status = medicalStatusEntity.getStatus();

                                switch (status) {
                                    case 1:
                                        ToastUtils.show(getView().getString(R.string.medical_change_success));
                                        if (BIRTHDAY == type) {
                                            UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_BIRTHDAY, map.get("birthday"));
                                        } else if (HEIGHT == type) {
                                            UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_HEIGHT, map.get("height"));
                                        } else if (WEIGHT == type) {
                                            UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_WEIGHT, map.get("weight"));
                                        }
                                        getUserInfo();
                                        break;
                                    case -1:
                                        ToastUtils.show(getView().getString(R.string.medical_unknow_error));
                                        break;
                                }
                            }
                        })
        );
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (mClickType) {
            case 1:
                mHeight = newVal + "";
                break;
            case 2:
                mWeight = newVal + "";
                break;
        }
    }

    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        switch (mClickType) {
            case 1:
                tmpStr = value + " cm";
                break;
            case 2:
                tmpStr = value + " kg";
                break;
        }
        return tmpStr;
    }
}
