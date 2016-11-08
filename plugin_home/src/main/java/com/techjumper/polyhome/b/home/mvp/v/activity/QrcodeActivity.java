package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.activity.QrcodeActivityPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

@Presenter(QrcodeActivityPresenter.class)
public class QrcodeActivity extends AppBaseActivity<QrcodeActivityPresenter> {
    public static final String TIME = "time";

    @Bind(R.id.bottom_title)
    TextView bottomTitle;
    @Bind(R.id.bottom_date)
    TextView bottomDate;
    private long time;
    @Bind(R.id.qr_code_img)
    ImageView qrCodeImg;

    public long getTime() {
        return time;
    }

    public TextView getBottomDate() {
        return bottomDate;
    }

    public ImageView getQrCodeImg() {
        return qrCodeImg;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_qrcode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomTitle.setText(R.string.title_qrcode);
        time = getIntent().getLongExtra(TIME, 0L);
    }
}
