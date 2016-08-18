package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.manager.WebTitleManager;
import com.techjumper.polyhomeb.mvp.p.fragment.ShoppingFragmentPresenter;
import com.techjumper.polyhomeb.utils.WebTitleHelper;
import com.techjumper.polyhomeb.widget.PolyWebView;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ShoppingFragmentPresenter.class)
public class ShoppingFragment extends AppBaseFragment<ShoppingFragmentPresenter> implements IWebViewTitleClick {

    @Bind(R.id.wb)
    PolyWebView mWebView;

    public static ShoppingFragment getInstance() {
        return new ShoppingFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String url = Config.sFriend;
        new WebTitleManager(url, mViewRoot, this);
        mWebView.addJsInterface(getActivity(), Constant.JS_NATIVE_BRIDGE);
        mWebView.processBack();
        mWebView.loadUrl(url);
    }

    @Override
    protected boolean isWebViewFragment() {
        return true;
    }

    @Override
    public void onTitleLeftFirstClick(int mLeftFirstIconType) {
        switch (mLeftFirstIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                break;
        }
    }

    @Override
    public void onTitleLeftSecondClick(int mLeftSecondIconType) {
        switch (mLeftSecondIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                break;
        }
    }

    @Override
    public void onTitleRightFirstClick(int mRightFirstIconType) {
        switch (mRightFirstIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                break;
        }
    }

    @Override
    public void onTitleRightSecondClick(int mRightSecondIconType) {
        switch (mRightSecondIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                break;
        }
    }

    public PolyWebView getWebView() {
        return mWebView;
    }
}
