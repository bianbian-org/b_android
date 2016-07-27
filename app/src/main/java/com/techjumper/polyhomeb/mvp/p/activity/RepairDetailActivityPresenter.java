package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailProprietorContentBean;
import com.techjumper.polyhomeb.mvp.m.RepairDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.RepairDetailActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RepairDetailActivityPresenter extends AppBaseActivityPresenter<RepairDetailActivity> {

    private RepairDetailActivityModel mModel = new RepairDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    @OnClick(R.id.tv_send)
    public void onClick(View view) {

        List<DisplayBean> datas = mModel.getDatas();

        //业主聊天
        PropertyRepairDetailProprietorContentData propertyRepairDetailProprietorContentData = new PropertyRepairDetailProprietorContentData();
        propertyRepairDetailProprietorContentData.setContent("美狗日杂些想作死就来");
        propertyRepairDetailProprietorContentData.setFailed(false);
        PropertyRepairDetailProprietorContentBean propertyRepairDetailProprietorContentBean = new PropertyRepairDetailProprietorContentBean(propertyRepairDetailProprietorContentData);
        datas.add(propertyRepairDetailProprietorContentBean);

        getView().getAdapter().loadData(datas);
        getView().getAdapter().notifyDataSetChanged();
        getView().getRv().smoothScrollToPosition(getView().getAdapter().getItemCount() + 1);
    }

}
