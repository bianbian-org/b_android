package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.UnpaidFragmentAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.UnpaidFragmentPresenter;
import com.techjumper.ptr_lib.PtrClassicFrameLayout;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(UnpaidFragmentPresenter.class)
public class UnpaidFragment extends AppBaseFragment<UnpaidFragmentPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.ptr)
    PtrClassicFrameLayout mPtr;

    private UnpaidFragmentAdapter mAdapter;

    public static UnpaidFragment getInstance() {
        return new UnpaidFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unpaid, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        mAdapter = new UnpaidFragmentAdapter();
//        mRv.setAdapter(mAdapter);
//        initListener();
    }
//    private void initListener() {
//        mRv.setOnLoadMoreListener(() -> getPresenter().getRepairData());
//        mPtr.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                getPresenter().refreshData();
//                new Handler().postDelayed(() -> stopRefresh(""), NetHelper.GLOBAL_TIMEOUT);
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//        });
//    }
//
//    public void stopRefresh(String msg) {
//        if (mPtr != null && mPtr.isRefreshing()) {
//            if (!TextUtils.isEmpty(msg))
//                showHint(msg);
//            mPtr.refreshComplete();
//        }
//    }
//
//    public void setHasMoreData(boolean hasMoreData) {
//        if (mRv != null) {
//            mRv.setHasLoadMore(hasMoreData);
//        }
//    }
//
//    public void showLoadMoreFail() {
//        if (mRv == null) return;
//        mRv.showFailUI();
//    }
//
//    public void loadMoreComplete() {
//        if (mRv == null) return;
//        mRv.onLoadMoreComplete();
//    }
//
//    public void onRepairDataReceive(List<DisplayBean> noticeData) {
//        if (mRv == null) return;
//        mAdapter.loadData(noticeData);
//    }
//
//    public UnpaidFragmentAdapter getAdapter() {
//        return mAdapter;
//    }
//
//    public PtrClassicFrameLayout getPtr(){
//        return mPtr;
//    }
}
