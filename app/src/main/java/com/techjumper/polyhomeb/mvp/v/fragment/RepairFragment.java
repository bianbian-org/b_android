package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.PropertyRepairAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.RepairFragmentPresenter;

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
        show(getPresenter().getDatas());
    }

    public void show(List<DisplayBean> datas) {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PropertyRepairAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(datas);
    }

}
