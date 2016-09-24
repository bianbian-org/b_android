package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.MedicalMainActivityAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalMainActivityPresenter;
import com.techjumper.polyhomeb.other.MedicalDividerDecoration;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.utils.TitleHelper;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MedicalMainActivityPresenter.class)
public class MedicalMainActivity extends AppBaseActivity<MedicalMainActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.tv_user_name)
    TextView mTvName;

    private MedicalMainActivityAdapter mAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        TitleHelper.setTitleRightText(mViewRoot, getString(R.string.medical_change_account));

        mRv.setLayoutManager(new GridLayoutManager(this, 2));
        mRv.addItemDecoration(new MedicalDividerDecoration(this));
        mAdapter = new MedicalMainActivityAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getViewData());
        String nickName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_NICK_NAME);
        String pName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_P_NAME);

        mTvName.setText(TextUtils.isEmpty(nickName) ? pName : nickName);

    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.medical);
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

    public MedicalMainActivityAdapter getAdapter() {
        return mAdapter;
    }
}
