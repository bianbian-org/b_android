package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.PropertyRepairAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.RepairFragmentPresenter;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.ptr_lib.PtrClassicFrameLayout;
import com.techjumper.ptr_lib.PtrDefaultHandler;
import com.techjumper.ptr_lib.PtrFrameLayout;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(RepairFragmentPresenter.class)
public class RepairFragment extends AppBaseFragment<RepairFragmentPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.ptr)
    PtrClassicFrameLayout mPtr;

    private PropertyRepairAdapter mAdapter;

    public static RepairFragment getInstance() {
        return new RepairFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repair, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PropertyRepairAdapter();
        mRv.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        mRv.setOnLoadMoreListener(() -> {
            getPresenter().getRepairData();
        });
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!UserManager.INSTANCE.isFamily()) {
                    ToastUtils.show(getActivity().getString(R.string.no_authority));
                    onRepairDataReceive(getPresenter().noData());
                    new Handler().postDelayed(() -> stopRefresh(""), 0);
                } else {
                    getPresenter().refreshData();
                    new Handler().postDelayed(() -> stopRefresh(""), NetHelper.GLOBAL_TIMEOUT);
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    public void stopRefresh(String msg) {
        if (mPtr != null && mPtr.isRefreshing()) {
            if (!TextUtils.isEmpty(msg))
                showHint(msg);
            mPtr.refreshComplete();
        }
    }

    public void setHasMoreData(boolean hasMoreData) {
        if (mRv != null) {
            mRv.setHasLoadMore(hasMoreData);
        }
    }

    public void showLoadMoreFail() {
        if (mRv == null) return;
        mRv.showFailUI();
    }

    public void loadMoreComplete() {
        if (mRv == null) return;
        mRv.onLoadMoreComplete();
    }

    public void onRepairDataReceive(List<DisplayBean> noticeData) {
        if (mRv == null) return;
        mAdapter.loadData(noticeData);
    }

    public PropertyRepairAdapter getAdapter() {
        return mAdapter;
    }

    public PtrClassicFrameLayout getPtr() {
        return mPtr;
    }


}
