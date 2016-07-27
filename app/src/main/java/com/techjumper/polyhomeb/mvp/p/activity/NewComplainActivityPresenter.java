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
import com.techjumper.polyhomeb.mvp.m.NewComplainActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewComplainActivity;
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
public class NewComplainActivityPresenter extends AppBaseActivityPresenter<NewComplainActivity> {

    private NewComplainActivityModel mModel = new NewComplainActivityModel(this);
    private Subscription mSubs1;
    private PolyPopupWindow mPop;
    private int mComplainChoose = -1;

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
        if (mComplainChoose == -1) {
            ToastUtils.show("请选择主题");
            return;
        }
        if (TextUtils.isEmpty(getView().getEtContent().getEditableText().toString().trim())) {
            ToastUtils.show("请输入你的内容");
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

    private void initPopup() {

        mPop = new PolyPopupWindow(getView(), R.style.popup_anim, new DevicePopListItemClick(), getView().getRightGroup(), new DevicePopDismiss());
        mPop.setMarginRight(8);
        mPop.setMarginTop(138);
        List<String> datas = new ArrayList<>();
        datas.add(getView().getString(R.string.complaint));
        datas.add(getView().getString(R.string.advice));
        datas.add(getView().getString(R.string.celebrate));
        mPop.initData(datas);

    }

    private class DevicePopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            getView().getTv().setText(s);
            mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
//            ObjectAnimator animator = ObjectAnimator.ofFloat(getView().getIvTriangle(), "rotation", 90f, 0f);
//            animator.setDuration(300);
//            animator.start();
            mComplainChoose = position;
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

}
