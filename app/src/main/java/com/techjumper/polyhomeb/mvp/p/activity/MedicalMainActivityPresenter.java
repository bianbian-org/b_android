package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalMainEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalMainActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeLoginAccountActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalUserInfoActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainActivityPresenter extends AppBaseActivityPresenter<MedicalMainActivity> {

    private MedicalMainActivityModel mModel = new MedicalMainActivityModel(this);

    private Subscription mSubs1;

    private Map<String, Result<MedicalMainEntity>> mDatas = new HashMap<>();

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        getMainData(0);
        mDatas.clear();
    }

    public void onTitleRightClick() {
        new AcHelper.Builder(getView()).target(MedicalChangeLoginAccountActivity.class).start();
    }

    @OnClick(R.id.layout_user_info)
    public void onClick(View view) {
        new AcHelper.Builder(getView()).target(MedicalUserInfoActivity.class).start();
    }

    //递归获取数据
    private int getMainData(int count) {
        if (count == 9) {
            getMedicalData(count, getPathByCount(count));
            return count;
        } else {
            getMedicalData(count, getPathByCount(count));
            return count + getMainData(count + 1);
        }
    }

    private void getMedicalData(int count, String path) {
        //path->bp血压  po 血氧  bg 血糖  ecg 心电  wt 体重体成分  pdr 运动量  bt 体温
        addSubscription(
                mSubs1 = mModel.getMedicalData(path, buildDataMap())
                        .subscribe(new Observer<Result<MedicalMainEntity>>() {
                            @Override
                            public void onCompleted() {
                                if (count == 9) {
                                    mDatas.clear();
                                    getView().dismissLoading();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (count == 9) {
                                    mDatas.clear();
                                    getView().showError(e);
                                    getView().dismissLoading();
                                }
                            }

                            @Override
                            public void onNext(Result<MedicalMainEntity> result) {
                                if (449 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_token_overdue));
                                    new AcHelper.Builder(getView()).closeCurrent(true).target(MedicalLoginActivity.class).start();
                                    return;
                                }
                                mDatas.put(path, result);
                                if (count == 9) {
                                    getView().onDataReceived(mModel.processData(mDatas));
                                }
                            }
                        }));
    }

    private Map<String, String> buildDataMap() {
        //1最近一周  2最近一月  3最近三月  4最近半年  5最近一年  这里取5就行
        //page 从 0 开始计数   这里取0就行
        //pagesize 为每页的数量   这里取1就行
        Map<String, String> map = new HashMap<>();
        map.put("timetype", 5 + "");
        map.put("page", 0 + "");
        map.put("pagesize", 1 + "");
        return map;
    }

    //根据递归,组装url中的measure_type
    private String getPathByCount(int count) {
        //2016-9-22,此时接口没有提供心率,睡眠,血脂,但是设计图有这三项,所以先1,2,3,这三个的接口会报404.
        switch (count) {
            case 0:
                return "bp";//血压
            case 1:
                return "bg";//血糖
            case 2:
                return "po";//血氧
            case 3:
                return "ecg";//心电图
            case 4:
                return "wt";//体重成分
            case 5:
                return "pdr";//运动量
            case 6:
                return "bt";//体温
            case 7:
                return "1";//心率
            case 8:
                return "2";//睡眠
            case 9:
                return "3";//血脂
            default:
                return "";

        }
    }
}
