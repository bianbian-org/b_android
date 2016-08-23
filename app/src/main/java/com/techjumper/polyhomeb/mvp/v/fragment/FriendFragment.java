package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.interfaces.IWebView;
import com.techjumper.polyhomeb.interfaces.IWebViewChromeClient;
import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.manager.WebTitleManager;
import com.techjumper.polyhomeb.mvp.p.fragment.FriendFragmentPresenter;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.utils.WebTitleHelper;
import com.techjumper.polyhomeb.widget.PolyWebView;
import com.techjumper.ptr_lib.PtrClassicFrameLayout;
import com.techjumper.ptr_lib.PtrDefaultHandler;
import com.techjumper.ptr_lib.PtrFrameLayout;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(FriendFragmentPresenter.class)
public class FriendFragment extends AppBaseFragment<FriendFragmentPresenter>
        implements IWebViewTitleClick
        , IWebViewChromeClient
        , IWebView {

    @Bind(R.id.right_first_iv)
    ImageView mIvRightFirst;
    @Bind(R.id.wb)
    PolyWebView mWebView;
    @Bind(R.id.ptr)
    PtrClassicFrameLayout mPtr;

    private boolean mCanRefresh = true;

    public static FriendFragment getInstance() {
        return new FriendFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
        return view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String url = Config.sFriend;
        new WebTitleManager(url, mViewRoot, this);
        mWebView.addJsInterface(getActivity(), Constant.JS_NATIVE_BRIDGE);
        mWebView.processBack();
        mWebView.loadUrl(url);
        initListener();
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
                getPresenter().onTitleRightClick();
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
                getPresenter().onTitleRightClick();
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
                getPresenter().onTitleRightClick();
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
                getPresenter().onTitleRightClick();
                break;
        }
    }

    private void initListener() {
        mWebView.setOnWebViewChromeClientListener(this);
        mWebView.setOnWebViewReceiveErrorListener(this);
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
                new Handler().postDelayed(() -> stopRefresh(""), NetHelper.GLOBAL_TIMEOUT);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mCanRefresh;
            }
        });
    }

    /**
     * 此处的刷新不是调用webView的reload(),而是调用js的方法->JAVA_2_JS_REFRESH;js那边通过Ajax来刷新,所以不用单纯重新刷新界面
     */
    private void refresh() {
        mWebView.loadUrl(Constant.JAVA_2_JS_REFRESH);
    }

    public void stopRefresh(String msg) {
        if (mPtr != null && mPtr.isRefreshing()) {
            if (!TextUtils.isEmpty(msg))
                showHint(msg);
            mPtr.refreshComplete();
        }
    }

    @Override
    public void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        if (mWebView != null) {
            mWebView.loadUrl("");
            mWebView.destroy();
        }
        super.onDetach();
    }

    /**
     * 页面加载完毕之后的接口
     */
    @Override
    public void onPageLoadFinish(WebView view, int newProgress) {
        stopRefresh("");
    }

    /**
     * 处理接收的Error的接口
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//        mWebView.loadUrl("http://pl.techjumper.com/neighbor/404");
//        mWebView.loadUrl(Config.sFriendErrorPage);
    }

    /**
     * 处理Http是不是Error的接口
     */
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//        mWebView.loadUrl("http://pl.techjumper.com/neighbor/404");
//        mWebView.loadUrl(Config.sFriendErrorPage);
    }

    /**
     * 判断mPtr能否滑动的监听
     */
    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mWebView.getTop() == t) {
            mCanRefresh = true;
        } else {
            mCanRefresh = false;
        }
    }

    public PolyWebView getWebView() {
        return mWebView;
    }

    public ImageView getIvRightFirst() {
        return mIvRightFirst;
    }

    public PtrClassicFrameLayout getPtr() {
        return mPtr;
    }
}
