package com.techjumper.polyhomeb.mvp.v.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.ui.fragment.BaseViewFragment;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.corelib.utils.window.StatusbarHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.fragment.AppBaseFragmentPresenter;
import com.techjumper.polyhomeb.utils.TitleHelper;
import com.techjumper.progressdialog.KProgressHUD;
import com.umeng.analytics.MobclickAgent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(AppBaseFragmentPresenter.class)
public abstract class AppBaseFragment<T extends AppBaseFragmentPresenter> extends BaseViewFragment<T> {

    private TitleHelper.Builder mTitleBuilder;
    private KProgressHUD mProgress;

    public boolean isPrepare = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = KProgressHUD.create(getActivity())
                .setDimAmount(0.5F)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setOnDismissListener(dialog -> onDialogDismiss());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        View offsetView = getStatusbarOffsetView(view);
        if (offsetView == null && view != null) {
            offsetView = view.findViewById(R.id.title_group);
        }

        if (offsetView != null)
            StatusbarHelper.setStatusBarOffset(offsetView);

        if (!isWebViewFragment()) {
            mTitleBuilder = TitleHelper.create(mViewRoot)
                    .title(getTitle())
                    .showLeft(showTitleLeft())
                    .showRight(showTitleRight())
                    .leftIconClick(v -> {
                        if (!onTitleLeftClick()) {
                            AcHelper.finish(getActivity());
                        }
                    })
                    .rightIconClick(v1 -> {
                        if (!onTitleRightClick()) {
                            KeyboardUtils.closeKeyboard(mViewRoot);

                        }
                    });
            mTitleBuilder.process();
        }
        isPrepare = true;
        return view;
    }

    protected boolean isWebViewFragment() {
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

    public TitleHelper.Builder getTitleBuilder() {
        return mTitleBuilder;
    }

    public String getTitle() {
        return "";
    }

    public void onResume() {
        MobclickAgent.onPageStart(getScreenName());
        super.onResume();
    }

    public void onPause() {
        MobclickAgent.onPageEnd(getScreenName());
        super.onPause();
    }

    protected View getStatusbarOffsetView(View view) {
        return null;
    }

    private String getScreenName() {
        return getActivity().getClass().getSimpleName() + "$" + getClass().getSimpleName();
    }

    public void showHint(String hint) {
        ToastUtils.showLong(hint);
    }

    public void showHintShort(String hint) {
        ToastUtils.show(hint);
    }

    public void showError(Throwable e) {
        ToastUtils.showLong(Utils.appContext.getString(R.string.error_to_connect_server));
        JLog.e(e.toString());
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
