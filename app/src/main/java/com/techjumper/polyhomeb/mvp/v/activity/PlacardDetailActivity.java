package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.PlacardDetailActivityPresenter;
import com.techjumper.polyhomeb.widget.AdvancedWebView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(PlacardDetailActivityPresenter.class)
public class PlacardDetailActivity extends AppBaseActivity<PlacardDetailActivityPresenter> {

    @Bind(R.id.tv_title_)
    TextView mTvTitle;
    @Bind(R.id.btn_type)
    Button mBtnType;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.wb)
    AdvancedWebView mWebView;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_placard_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init();
        getPresenter().loading();
    }

    @Override
    public String getLayoutTitle() {
        return String.format(getResources().getString(R.string.placard_detail), getPresenter().getType());
    }

    private void init() {
        mTvTitle.setText(getPresenter().getTitle());
        mBtnType.setText(getPresenter().getType());
        mTvTime.setText(getPresenter().getTime());

    }

    public void onDataReceive(String data) {
        if (TextUtils.isEmpty(data)) return;
        String tpl = getFromAssets("notice.html");
        String web = tpl;
        if (tpl.contains("$content$")) {
            web = tpl.replace("$content$", data);
        }
        JLog.e(web);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        mWebView.loadDataWithBaseURL(null, web, "text/html", "utf-8", null);
    }

    private String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
