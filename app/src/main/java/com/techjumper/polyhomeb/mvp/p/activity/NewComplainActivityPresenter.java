package com.techjumper.polyhomeb.mvp.p.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.entity.event.DeletePicNotifyEvent;
import com.techjumper.polyhomeb.entity.event.RefreshComplainListDataEvent;
import com.techjumper.polyhomeb.mvp.m.NewComplainActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewComplainActivity;
import com.techjumper.polyhomeb.utils.UploadPicUtil;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewComplainActivityPresenter extends AppBaseActivityPresenter<NewComplainActivity> {

    private NewComplainActivityModel mModel = new NewComplainActivityModel(this);
    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;
    private PolyPopupWindow mPop;
    private int mComplainChoose = -1;

    private List<String> mUrls = new ArrayList<>();
    private List<String> mBase64List = new ArrayList<>();

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
                    }
                }));
        initPopup();
        refreshComplainFragment();
    }

    private void refreshComplainFragment() {
        RxUtils.unsubscribeIfNotNull(mSubs4);
        addSubscription(
                mSubs4 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof RefreshComplainListDataEvent) {
                        getView().finish(); //自己收到自己发出的消息时,就证明这个消息已经成功发出,其他地方订阅了也能收到,所以这时候再finish当前页面
                    }
                }));
    }

    public void onTitleRightClick() {

        if (mComplainChoose == -1) {
            ToastUtils.show(getView().getString(R.string.please_choose_theme));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtPhone().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_your_phone));
            return;
        }
        if (TextUtils.isEmpty(getView().getEtContent().getEditableText().toString().trim())) {
            ToastUtils.show(getView().getString(R.string.please_input_your_content));
            return;
        }

        getView().showLoading();
        if (getView().getPhotos().size() != 0) {
//            new Thread(() -> {
//                transformCode();
//                if (mBase64List.size() == getView().getPhotos().size()) {
//                    getView().runOnUiThread(() -> uploadPic());
            uploadPic();
//                }
//            }).start();
        } else if (getView().getPhotos().size() == 0) {
            uploadData();
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    public ArrayList<String> getChoosedPhoto() {
        return getView().getPhotos();
    }

    @OnClick(R.id.layout_device)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_device:
                ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangle(), "rotation", 0f, 90f);
                animator.setDuration(300);
                animator.start();
                if (mPop.isShowing()) {
                    mPop.dismiss();
                }
                mPop.show(PolyPopupWindow.AnimStyle.ALPHA);
                KeyboardUtils.closeKeyboard(getView().getEtContent());
                break;
        }
    }

    private void uploadData() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.commitComplain(mComplainChoose + ""
                        , getView().getEtContent().getEditableText().toString().trim()
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
                                    //提交成功后,应当finish当前页面,回到上个界面,并且自动刷新数据,所以这里使用RxBus通知.
                                    //但是有可能出现,正在发送消息或者刚开始准备发送消息的时候,下面注释了的getView().finish()
                                    //会把当前页面关掉,导致发不出去,,所以我在当前Presenter中,订阅这发送的事件,当我自己收到我自己发出的消息,
                                    //就说明消息已经成功发布了,我就能finish当前页面了
                                    //这样的话就能保证其他地方也能收到这个消息
                                    //refreshComplainFragment()此方法就是起这个作用.
                                    RxBus.INSTANCE.send(new RefreshComplainListDataEvent(mModel.getComplainStatus()));
//                                    getView().finish();
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
//                        .from(mBase64List)
                        .from(getView().getPhotos())
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

    private void initPopup() {

        mPop = new PolyPopupWindow(getView(), R.style.popup_anim, new DevicePopListItemClick(), getView().getRightGroup(), new DevicePopDismiss());
        mPop.setMarginRight(8);
        mPop.setMarginTop(138);
        List<String> datas = new ArrayList<>();
        datas.add(getView().getString(R.string.complain));
        datas.add(getView().getString(R.string.advice));
        datas.add(getView().getString(R.string.celebrate));
        mPop.initData(datas);

    }

    private class DevicePopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            getView().getTv().setText(s);
            mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
            mComplainChoose = position + 1;  //1-投诉 2-建议 3-表扬
            switch (position) {
                case 0:
                    getView().getEtContent().setHint(getView().getResources().getString(R.string.complain_text_hint_complaint));
                    break;
                case 1:
                    getView().getEtContent().setHint(getView().getResources().getString(R.string.complain_text_hint_advice));
                    break;
                case 2:
                    getView().getEtContent().setHint(getView().getResources().getString(R.string.complain_text_hint_celebrate));
                    break;
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

    /**
     * 将图片转换为base64编码
     */
    private void transformCode() {
        mBase64List.clear();
        ArrayList<String> photos = getView().getPhotos();
        for (int i = 0; i < photos.size(); i++) {
            String base64 = UploadPicUtil.bitmapPath2Base64(photos.get(i));
            mBase64List.add(base64);
        }
    }

    public EditText getEtPhone() {
        return getView().getEtPhone();
    }

}
