package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.PropertyPlacardAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.PlacardFragmentPresenter;

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
        mAdapter.loadData(getPresenter().getData());
    }
}
