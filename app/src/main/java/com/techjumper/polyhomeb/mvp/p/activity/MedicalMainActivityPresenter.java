package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalMainData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MedicalMainBean;
import com.techjumper.polyhomeb.entity.event.ReloadMedicalMainEvent;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalMainEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalMainActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeLoginAccountActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalUserInfoActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import retrofit2.adapter.rxjava.Result;
import rx.Observer;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainActivityPresenter extends AppBaseActivityPresenter<MedicalMainActivity> {

    private MedicalMainActivityModel mModel = new MedicalMainActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        getMainData(0);
        reload();
    }

    //切换了用户,需要刷新首页数据
    private void reload() {
        addSubscription(
                RxBus.INSTANCE.asObservable()
                        .subscribe(o -> {
                            if (o instanceof ReloadMedicalMainEvent) {
                                getMainData(0);
                            }
                        }));
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
            getMedicalData(count, mModel.getPathByCount(count));
            return count;
        } else {
            getMedicalData(count, mModel.getPathByCount(count));
            return count + getMainData(count + 1);
        }
    }

    private void getMedicalData(int count, String path) {
        //path->bp血压  po 血氧  bg 血糖  ecg 心电  wt 体重体成分  pdr 运动量  bt 体温
        addSubscription(
                mModel.getMedicalData(path, buildDataMap())
                        .subscribe(new Observer<Result<MedicalMainEntity>>() {
                            @Override
                            public void onCompleted() {
                                if (count == 9)
                                    getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Result<MedicalMainEntity> result) {
                                if (449 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_token_overdue));
                                    new AcHelper.Builder(getView()).closeCurrent(true).target(MedicalLoginActivity.class).start();
                                    return;
                                }
                                notifyItemDataChanged(path, result);
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

    public List<DisplayBean> getViewData() {
        return mModel.getViewData();
    }

    private void notifyItemDataChanged(String path, Result<MedicalMainEntity> result) {
        //如果某个接口的数据是空的,那么就直接不管,return就行.
        if (result == null
                || result.response() == null
                || result.response().body() == null
                || result.response().body().getData() == null
                || result.response().body().getData().size() == 0) return;
        List<MedicalMainEntity.DataBean> beanList = result.response().body().getData();
        notifyItem(path, beanList.get(0));
    }

    private void notifyItem(String path, MedicalMainEntity.DataBean dataBean) {
        List<DisplayBean> displayBeen = getView().getAdapter().getData();
        int position = mModel.getPositionByKey(path);
        String data = mModel.getDataByKey(path, dataBean);
        if ((displayBeen.get(position)) instanceof MedicalMainBean) {
            DisplayBean bean = displayBeen.get(position);
            MedicalMainBean medicalMainBean = (MedicalMainBean) bean;
            MedicalMainData medicalMainData = medicalMainBean.getData();
            medicalMainData.setData(data);
            medicalMainData.setDataBean(dataBean);
            getView().getAdapter().notifyItemChanged(position);

            if ("bp".equals(path)) {  //由于心率在接口中未有提供,所以这里从bp 血压中取得心率.
                DisplayBean bean1 = displayBeen.get(7);
                MedicalMainBean medicalMainBean1 = (MedicalMainBean) bean1;
                MedicalMainData medicalMainData1 = medicalMainBean1.getData();
                medicalMainData1.setData(mModel.getDataByKey("1", dataBean));  //此处的1代表MedicalMainActivityModel中的心率的"接口"
                medicalMainData1.setDataBean(dataBean);
                getView().getAdapter().notifyItemChanged(7);
            }
        }
    }
}
