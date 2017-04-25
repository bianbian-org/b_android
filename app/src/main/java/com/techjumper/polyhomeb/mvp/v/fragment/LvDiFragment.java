package com.techjumper.polyhomeb.mvp.v.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.PolyTabShowEvent;
import com.techjumper.polyhomeb.mvp.p.fragment.LvDiFragmentPresenter;

import butterknife.Bind;
import butterknife.OnClick;

@Presenter(LvDiFragmentPresenter.class)
public class LvDiFragment extends AppBaseWebViewFragment<LvDiFragmentPresenter> {

    @Bind(R.id.wb)
    com.techjumper.polyhomeb.widget.PolyWebView wb;
    @Bind(R.id.iv_left_icon)
    ImageView layout;

    private boolean isFirst = true;

    public static LvDiFragment getInstance() {
        return new LvDiFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lvdi, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getTitle() {
        return Utils.appContext.getString(R.string.lv_di_title);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepare && isVisibleToUser) {
            if (isFirst) {
                isFirst = false;
                init();
            }
        }
    }

    private void init() {
        String url = Config.LV_DI_URL;
        wb.loadUrl(url);
    }

    @OnClick(R.id.tv_title)
    public void onClick(View view) {
        RxBus.INSTANCE.send(new PolyTabShowEvent());
    }
}
