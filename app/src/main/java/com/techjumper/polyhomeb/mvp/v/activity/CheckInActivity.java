package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.CheckInActivityAdapter;
import com.techjumper.polyhomeb.entity.CheckInEntity;
import com.techjumper.polyhomeb.mvp.p.activity.CheckInActivityPresenter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(CheckInActivityPresenter.class)
public class CheckInActivity extends AppBaseActivity<CheckInActivityPresenter> {

    @Bind(R.id.iv_check_in)
    ImageView mIvCheck;
    @Bind(R.id.iv_house)
    ImageView mIvHouse;
    @Bind(R.id.rv)
    RecyclerViewFinal mRv;

    private CheckInActivityAdapter mAdapter;

    public boolean mCanCheckIn = false;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_check_in);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CheckInActivityAdapter();
        mRv.setAdapter(mAdapter);
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.check_in);
    }

    public void processView(CheckInEntity checkInEntity) {

        //没有数据,或者从未签过到的情况下
        if (checkInEntity == null
                || checkInEntity.getData() == null
                || checkInEntity.getData().getSign_days() == null
                || checkInEntity.getData().getSign_days().size() == 0) {
            showView(false);
            mAdapter.loadData(getPresenter().getData(checkInEntity));
            return;
        }

        List<Integer> signDays = checkInEntity.getData().getSign_days();

        //当前是几号
        Calendar instance = Calendar.getInstance();
        int date = instance.get(Calendar.DATE);
        boolean temp = false;
        if (Constant.TRUE_ENTITY_RESULT.equals(checkInEntity.getData().getResult())) {
            temp = true;
        }
        for (Integer i : signDays) {
            if (i == date) {
                temp = true;
                break;
            }
        }
        showView(temp);
        mAdapter.loadData(getPresenter().getData(checkInEntity));
    }

    //true代表当日已经签到
    private void showView(boolean isShow) {
        if (isShow) {
            PicassoHelper.getDefault().load(R.mipmap.icon_click_check_in_success).into(mIvCheck);
            mIvHouse.setImageBitmap(null);
            mCanCheckIn = false;
        } else {
            PicassoHelper.getDefault().load(R.mipmap.icon_click_to_check_in).into(mIvCheck);
            PicassoHelper.getDefault().load(R.mipmap.icon_check_in_house).into(mIvHouse);
            mCanCheckIn = true;
        }
    }
}
