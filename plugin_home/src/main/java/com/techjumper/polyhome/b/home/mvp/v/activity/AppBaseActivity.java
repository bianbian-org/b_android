package com.techjumper.polyhome.b.home.mvp.v.activity;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.ui.activity.BaseFragmentActivity;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.activity.AppBaseActivityPresenter;
import com.techjumper.progressdialog.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(AppBaseActivityPresenter.class)
public abstract class AppBaseActivity<T extends AppBaseActivityPresenter> extends BaseFragmentActivity<T> {

    private List<Subscription> mSubList = new ArrayList<>();
    private KProgressHUD mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = KProgressHUD.create(this)
                .setDimAmount(0.F)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setOnDismissListener(dialog -> onDialogDismiss());

        //去除虚拟栏
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setAttributes(params);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    public String getLayoutTitle() {
        return "";
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
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
        JLog.d(e);
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
}
