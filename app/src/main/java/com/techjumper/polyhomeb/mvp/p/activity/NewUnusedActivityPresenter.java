package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.entity.event.DeletePicNotifyEvent;
import com.techjumper.polyhomeb.mvp.m.NewUnusedActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewUnusedActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewUnusedActivityPresenter extends AppBaseActivityPresenter<NewUnusedActivity>
        implements CompoundButton.OnCheckedChangeListener {

    private NewUnusedActivityModel mModel = new NewUnusedActivityModel(this);

    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;
    private List<String> mUrls = new ArrayList<>();
    private List<String> mBase64List = new ArrayList<>();
    private boolean alreadyReCalculate = false;

    private int mDiscount = 1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof DeletePicNotifyEvent) {
                        DeletePicNotifyEvent event = (DeletePicNotifyEvent) o;
                        getView().getPhotos().remove(event.getPosition());
                        getView().getAdapter().loadData(getDatas());
                        getView().getAdapter().notifyDataSetChanged();
                        if (getView().getAdapter().getData().size() <= 3 && !alreadyReCalculate) {
                            getView().calculateHeight(true);
                            alreadyReCalculate = true;
                        }
                    }
                }));
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mDiscount = 0;
        } else {
            mDiscount = 1;
        }
    }

    public void onTitleRightClick() {
        if (TextUtils.isEmpty(getView().getEtTitle().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_your_title));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtContent().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_your_content));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtSecondHandPrice().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_your_second_hand_price));
            return;
        }

        getView().showLoading();
        if (getView().getPhotos().size() != 0) {
            uploadPic();
        } else if (getView().getPhotos().size() == 0) {
            uploadData();
        }
    }

    private void uploadData() {
        String originPrice;
        if (!TextUtils.isEmpty(getView().getEtOriginPrice().getEditableText().toString())) {
            originPrice = getView().getEtOriginPrice().getEditableText().toString();
        } else {
            originPrice = "";
        }
        RxUtils.unsubscribeIfNotNull(mSubs4);
        addSubscription(
                mSubs4 = mModel.newArticle(getView().getEtTitle().getEditableText().toString()
                        , getView().getEtContent().getEditableText().toString()
                        , mUrls
                        , "1"   //# 帖子类型 0-非闲置 1-闲置
                        , getView().getEtSecondHandPrice().getEditableText().toString()   //# 现价
                        , originPrice   //# 原价
                        , mDiscount + "")  //# 是否接受议价 0-接受 1-不接受
                        .asObservable().subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                                mUrls.clear();
                                mBase64List.clear();
                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) {
                                    getView().dismissLoading();
                                    return;
                                }
                                if (Constant.TRUE_ENTITY_RESULT.equals(trueEntity.getData().getResult())) {
                                    getView().showHint(getView().getString(R.string.commit_success));
                                    getView().dismissLoading();
                                    getView().finish();
                                } else {
                                    getView().showHint(getView().getString(R.string.commit_failed));
                                    getView().dismissLoading();
                                }
                            }
                        }));
    }

    private void uploadPic() {
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = Observable
                        .from(getView().getPhotos())
                        .switchMap(s -> mModel.uploadPic(s))
                        .map(uploadPicEntity -> {
                            mUrls.add(uploadPicEntity.getData().getUrl());
                            return uploadPicEntity;
                        })
                        .subscribe(new Observer<UploadPicEntity>() {
                            @Override
                            public void onCompleted() {
                                uploadData();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                                mUrls.clear();
                                mBase64List.clear();
                            }

                            @Override
                            public void onNext(UploadPicEntity uploadPicEntity) {
                                if (!processNetworkResult(uploadPicEntity)) return;
                            }
                        }));

    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    public ArrayList<String> getChoosedPhoto() {
        return getView().getPhotos();
    }

}
