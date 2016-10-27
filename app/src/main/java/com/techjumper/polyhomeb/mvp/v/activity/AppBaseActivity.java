package com.techjumper.polyhomeb.mvp.v.activity;


import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.ui.activity.BaseFragmentActivity;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.corelib.utils.window.StatusbarHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.manager.ActivityStack;
import com.techjumper.polyhomeb.mvp.p.activity.AppBaseActivityPresenter;
import com.techjumper.polyhomeb.utils.TitleHelper;
import com.techjumper.progressdialog.KProgressHUD;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(AppBaseActivityPresenter.class)
public abstract class AppBaseActivity<T extends AppBaseActivityPresenter> extends BaseFragmentActivity<T> implements SwipeBackActivityBase {

    private List<Subscription> mSubList = new ArrayList<>();
    private KProgressHUD mProgress;
    private TitleHelper.Builder mTitleBuilder;


    /***************
     * compile 'me.imid.swipebacklayout.lib:library:1.0.0'侧滑库
     **************/
    //包括implements SwipeBackActivityBase
    //不需要设置透明
    private SwipeBackActivityHelper mHelper;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (canSlide2Close()) {
            mHelper.onPostCreate();
        }
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);

    }

    @Override
    public void scrollToFinishActivity() {
        me.imid.swipebacklayout.lib.Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**********************
     * compile 'me.imid.swipebacklayout.lib:library:1.0.0'侧滑库
     *********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = KProgressHUD.create(this)
                .setDimAmount(0.5F)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setOnDismissListener(dialog -> onDialogDismiss());

        if (!isWebViewActivity()) {
            initGeneralTitle();
        }

        if (canSlide2Close()) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            mSwipeBackLayout = getSwipeBackLayout();
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
            mSwipeBackLayout.setEdgeSize(20);
        }

        /**************************compile 'com.r0adkll:slidableactivity:2.0.5'侧滑库***********************/
        //需要在主题中设置两个透明
//        if (canSlide2Close()) {
//            SlidrConfig config = new SlidrConfig.Builder()
//                    .position(SlidrPosition.LEFT)
//                    .sensitivity(1f)
//                    .scrimColor(Color.BLACK)
//                    .scrimStartAlpha(0.8f)
//                    .scrimEndAlpha(0f)
//                    .velocityThreshold(2400)
//                    .distanceThreshold(0.25f)
//                    .edge(true)
//                    .edgeSize(0.18f)
//                    .build();
//            Slidr.attach(this, config);
//        }
        /**************************compile 'com.r0adkll:slidableactivity:2.0.5'侧滑库***********************/

    }

    protected boolean canSlide2Close() {
        return true;
    }

    public String getLayoutTitle() {
        return "";
    }

    protected boolean isWebViewActivity() {
        return false;
    }

    protected boolean showTitleRight() {
        return false;
    }

    protected boolean showTitleLeft() {
        return true;
    }

    protected boolean onTitleRightClick() {
        return false;
    }

    protected boolean onTitleLeftClick() {
        return false;
    }

    protected boolean useStatusBarTransform_() {
        return true;
    }

    private void initGeneralTitle() {
        mTitleBuilder = TitleHelper.create(mViewRoot)
                .title(getLayoutTitle())
                .showLeft(showTitleLeft())
                .showRight(showTitleRight())
                .leftIconClick(v -> {
                    if (!onTitleLeftClick()) {
                        onBackPressed();
                    }
                })
                .rightIconClick(v1 -> {
                    if (!onTitleRightClick()) {
                        KeyboardUtils.closeKeyboard(mViewRoot);
                    }
                });
        mTitleBuilder.process();
    }

    @Override
    protected StatusbarHelper.Builder onStatusbarTransform(StatusbarHelper.Builder builder) {
        View titleGroup = findViewById(R.id.title_group);
        return titleGroup == null ? super.onStatusbarTransform(builder) :
                (useStatusBarTransform_() ? builder.setActionbarView(titleGroup) : super.onStatusbarTransform(builder));
    }

    public void onResume() {
        MobclickAgent.onResume(this);
        ActivityStack.getInstance().setResumeActivity(this);
        super.onResume();
    }

    public void onPause() {
        MobclickAgent.onPause(this);
        ActivityStack.getInstance().setResumeActivity(null);
        super.onPause();
    }

    public List<Subscription> addSubscription(Subscription subscription) {
        mSubList.add(subscription);
        return mSubList;
    }

    public void unsubscribeAll() {
        for (Subscription sub : mSubList) {
            RxUtils.unsubscribeIfNotNull(sub);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribeAll();
    }

    public void showHint(String hint) {
        ToastUtils.showLong(hint);
    }

    public void showHintShort(String hint) {
        ToastUtils.show(hint);
    }

    public void showError(Throwable e) {
        ToastUtils.show(Utils.appContext.getString(R.string.error_to_connect_server));
    }

    public void showLoading() {
        showLoading(true);
    }

    public void showLoading(boolean cancellable) {
        mProgress.setCancellable(cancellable)
                .show();
    }

    protected void onDialogDismiss() {
        getPresenter().onDialogDismiss();
    }

    public void dismissLoading() {
        mProgress.dismiss();
    }

    @Override
    protected boolean useStatusBarTransform() {
        return useStatusBarTransform_();
    }
}
