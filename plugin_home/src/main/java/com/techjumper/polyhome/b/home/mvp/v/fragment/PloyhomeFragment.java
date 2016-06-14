package com.techjumper.polyhome.b.home.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(PloyhomeFragmentPresenter.class)
public class PloyhomeFragment extends AppBaseFragment<PloyhomeFragmentPresenter> {

    @Bind(R.id.notice_title)
    TextView noticeTitle;
    @Bind(R.id.notice_content)
    TextView noticeContent;

    public TextView getNoticeTitle() {
        return noticeTitle;
    }

    public TextView getNoticeContent() {
        return noticeContent;
    }

    public static PloyhomeFragment getInstance() {
        return new PloyhomeFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ployhome, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
