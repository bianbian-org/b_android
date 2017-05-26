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
import com.techjumper.polyhomeb.entity.event.RefreshRepairListDataEvent;
import com.techjumper.polyhomeb.mvp.m.NewRepairActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewRepairActivity;
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
public class NewRepairActivityPresenter extends AppBaseActivityPresenter<NewRepairActivity> {

    private NewRepairActivityModel mModel = new NewRepairActivityModel(this);
    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;
    private PolyPopupWindow mPopDevice, mPopType;
    private int mDeviceChoose, mTypeChoose = -1;

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
        refreshRepairFragment();
    }

    private void refreshRepairFragment() {
        RxUtils.unsubscribeIfNotNull(mSubs4);
        addSubscription(
                mSubs4 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof RefreshRepairListDataEvent) {
                        getView().finish();//自己收到自己发出的消息时,就证明这个消息已经成功发出,其他地方订阅了也能收到,所以这时候再finish当前页面
                    }
                }));
    }

    public void onTitleRightClick() {
        if (mDeviceChoose == -1) {
            ToastUtils.show(getView().getString(R.string.please_choose_repair_device));
            return;
        }
        if (mTypeChoose == -1) {
            ToastUtils.show(getView().getString(R.string.please_choose_repair_type));
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

    @OnClick({R.id.layout_device, R.id.layout_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_device:
                ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangleDevice(), "rotation", 0f, 90f);
                animator.setDuration(300);
                animator.start();
                if (mPopDevice.isShowing()) {
                    mPopDevice.dismiss();
                }
                mPopDevice.show(PolyPopupWindow.AnimStyle.ALPHA);
                KeyboardUtils.closeKeyboard(getView().getEtContent());
                break;
            case R.id.layout_type:
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(getView().getIvTriangleType(), "rotation", 0f, 90f);
                animator1.setDuration(300);
                animator1.start();
                if (mPopType.isShowing()) {
                    mPopType.dismiss();
                }
                mPopType.show(PolyPopupWindow.AnimStyle.ALPHA);
                KeyboardUtils.closeKeyboard(getView().getEtContent());
                break;
        }
    }

    private void uploadData() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.commitRepair(mTypeChoose + ""
                        , mDeviceChoose + ""
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
                                    //refreshRepairFragment()此方法就是起这个作用.
                                    RxBus.INSTANCE.send(new RefreshRepairListDataEvent(mModel.getRepairStatus()));
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
                        .concatMap(s -> mModel.uploadPic(s))
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

        mPopDevice = new PolyPopupWindow(getView(), R.style.popup_anim, new DevicePopListItemClick(), getView().getRightGroup(), new DevicePopDismiss());
        mPopDevice.setMarginRight(8);
        mPopDevice.setMarginTop(93);
        List<String> datas = new ArrayList<>();  //报修设备 1-门窗类 2-水电类 3-锁类 4-电梯类 5-墙类
        datas.add(getView().getString(R.string.pop_windows));
        datas.add(getView().getString(R.string.pop_water_elec));
        datas.add(getView().getString(R.string.pop_locks));
        datas.add(getView().getString(R.string.pop_elevators));
        datas.add(getView().getString(R.string.pop_walls));
        mPopDevice.initData(datas);

        mPopType = new PolyPopupWindow(getView(), R.style.popup_anim, new TypePopListItemClick(), getView().getRightGroup(), new TypePopDismiss());
        mPopType.setMarginRight(8);
        mPopType.setMarginTop(110);
        List<String> datas1 = new ArrayList<>();  //报修类型 1-个人报修 2-公共区域报修
        datas1.add(getView().getString(R.string.pop_personal));
        datas1.add(getView().getString(R.string.pop_common));
        mPopType.initData(datas1);
    }

    private class DevicePopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            getView().getTvDevice().setText(s);
            mPopDevice.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
//            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangleDevice(), "rotation", 90f, 0f);
//            animator.setDuration(300);
//            animator.start();
            mDeviceChoose = position + 1;
        }
    }

    private class TypePopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            getView().getTvType().setText(s);
            mPopType.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
//            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangleType(), "rotation", 90f, 0f);
//            animator.setDuration(300);
//            animator.start();
            mTypeChoose = position + 1;
        }
    }

    private class DevicePopDismiss implements PolyPopupWindow.OnPopDismiss {

        @Override
        public void onDismiss() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangleDevice(), "rotation", 90f, 0f);
            animator.setDuration(300);
            animator.start();
        }
    }

    private class TypePopDismiss implements PolyPopupWindow.OnPopDismiss {

        @Override
        public void onDismiss() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangleType(), "rotation", 90f, 0f);
            animator.setDuration(300);
            animator.start();
        }
    }

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
