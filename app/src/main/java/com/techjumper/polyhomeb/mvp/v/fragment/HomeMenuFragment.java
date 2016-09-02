package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.StatusbarHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.HomeMenuAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeMenuFragmentPresenter;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(HomeMenuFragmentPresenter.class)
public class HomeMenuFragment extends AppBaseFragment<HomeMenuFragmentPresenter> {

    @Bind(R.id.layout_head)
    LinearLayout mLayout;
    @Bind(R.id.iv_avatar)
    ImageView mIvAvatar;
    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.iv_bg)
    ImageView mIvBg;

    @Bind(R.id.layout_avatar)
    FrameLayout layout;

    private HomeMenuAdapter mAdapter;

    public static HomeMenuFragment getInstance() {
        return new HomeMenuFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_menu, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        int statusBarHeightPx = StatusbarHelper.getStatusBarHeightPx(getActivity());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
        layoutParams.height -= statusBarHeightPx;
        layoutParams.topMargin = statusBarHeightPx;
        mLayout.setLayoutParams(layoutParams);

        //20dp-bar = margintop
        RelativeLayout.LayoutParams ivBGLayoutParams = (RelativeLayout.LayoutParams) mIvBg.getLayoutParams();
        ivBGLayoutParams.topMargin = RuleUtils.dp2Px(40) - statusBarHeightPx;
        mIvBg.setLayoutParams(ivBGLayoutParams);
        mLayout.requestLayout();

        //20dp-bar = margintop
        RelativeLayout.LayoutParams ivLayoutParams = (RelativeLayout.LayoutParams) layout.getLayoutParams();
        ivLayoutParams.topMargin = RuleUtils.dp2Px(40) - statusBarHeightPx;
        layout.setLayoutParams(ivLayoutParams);
        mLayout.requestLayout();


        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new HomeMenuAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());

        getPresenter().setAvatarAndName();

    }

    public TextView getTvUserName() {
        return mTvUserName;
    }

    public ImageView getIvAvatar() {
        return mIvAvatar;
    }

    public HomeMenuAdapter getAdapter() {
        return mAdapter;
    }

    public ImageView getIvBg() {
        return mIvBg;
    }

}
