package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.mvp.m.JoinVillageActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.JoinVillageActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.user.UserManager;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JoinVillageActivityPresenter extends AppBaseActivityPresenter<JoinVillageActivity> {

    private JoinVillageActivityModel mModel = new JoinVillageActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public String getName() {
        return mModel.getName();
    }

    @OnClick(R.id.tv_join)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_join:
                checkInputAndJoin();
                break;
        }
    }

    private void checkInputAndJoin() {
        if (TextUtils.isEmpty(getView().getEtBuilding().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_building_num));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtUnit().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_unit_num));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtRoom().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_room_num));
            return;
        }
        join();
    }

    private void join() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.joinVillage(
                        getView().getEtBuilding().getEditableText().toString()
                        , getView().getEtUnit().getEditableText().toString()
                        , getView().getEtRoom().getEditableText().toString())
                        .subscribe(new Observer<TrueEntity>() {
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
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (Constant.TRUE_ENTITY_RESULT.equals(trueEntity.getData().getResult())) {
                                    ToastUtils.show(getView().getString(R.string.commit_to_verify));
                                    getView().dismissLoading();
                                    //此处发送RxBus,目的是为了让用户加入之后,能够实时改变侧边栏和首页的title
                                    RxBus.INSTANCE.send(new ChooseFamilyVillageEvent(mModel.getId(), mModel.getName(), 1, -1));
                                    //将楼栋号,单元号,房间号,名字,id全都存下来
                                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_BUILDING, getView().getEtBuilding().getEditableText().toString());
                                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_UNIT, getView().getEtUnit().getEditableText().toString());
                                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_ROOM, getView().getEtRoom().getEditableText().toString());
                                    UserManager.INSTANCE.updateFamilyOrVillageInfo(false, mModel.getId() + "", mModel.getName(), mModel.getId());
                                    new AcHelper.Builder(getView())
                                            .closeCurrent(true)
                                            .enterAnim(R.anim.fade_in)
                                            .exitAnim(R.anim.fade_out)
                                            .target(TabHomeActivity.class)
                                            .start();
                                }
                            }
                        }));
    }
}
