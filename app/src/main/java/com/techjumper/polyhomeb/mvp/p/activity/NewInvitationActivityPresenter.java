package com.techjumper.polyhomeb.mvp.p.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.SectionsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.entity.event.DeletePicNotifyEvent;
import com.techjumper.polyhomeb.mvp.m.NewInvitationActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewInvitationActivity;
import com.techjumper.polyhomeb.utils.UploadPicUtil;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewInvitationActivityPresenter extends AppBaseActivityPresenter<NewInvitationActivity> {

    private NewInvitationActivityModel mModel = new NewInvitationActivityModel(this);

    private Map<Integer, String> mSectionMap = new HashMap<>();
    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;
    private List<String> mUrls = new ArrayList<>();
    private List<String> mBase64List = new ArrayList<>();
    private int mSection = -1;
    private PolyPopupWindow mPop;
    public boolean isFirstRequestSuccess = false;
    private boolean alreadyReCalculate = false;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getSections();
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

    @OnClick(R.id.layout_type)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_type:
                if (mPop != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangle(), "rotation", 0f, 90f);
                    animator.setDuration(300);
                    animator.start();
                    if (mPop.isShowing()) {
                        mPop.dismiss();
                    }
                    mPop.show(PolyPopupWindow.AnimStyle.ALPHA);
                    KeyboardUtils.closeKeyboard(getView().getEtContent());
                } else {
                    ToastUtils.show(getView().getString(R.string.get_sections));
                }
                break;
        }
    }

    public void onTitleRightClick() {
        if (mSection == -1) {
            ToastUtils.show(getView().getString(R.string.please_choose_sections));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtTitle().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_your_title));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtContent().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_your_content));
            return;
        }

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

    private void initPop() {
        mPop = new PolyPopupWindow(getView(), R.style.popup_anim, new DevicePopListItemClick(), getView().getRootView(), new DevicePopDismiss());
        mPop.setAnchorView(getView().getTvType());
        mPop.setXoff(80);
        if (!isFirstRequestSuccess) {
            getSections();
            return;
        }
        List<String> datas = new ArrayList<>();
        for (String name : mSectionMap.values()) {
            datas.add(name);
        }
        mPop.initData(datas);
    }

    private class DevicePopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            getView().getTvType().setText(s);
            mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
            for (Map.Entry<Integer, String> entry : mSectionMap.entrySet()) {
                if (entry.getValue().equals(s)) {
                    mSection = entry.getKey();
                    break;
                }
            }
        }
    }

    private class DevicePopDismiss implements PolyPopupWindow.OnPopDismiss {

        @Override
        public void onDismiss() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangle(), "rotation", 90f, 0f);
            animator.setDuration(300);
            animator.start();
        }
    }

    private void uploadData() {
        RxUtils.unsubscribeIfNotNull(mSubs4);
        addSubscription(
                mSubs4 = mModel.newArticle(mSection + ""
                        , getView().getEtTitle().getEditableText().toString()
                        , getView().getEtContent().getEditableText().toString()
                        , mUrls
                        , ""   //# 帖子类型 0-非闲置 1-闲置
                        , ""   //# 现价
                        , ""   //# 原价
                        , "")  //# 是否接受议价 0-接受 1-不接受
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

    private void getSections() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getSections()
                        .subscribe(new Observer<SectionsEntity>() {
                            @Override
                            public void onCompleted() {
                                initPop();
                            }

                            @Override
                            public void onError(Throwable e) {
                                isFirstRequestSuccess = false;
                                ToastUtils.show(getView().getString(R.string.get_sections));
                            }

                            @Override
                            public void onNext(SectionsEntity sectionsEntity) {
                                if (!processNetworkResult(sectionsEntity)) return;
                                String[][] result = sectionsEntity.getData().getResult();
                                onSectionsReceive(result);
                                isFirstRequestSuccess = true;
                            }
                        }));
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

    /**
     * 将图片转换为base64编码
     */
    private void transformCode() {
        mBase64List.clear();
        ArrayList<String> photos = getView().getPhotos();
        for (int i = 0; i < photos.size(); i++) {
            String base64 = UploadPicUtil.bitmap2Base64(photos.get(i));
            mBase64List.add(base64);
        }
    }

    private void onSectionsReceive(String[][] result) {
        mSectionMap.clear();
        for (int i = 0; i < result.length; i++) {
            String[] strings = result[i];
            String index = strings[0];  //index
            String name = strings[1];   //name
            int convert2int = NumberUtil.convertToint(index, -1);
            if (convert2int == -1) {
                mSectionMap.put(Integer.parseInt(name), name);
            } else {
                mSectionMap.put(Integer.parseInt(index), name);
            }
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    public ArrayList<String> getChoosedPhoto() {
        return getView().getPhotos();
    }
}
