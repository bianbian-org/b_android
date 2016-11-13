package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberDetailBean;
import com.techjumper.polyhomeb.mvp.m.MemberDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MemberDetailActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MemberDetailActivityPresenter extends AppBaseActivityPresenter<MemberDetailActivity>
        implements SwitchCompat.OnCheckedChangeListener {

    private MemberDetailActivityModel mModel = new MemberDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        loadData();
    }

    private void loadData() {
        getView().onMemberDetailDataReceive(mModel.loadData());
    }

    public String getMemberName() {
        return mModel.getMemberName();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ToastUtils.show(isChecked ? "管理员" : "非管理员");

        // TODO: 2016/11/13  得到最上面那个  是否设置为管理员  的结果
        //可以考虑  把这个结果和mModel里面的结果对比，如果和初始值相同，就不提交或者XXX，如果变了再提交(这一步肯定是放到onClick中)
    }

    public void onItemCheckedChange(boolean isChecked, MemberDetailBean dataBean) {
        ToastUtils.show(isChecked + "..");

        // TODO: 2016/11/13  得到所有改变过的item的结果
        //可以考虑  把这个结果和mModel里面的结果对比，取出变了的在提交(这一步肯定是放到onClick中)
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {

        // TODO: 2016/11/13  提交数据
    }

}
