package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalMainData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MedicalMainBean;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalMainEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalMainActivityPresenter;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainActivityModel extends BaseModel<MedicalMainActivityPresenter> {

    public MedicalMainActivityModel(MedicalMainActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<Result<MedicalMainEntity>> getMedicalData(String path, Map<String, String> map) {
        return RetrofitHelper.
                <ServiceAPI>createMedicalConnection()
                .getMedicalMainData(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN)
                        , path
                        , map)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> processData(Map<String, Result<MedicalMainEntity>> map) {
        List<DisplayBean> displayBeen = new ArrayList<>();
        for (Map.Entry<String, Result<MedicalMainEntity>> m : map.entrySet()) {
            sortData(m.getKey(), m.getValue(), displayBeen);
        }
        return displayBeen;
    }

    private void sortData(String key, Result<MedicalMainEntity> result, List<DisplayBean> displayBeen) {

        switch (key) {
            case "bp"://血压
                process(key, result, displayBeen);
                break;
            case "bg"://血糖
                process(key, result, displayBeen);
                break;
            case "po"://血氧
                process(key, result, displayBeen);
                break;
            case "ecg"://心电图
                process(key, result, displayBeen);
                break;
            case "wt"://体重成分
                process(key, result, displayBeen);
                break;
            case "pdr"://运动量
                process(key, result, displayBeen);
                break;
            case "bt"://体温
                process(key, result, displayBeen);
                break;
            case "1"://心率
                process(key, result, displayBeen);
                break;
            case "2"://睡眠
                process(key, result, displayBeen);
                break;
            case "3"://血脂
                process(key, result, displayBeen);
                break;
        }
    }

    private void process(String key, Result<MedicalMainEntity> result, List<DisplayBean> displayBeen) {
        MedicalMainData medicalMainData = new MedicalMainData();
        if (result != null
                || result.response() != null
                || result.response().body() != null
                || result.response().body().getData() != null
                || result.response().body().getData().size() != 0) {
            medicalMainData.setNoData(false);
            medicalMainData.setPosition(getPositionByKey(key));
            medicalMainData.setData(getDataByKey(key, result));
        } else {
            medicalMainData.setNoData(true);
            medicalMainData.setPosition(getPositionByKey(key));
        }
        MedicalMainBean medicalMainBean = new MedicalMainBean(medicalMainData);
        displayBeen.add(medicalMainBean);
    }

    private int getPositionByKey(String key) {
        switch (key) {
            case "bp"://血压
                return 0;
            case "bg"://血糖
                return 1;
            case "po"://血氧
                return 2;
            case "ecg"://心电图
                return 3;
            case "wt"://体重成分
                return 4;
            case "pdr"://运动量
                return 5;
            case "bt"://体温
                return 6;
            case "1"://心率
                return 7;
            case "2"://睡眠
                return 8;
            case "3"://血脂
                return 9;
            default:
                return -1;
        }
    }

    private String getDataByKey(String key, Result<MedicalMainEntity> result) {
        switch (key) {
            case "bp"://血压
                MedicalMainEntity.DataBean dataBean1 = result.response().body().getData().get(0);
                int systolic = dataBean1.getSystolic();  //高压
                int diastolic = dataBean1.getDiastolic();  //低压
                return diastolic + "/" + systolic;
            case "bg"://血糖
                MedicalMainEntity.DataBean dataBean2 = result.response().body().getData().get(0);
                double bgValue = dataBean2.getBgValue();
                return bgValue + "";
            case "po"://血氧
                MedicalMainEntity.DataBean dataBean3 = result.response().body().getData().get(0);
                double spo2 = dataBean3.getSpo2();
                return spo2 + "";
            case "ecg"://心电图
                MedicalMainEntity.DataBean dataBean4 = result.response().body().getData().get(0);
                String measureTime = dataBean4.getMeasureTime();  //需要时间格式化
                return formatTime(measureTime);
            case "wt"://体重成分
                MedicalMainEntity.DataBean dataBean5 = result.response().body().getData().get(0);
                double weight = dataBean5.getWeight();
                return weight + "";
            case "pdr"://运动量
                MedicalMainEntity.DataBean dataBean6 = result.response().body().getData().get(0);
                int stepCount = dataBean6.getStepCount();
                return stepCount + "";
            case "bt"://体温
                MedicalMainEntity.DataBean dataBean7 = result.response().body().getData().get(0);
                double btValue = dataBean7.getBtValue();
                return btValue + "";
            case "1"://心率
                MedicalMainEntity.DataBean dataBean8 = result.response().body().getData().get(0);
                int heartRate = dataBean8.getHeartRate();
                return heartRate + "";
            case "2"://睡眠
                return "";
            case "3"://血脂
                return "";
            default:
                return "———";
        }
    }

    //无数据时显示的视图
    public List<DisplayBean> noData() {

        List<DisplayBean> displayBeen = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MedicalMainData medicalMainData = new MedicalMainData();
            medicalMainData.setNoData(true);
            medicalMainData.setPosition(i); //-1代表无数据,所以其他东西都不用设置,留给item自己处理
            MedicalMainBean medicalMainBean = new MedicalMainBean(medicalMainData);
            displayBeen.add(medicalMainBean);
        }
        return displayBeen;
    }

    private String formatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MM-dd").format(date);
    }
}
