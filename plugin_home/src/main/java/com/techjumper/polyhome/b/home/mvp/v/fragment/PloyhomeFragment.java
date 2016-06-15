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
import com.techjumper.polyhome.b.home.widget.SquareView;

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
    @Bind(R.id.fp_temperature)
    SquareView fpTemperature;
    @Bind(R.id.fp_restrict)
    SquareView fpRestrict;

    public TextView getNoticeTitle() {
        return noticeTitle;
    }

    public TextView getNoticeContent() {
        return noticeContent;
    }

    public SquareView getFpTemperature() {
        return fpTemperature;
    }

    public SquareView getFpRestrict() {
        return fpRestrict;
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
