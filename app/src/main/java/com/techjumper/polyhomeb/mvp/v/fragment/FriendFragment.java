package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.fragment.FriendFragmentPresenter;
import com.techjumper.polyhomeb.widget.PolyWebView;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(FriendFragmentPresenter.class)
public class FriendFragment extends AppBaseFragment<FriendFragmentPresenter> {

    @Bind(R.id.iv_right_icon)
    ImageView mRightIcon;
    @Bind(R.id.wb)
    PolyWebView mWebView;

    public static FriendFragment getInstance() {
        return new FriendFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mWebView.addJsInterface(getActivity(), Constant.JS_NATIVE_BRIDGE);
        mWebView.processBack();
        mWebView.loadUrl(Config.sFriend);
    }

    @Override
    public String getTitle() {
        return getString(R.string.friend);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    public ImageView getRightIcon() {
        return mRightIcon;
    }

    public PolyWebView getWebView() {
        return mWebView;
    }

}
