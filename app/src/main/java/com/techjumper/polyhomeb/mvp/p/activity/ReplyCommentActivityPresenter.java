package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.entity.event.DeletePicNotifyEvent;
import com.techjumper.polyhomeb.mvp.m.ReplyCommentActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyCommentActivity;
import com.techjumper.polyhomeb.utils.UploadPicUtil;

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
public class ReplyCommentActivityPresenter extends AppBaseActivityPresenter<ReplyCommentActivity> {

    private ReplyCommentActivityModel mModel = new ReplyCommentActivityModel(this);

    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;
    private List<String> mBase64List = new ArrayList<>();
    private List<String> mUrls = new ArrayList<>();

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof DeletePicNotifyEvent) {
                        DeletePicNotifyEvent event = (DeletePicNotifyEvent) o;
                        getView().getPhotos().remove(event.getPosition());
                        getView().getAdapter().loadData(getDatas());
                        getView().getAdapter().notifyDataSetChanged();
                        getView().changeRightGroup((getChoosedPhoto().size() > 0
                                || getView().getEtContent().getEditableText().toString().trim().length() > 0) ? true : false);
                    }
                }));

        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxTextView.afterTextChangeEvents(getView().getEtContent())
                        .asObservable().subscribe(textViewAfterTextChangeEvent -> {
                            String trim = textViewAfterTextChangeEvent.editable().toString().trim();
                            getView().changeRightGroup((trim.length() >= 1
                                    || getChoosedPhoto().size() > 0) ? true : false);
                        }));
    }

    public void onTitleLeftClick() {
        if (!TextUtils.isEmpty(getView().getEtContent().getEditableText().toString().trim())
                || getView().getPhotos().size() != 0) {
            showDialog();
//            JLog.e(mUrls.get(0));
        } else {
            getView().finish();
        }
    }

    public void onTitleRightClick() {

        getView().showLoading();
        if (getView().getPhotos().size() != 0) {
            new Thread(() -> {
                transformCode();
                if (mBase64List.size() == getView().getPhotos().size()) {
                    getView().runOnUiThread(() -> uploadPic());
                }
            }).start();
        } else if (getView().getPhotos().size() == 0) {
            uploadData();
        }

    }

    private void showDialog() {
        DialogUtils.getBuilder(getView())
                .content(R.string.confirm_cancel)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            getView().finish();
                            break;
                    }
                })
                .show();
    }

    private void uploadPic() {
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = Observable
                        .from(mBase64List)
                        .flatMap(s -> mModel.uploadPic(s))
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
                            }
                        }));
    }

    private void uploadData() {
        RxUtils.unsubscribeIfNotNull(mSubs4);
        addSubscription(
                mSubs4 = mModel.replyComment(
                        getView().getEtContent().getEditableText().toString().trim()
                        , mUrls)
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
                                    //发出消息,让网页reload当前页面
//                                    RxBus.INSTANCE.send(new ReloadWebPageEvent());
                                    getView().finish();
                                } else {
                                    getView().showHint(getView().getString(R.string.commit_failed));
                                    getView().dismissLoading();
                                }
                            }
                        }));
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    public ArrayList<String> getChoosedPhoto() {
        return getView().getPhotos();
    }

    private void transformCode() {
        mBase64List.clear();
        ArrayList<String> photos = getView().getPhotos();
        for (int i = 0; i < photos.size(); i++) {
            String base64 = UploadPicUtil.bitmap2Base64(photos.get(i));
            mBase64List.add(base64);
        }
    }
}
