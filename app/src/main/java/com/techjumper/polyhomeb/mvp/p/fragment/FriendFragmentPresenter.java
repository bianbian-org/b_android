package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ChangeVillageIdRefreshEvent;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.entity.event.RefreshStopEvent;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.mvp.v.activity.NewInvitationActivity;
import com.techjumper.polyhomeb.mvp.v.activity.NewUnusedActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.FriendFragment;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class FriendFragmentPresenter extends AppBaseFragmentPresenter<FriendFragment> {

    private PolyPopupWindow mPopDevice;
    private Subscription mSubs1, mSubs2, mSubs3;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        initPop();
        reloadPage();
        reloadUrlAndRefreshWebPage();
    }

    //收到此消息,说明在侧边栏切换了家庭,那么此处就应该重新按照SP中存储的小区或者家庭的id来重载页面
    private void reloadUrlAndRefreshWebPage() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof ChooseFamilyVillageEvent) {
                                getView().getWebView().reload();
                            }
                        })
        );
    }

    private void reloadPage() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof ReloadWebPageEvent) {  //此方法没什么用了,只是在回复帖子之后使用
//                        getView().getWebView().reload();
                    } else if (o instanceof RefreshStopEvent) {  //此方法没有任何用了.之前是在JS中通知停止刷新
                        getView().stopRefresh("");
                    } else if (o instanceof ChangeVillageIdRefreshEvent) {
                        getView().reload();
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

    private void initPop() {
        mPopDevice = new PolyPopupWindow(getView().getActivity(), R.style.popup_anim, new PopListClick(), getView().getIvRightFirst(), () -> {
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
