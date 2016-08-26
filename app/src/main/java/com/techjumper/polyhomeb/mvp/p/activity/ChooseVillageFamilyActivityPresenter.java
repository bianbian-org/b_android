package com.techjumper.polyhomeb.mvp.p.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ChooseVillageEvent;
import com.techjumper.polyhomeb.entity.event.VillageEntity;
import com.techjumper.polyhomeb.mvp.m.ChooseVillageFamilyActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChooseVillageFamilyActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ScanHostQRCodeActivity;

import java.util.List;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChooseVillageFamilyActivityPresenter extends AppBaseActivityPresenter<ChooseVillageFamilyActivity> {

    private Subscription mSubs1, mSubs2;
    private boolean mIsShowRv = false;

    private ChooseVillageFamilyActivityModel mModel = new ChooseVillageFamilyActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getVillages();

        /****************假数据******************/
//        VillageEntity villageEntity = new VillageEntity();
//        VillageEntity.DataBean dataBean = new VillageEntity.DataBean();
//        List<VillageEntity.DataBean.InfosBean> list = new ArrayList<>();
//
//        for (int i = 0; i < 7; i++) {
//            VillageEntity.DataBean.InfosBean infosBean = new VillageEntity.DataBean.InfosBean();
//            infosBean.setProvince("省" + i);
//            List<VillageEntity.DataBean.InfosBean.VillagesBean> list1 = new ArrayList<>();
//            for (int j = 0; j < i + 1; j++) {
//                VillageEntity.DataBean.InfosBean.VillagesBean villagesBean = new VillageEntity.DataBean.InfosBean.VillagesBean();
//                villagesBean.setId(j);
//                villagesBean.setName("小区" + j + ".." + i);
//                list1.add(villagesBean);
//            }
//            infosBean.setVillages(list1);
//            list.add(infosBean);
//        }
//
//        dataBean.setInfos(list);
//        villageEntity.setData(dataBean);
//
//        mModel.processData(villageEntity);
//        getView().onProvinceDataReceive(mModel.getProvinces());
//        getView().onNameAndIdDataReceive(mModel.getNamesAndIds());
        /****************假数据******************/

        RxUtils.unsubscribeIfNotNull(mSubs2);
        mSubs2 = RxBus.INSTANCE
                .asObservable().subscribe(o -> {
                    if (o instanceof ChooseVillageEvent) {
                        ChooseVillageEvent event = (ChooseVillageEvent) o;
                        String name = event.getName();
                        List<String> provinces = mModel.getProvinces();
                        for (int i = 0; i < provinces.size(); i++) {
                            if (name.equals(provinces.get(i))) {
                                getView().sCurrentIndex = i;
                                getView().getVp().setCurrentItem(i);
                                getView().getVpAdapter().notifyDataSetChanged();
                                processClick(true);
                                reloadRv();
                                break;
                            }
                        }
                    }
                });
    }

    public void onTitleRightClick() {
        new AcHelper.Builder(getView()).target(ScanHostQRCodeActivity.class).start();
    }

    @OnClick(R.id.layout_triangle)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_triangle:
                processClick(mIsShowRv);
                break;
        }
    }

    private void processClick(boolean mIsShowRv) {
        if (!mIsShowRv) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangle(), "rotation", 0f, 180f);
            animator.setDuration(300);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangle(), "rotation", 180f, 0f);
            animator.setDuration(300);
            animator.start();
        }
        getView().getAdapter().loadData(getRvProvinceDatas());
        getView().getRv().setVisibility(!mIsShowRv ? View.VISIBLE : View.GONE);
        getView().processIndicatorAndText(mIsShowRv);
        getView().getDividerBig().setVisibility(!mIsShowRv ? View.VISIBLE : View.GONE);
        getView().getDividerSmall().setVisibility(!mIsShowRv ? View.VISIBLE : View.GONE);
        this.mIsShowRv = !mIsShowRv;
    }

    private void getVillages() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getVillages()
                        .subscribe(new Observer<VillageEntity>() {
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
                            public void onNext(VillageEntity villageEntity) {
                                getView().dismissLoading();
                                if (!processNetworkResult(villageEntity)) return;
                                if (villageEntity != null
                                        && villageEntity.getData() != null
                                        && villageEntity.getData().getInfos() != null
                                        && villageEntity.getData().getInfos().size() != 0) {
                                    mModel.processData(villageEntity);
                                    getView().onProvinceDataReceive(mModel.getProvinces());
                                    getView().onNameAndIdDataReceive(mModel.getNamesAndIds());
                                }
                            }
                        }));
    }

    public void reloadRv() {
        getView().getVillageAdapter().loadData(getRvVillageDatas(getView().sCurrentIndex));
    }

    public List<DisplayBean> getRvProvinceDatas() {
        return mModel.getRvProvinceDatas();
    }

    public List<DisplayBean> getRvVillageDatas(int currentIndex) {
        return mModel.getRvVillageDatas(currentIndex);
    }

    public int getComeFrom() {
        return mModel.getComeFrom();
    }
}
