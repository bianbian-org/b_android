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
import com.techjumper.corelib.utils.window.ToastUtils;
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

    private boolean mCanRefresh = true;   //可否下拉刷新
    private String mRefreshType = "";     //下拉刷新的类型:根据url带的refresh=这个参数来判断
    private boolean mIsOtherError = false;//是不是其他类型的错误(其他类型错误包括:404.500,等等,以及断网这种错误)

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
        WebTitleManager webTitleManager = new WebTitleManager(url, mViewRoot, this);
//        mRefreshType = webTitleManager.getRefreshType();
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
    public void onTitleLeftFirstClick(String mLeftFirstMethod) {
        switch (mLeftFirstMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                getPresenter().onTitleRightClick();
                break;
            default:
                onLineMethod(mLeftFirstMethod);
                break;
        }
    }

    @Override
    public void onTitleLeftSecondClick(String mLeftSecondMethod) {
        switch (mLeftSecondMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                getPresenter().onTitleRightClick();
                break;
            default:
                onLineMethod(mLeftSecondMethod);
                break;
        }
    }

    @Override
    public void onTitleRightFirstClick(String mRightFirstMethod) {
        switch (mRightFirstMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                getPresenter().onTitleRightClick();
                break;
            default:
                onLineMethod(mRightFirstMethod);
                break;
        }
    }

    @Override
    public void onTitleRightSecondClick(String mRightSecondMethod) {
        switch (mRightSecondMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                getPresenter().onTitleRightClick();
                break;
            default:
                onLineMethod(mRightSecondMethod);
                break;
        }
    }

    private void initListener() {
        mWebView.setOnWebViewChromeClientListener(this);
        mWebView.setOnWebViewReceiveErrorListener(this);
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //判断是哪种情况的刷新:如果是404,500之类的,或者断网这种错误,导致用户手动刷新的,那么肯定就需要reload
                //如果不是以上情况导致的用户手动下拉刷新,那么就调用refresh()刷新,具体的刷新方式,按照url的refresh=参数来做,也就是refresh()自己去判断
                //9月5日更改刷新逻辑客户端直接调用reload
//                if (mIsOtherError) {
                mWebView.reload();
//                    mIsOtherError = false;
//                } else {
//                refresh();
//                }
                new Handler().postDelayed(() -> stopRefresh(""), NetHelper.GLOBAL_TIMEOUT);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mCanRefresh;
            }
        });
    }

//    /**
//     * 此处的刷新不是调用webView的reload(),而是调用js的方法->JAVA_2_JS_REFRESH;js那边通过Ajax来刷新,所以不用单纯重新刷新界面
//     */
//    private void refresh() {
//        if (TextUtils.isEmpty(mRefreshType)) {
//            mWebView.reload();
//        } else {
//            mWebView.loadUrl("javascript:" + mRefreshType + "()");
//        }
//    }

    /**
     * 根据url传回来的method,调用页面的function
     */
    private void onLineMethod(String method) {
        if (TextUtils.isEmpty(method)) return;
        mWebView.loadUrl("javascript:" + method + "()");
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
        ToastUtils.show("友邻网页错误,错误码:" + errorCode);
        mIsOtherError = true;
    }

    /**
     * 处理Http是不是Error的接口
     */
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//        mWebView.loadUrl("http://pl.techjumper.com/neighbor/404");
//        mWebView.loadUrl(Config.sFriendErrorPage);
        ToastUtils.show("在友邻中,WebView的HTTP错误了");
        mIsOtherError = true;
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
