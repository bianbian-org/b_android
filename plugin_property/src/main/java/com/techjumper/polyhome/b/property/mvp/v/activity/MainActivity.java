package com.techjumper.polyhome.b.property.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.p.activity.MainActivityPresenter;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ListFragment;

import butterknife.Bind;

@Presenter(MainActivityPresenter.class)
public class MainActivity extends AppBaseActivity<MainActivityPresenter> {

    @Bind(R.id.title_date)
    TextView titleDate;
    @Bind(R.id.bottom_back)
    ImageView bottomBack;
    @Bind(R.id.bottom_home)
    ImageView bottomHome;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleDate.setText("3月18日  周五  22:45");

        replaceFragment(R.id.container, ListFragment.getInstance());
    }
}
