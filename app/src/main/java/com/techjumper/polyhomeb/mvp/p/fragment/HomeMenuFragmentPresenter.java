package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.HomeMenuFragmentModel;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeMenuFragment;

import java.util.List;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeMenuFragmentPresenter extends AppBaseFragmentPresenter<HomeMenuFragment> {

    private HomeMenuFragmentModel mModel = new HomeMenuFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.layout_head)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                if(true) {
                    new AcHelper.Builder(getView().getActivity()).target(LoginActivity.class).start();
                } else {
//                    new AcHelper.Builder(getView().getActivity()).target(LogoutActivity.class).start();
                }
                break;
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }
}
