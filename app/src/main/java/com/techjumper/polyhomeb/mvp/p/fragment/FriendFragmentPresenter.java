package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.mvp.v.activity.NewInvitationActivity;
import com.techjumper.polyhomeb.mvp.v.activity.NewUnusedActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.FriendFragment;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class FriendFragmentPresenter extends AppBaseFragmentPresenter<FriendFragment> {

    private PolyPopupWindow mPopDevice;
    private Subscription mSub1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        initPop();
        reloadPage();
    }

    private void reloadPage() {
        RxUtils.unsubscribeIfNotNull(mSub1);
        addSubscription(
                mSub1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof ReloadWebPageEvent) {
                        getView().getWebView().reload();
                    }
                }));

    }

    public void onTitleRightClick() {
        clickAdd();
    }

    private void clickAdd() {
        if (mPopDevice.isShowing()) {
            mPopDevice.dismiss();
        }
        mPopDevice.show(PolyPopupWindow.AnimStyle.ALPHA);
    }

    @OnClick(R.id.iv_left_icon)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_icon:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
        }
    }

    private void initPop() {
        mPopDevice = new PolyPopupWindow(getView().getActivity(), R.style.popup_anim, new PopListClick(), getView().getRightIcon(), () -> {
        });
        mPopDevice.setMarginRight(10);
        mPopDevice.setMarginTop(210);
        mPopDevice.setBgPic(ResourceUtils.getDrawableRes(R.drawable.bg_pop_));
        List<String> datas = new ArrayList<>();  //发布帖子,发布闲置
        datas.add(getView().getString(R.string.pop_publish_invitation));
        datas.add(getView().getString(R.string.pop_publish_unused));
        mPopDevice.initData(datas);
    }

    private class PopListClick implements PolyPopupWindow.ItemClickCallBack {
        @Override
        public void callBack(int position, String s) {
            mPopDevice.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
            if (position == 0) {
                new AcHelper.Builder(getView().getActivity()).target(NewInvitationActivity.class).start();
            } else {
                new AcHelper.Builder(getView().getActivity()).target(NewUnusedActivity.class).start();
            }
        }
    }
}
