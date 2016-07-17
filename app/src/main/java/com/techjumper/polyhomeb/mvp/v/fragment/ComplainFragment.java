package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.PropertyComplainAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.ComplainFragmentPresenter;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ComplainFragmentPresenter.class)
public class ComplainFragment extends AppBaseFragment<ComplainFragmentPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;

    private PropertyComplainAdapter mAdapter;


    public static ComplainFragment getInstance() {
        return new ComplainFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complain, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PropertyComplainAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getData());
    }
}
