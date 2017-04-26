package com.techjumper.polyhomeb.mvp.v.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.fragment.LvDiFragmentPresenter;
import com.techjumper.polyhomeb.widget.AdvancedWebView;

import static com.techjumper.polyhomeb.R.id.wb;

@Presenter(LvDiFragmentPresenter.class)
public class LvDiFragment extends AppBaseWebViewFragment<LvDiFragmentPresenter> {

    public static LvDiFragment getInstance() {
        return new LvDiFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lvdi, null);
        initWebView((AdvancedWebView) view.findViewById(wb));
        view.findViewById(R.id.title_group).setBackgroundResource(R.color.color_a8a8a8);
        return view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init();
    }

    @Override
    public String getTitle() {
        return Utils.appContext.getString(R.string.lv_di_title);
    }


    private void init() {
        String url = Config.LV_DI_URL;
        getWebView().loadUrl(url);
    }

}
