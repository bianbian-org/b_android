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
import com.techjumper.polyhomeb.adapter.PropertyPlacardAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.PlacardFragmentPresenter;
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
@Presenter(PlacardFragmentPresenter.class)
public class PlacardFragment extends AppBaseFragment<PlacardFragmentPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.ptr)
    PtrClassicFrameLayout mPtr;

    private PropertyPlacardAdapter mAdapter;

    public static PlacardFragment getInstance() {
        return new PlacardFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_placard, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PropertyPlacardAdapter();
        mRv.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        mRv.setOnLoadMoreListener(() -> {
            getPresenter().getNoticeData();
        });
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!UserManager.INSTANCE.isFamily()) {
                    ToastUtils.show(getActivity().getString(R.string.no_authority));
                    onNoticeDataReceive(getPresenter().noData());
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

    public void onNoticeDataReceive(List<DisplayBean> noticeData) {
        if (mRv == null) return;
        mAdapter.loadData(noticeData);
    }
}
