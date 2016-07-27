package com.techjumper.polyhomeb.mvp.p.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.DeleteNotifyEvent;
import com.techjumper.polyhomeb.mvp.m.NewRepairActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewRepairActivity;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewRepairActivityPresenter extends AppBaseActivityPresenter<NewRepairActivity> {

    private NewRepairActivityModel mModel = new NewRepairActivityModel(this);
    private Subscription mSubs1;
    private PolyPopupWindow mPopDevice, mPopType;
    private int mDeviceChoose, mTypeChoose = -1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof DeleteNotifyEvent) {
                        DeleteNotifyEvent event = (DeleteNotifyEvent) o;
                        getView().getPhotos().remove(event.getPosition());
                        getView().getAdapter().loadData(getDatas());
                        getView().getAdapter().notifyDataSetChanged();
                    }
                }));
        initPopup();
    }

    public void onTitleRightClick() {
        if (mDeviceChoose == -1) {
            ToastUtils.show("请选择报修设备");
            return;
        }
        if (mTypeChoose == -1) {
            ToastUtils.show("请选择报修类型");
            return;
        }
        if (TextUtils.isEmpty(getView().getEtContent().getEditableText().toString().trim())) {
            ToastUtils.show("请输入报修内容");
            return;
        }
        ToastUtils.show("提交");
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

    private void initPopup() {

        mPopDevice = new PolyPopupWindow(getView(), R.style.popup_anim, new DevicePopListItemClick(), getView().getRightGroup(), new DevicePopDismiss());
        mPopDevice.setMarginRight(8);
        mPopDevice.setMarginTop(93);
        List<String> datas = new ArrayList<>();
        datas.add(getView().getString(R.string.repair_windows));
        datas.add(getView().getString(R.string.repair_walls));
        datas.add(getView().getString(R.string.repair_elevators));
        datas.add(getView().getString(R.string.repair_water_elec));
        datas.add(getView().getString(R.string.repair_locks));
        mPopDevice.initData(datas);

        mPopType = new PolyPopupWindow(getView(), R.style.popup_anim, new TypePopListItemClick(), getView().getRightGroup(), new TypePopDismiss());
        mPopType.setMarginRight(8);
        mPopType.setMarginTop(110);
        List<String> datas1 = new ArrayList<>();
        datas1.add(getView().getString(R.string.repair_personal));
        datas1.add(getView().getString(R.string.repair_common));
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
            mDeviceChoose = position;
        }
    }

    private class TypePopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            getView().getTvType().setText(s);
            mPopType.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangleType(), "rotation", 90f, 0f);
            animator.setDuration(300);
            animator.start();
            mTypeChoose = position;
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
}
