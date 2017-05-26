package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.RoomManageAdapter;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRoomBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.RoomManageBean;
import com.techjumper.polyhomeb.mvp.p.activity.RoomManageActivityPresenter;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(RoomManageActivityPresenter.class)
public class RoomManageActivity extends AppBaseActivity<RoomManageActivityPresenter> {

    @Bind(R.id.tv_right)
    TextView mTvRight;     //右上角文字
    @Bind(R.id.rv)
    RecyclerViewFinal mRv;

    private RoomManageAdapter mAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_room_manage);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvRight.setText(R.string.edit);
        initDatas();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.room_manage);
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

    private void initDatas() {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RoomManageAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.setOnRoomItemClick(new RoomManageAdapter.IItemClick() {
            @Override
            public void onRoomItemClick(RoomManageBean data) {
                getPresenter().onRoomItemClick(data);
            }

            @Override
            public void onNewRoomItemClick(NewRoomBean data) {
                getPresenter().onNewRoomItemClick(data);
            }
        });
    }

    public void onRoomsDataReceive(List<DisplayBean> datas) {
        if (mAdapter == null) return;
        mAdapter.loadData(datas);
    }

    public RoomManageAdapter getAdapter() {
        return mAdapter;
    }
}
