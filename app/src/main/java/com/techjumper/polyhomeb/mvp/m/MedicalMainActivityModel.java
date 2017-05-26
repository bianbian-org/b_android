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

    public int getPositionByKey(String key) {
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

    public String getDataByKey(String key, MedicalMainEntity.DataBean dataBean) {
        switch (key) {
            case "bp"://血压
                int systolic = dataBean.getSystolic();  //高压
                int diastolic = dataBean.getDiastolic();  //低压
                return diastolic + "/" + systolic;
            case "bg"://血糖
                double bgValue = dataBean.getBgValue();
                return bgValue + "";
            case "po"://血氧
                double spo2 = dataBean.getSpo2();
                return spo2 + "";
            case "ecg"://心电图
                String measureTime = dataBean.getMeasureTime();  //需要时间格式化
                return formatTime(measureTime);
            case "wt"://体重成分
                double weight = dataBean.getWeight();
                return weight + "";
            case "pdr"://运动量
                int stepCount = dataBean.getStepCount();
                return stepCount + "";
            case "bt"://体温
                double btValue = dataBean.getBtValue();
                return btValue + "";
            case "1"://心率
                int heartRate = dataBean.getHeartRate();
                return heartRate + "";
            case "2"://睡眠
                return "";
            case "3"://血脂
                return "";
            default:
                return "";
        }
    }

    public String getPathByCount(int count) {
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

    //先把View显示出来,之后访问网络之时,再把取到的数据分别设置上去
    public List<DisplayBean> getViewData() {
        List<DisplayBean> displayBeen = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MedicalMainData medicalMainData = new MedicalMainData();
            medicalMainData.setPosition(i);
            medicalMainData.setData("");
            MedicalMainBean medicalMainBean = new MedicalMainBean(medicalMainData);
            displayBeen.add(medicalMainBean);
        }
        return displayBeen;
    }

    private String formatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MM月dd日").format(date);
    }
}
