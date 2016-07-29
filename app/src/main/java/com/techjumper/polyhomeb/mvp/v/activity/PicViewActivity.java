package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.PicViewAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.PicViewActivityPresenter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(PicViewActivityPresenter.class)
public class PicViewActivity extends AppBaseActivity<PicViewActivityPresenter> {

    @Bind(R.id.vp)
    ViewPager mVp;
    @Bind(R.id.tv_right)
    TextView mTvRight;

    private ArrayList<String> mUrls;
    private String mCurrentUrl;
    private PicViewAdapter mAdapter;
    private int mCurrentUrlPosition = 0;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_pic_view);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mUrls = getPresenter().getChoosedPic();
        mCurrentUrl = getPresenter().getCurrentUrl();

        mAdapter = new PicViewAdapter(Glide.with(this), mUrls);

        mVp.setAdapter(mAdapter);
        for (int i = 0; i < mUrls.size(); i++) {
            if (mUrls.get(i).equals(mCurrentUrl)) {
                mCurrentUrlPosition = i;
                break;
            }
        }
        mVp.setCurrentItem(mCurrentUrlPosition);
        mVp.setOffscreenPageLimit(5);
        mTvRight.setText((mCurrentUrlPosition + 1) + "/" + mUrls.size());
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvRight.setText((position + 1) + "/" + mUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.show_big_pic);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }
}
