package com.techjumper.polyhome.b.home.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.widget.AdViewPager;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome.b.home.widget.MyViewPager;
import com.techjumper.polyhome.b.home.widget.SquareView;
import com.techjumper.polyhome.b.home.widget.TextAutoView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(PloyhomeFragmentPresenter.class)
public class PloyhomeFragment extends AppBaseFragment<PloyhomeFragmentPresenter> {

    @Bind(R.id.fp_temperature)
    SquareView fpTemperature;
    @Bind(R.id.fp_restrict)
    SquareView fpRestrict;
    @Bind(R.id.notice_num)
    TextView noticeNum;
    @Bind(R.id.notice_title)
    TextView noticeTitle;
    @Bind(R.id.notice_content)
    TextAutoView noticeContent;
    @Bind(R.id.image_ad)
    ImageView ad;
    @Bind(R.id.video_ad)
    MyTextureView textureView;
    @Bind(R.id.jujia)
    SquareView jujia;
    @Bind(R.id.smarthome)
    SquareView smarthome;
    @Bind(R.id.property)
    SquareView property;
    @Bind(R.id.notice_layout)
    RelativeLayout noticeLayout;
    @Bind(R.id.shopping)
    SquareView shopping;
    @Bind(R.id.ad)
    FrameLayout ad_layout;
    @Bind(R.id.advp)
    AdViewPager advp;

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

    public ImageView getAd() {
        return ad;
    }

    public MyTextureView getTextureView() {
        return textureView;
    }

    public static PloyhomeFragment getInstance() {
        return new PloyhomeFragment();
    }

    public SquareView getProperty() {
        return property;
    }

    public SquareView getShopping() {
        return shopping;
    }

    public RelativeLayout getNoticeLayout() {
        return noticeLayout;
    }

    public SquareView getSmarthome() {
        return smarthome;
    }

    public SquareView getJujia() {
        return jujia;
    }

    public FrameLayout getAd_layout() {
        return ad_layout;
    }

    public TextView getNoticeNum() {
        return noticeNum;
    }

    public AdViewPager getAdvp() {
        return advp;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ployhome, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }
}
