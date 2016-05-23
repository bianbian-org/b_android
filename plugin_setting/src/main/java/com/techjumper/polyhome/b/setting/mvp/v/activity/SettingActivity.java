package com.techjumper.polyhome.b.setting.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.techjumper.commonres.entity.event.LoginEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.setting.R;
import com.techjumper.polyhome.b.setting.UserManager;
import com.techjumper.polyhome.b.setting.mvp.p.activity.SettingActivityPresenter;
import com.techjumper.polyhome.b.setting.mvp.v.fragment.LoginFragment;
import com.techjumper.polyhome.b.setting.mvp.v.fragment.SettingFragment;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/6.
 */
@Presenter(SettingActivityPresenter.class)
public class SettingActivity extends AppBaseActivity<SettingActivityPresenter> {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.date)
    TextView date;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.layout_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        date.setText(CommonDateUtil.getTitleDate());

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof LoginEvent) {
                        LoginEvent loginEvent = (LoginEvent) o;
                        if (loginEvent.isLogin()) {
                            switchToSetting();
                        } else {
                            switchToLogin();
                        }
                    }
                }));

        if (!UserManager.INSTANCE.isLogin()) {
            UserManager.INSTANCE.notifyLoginOrLogoutEvent(false);
        } else {
            UserManager.INSTANCE.notifyLoginOrLogoutEvent(true);
        }
    }

    private void switchToSetting() {
        if (!isFinishing()){
            replaceFragment(R.id.container, SettingFragment.getInstance());
            title.setText(R.string.setting);
        }
    }

    private void switchToLogin() {
        if (!isFinishing()){
            replaceFragment(R.id.container, LoginFragment.getInstance());
            title.setText(R.string.setting_login);
        }
    }
}
