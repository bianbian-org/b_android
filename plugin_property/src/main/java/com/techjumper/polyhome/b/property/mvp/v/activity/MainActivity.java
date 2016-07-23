package com.techjumper.polyhome.b.property.mvp.v.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.event.LoginEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.activity.MainActivityPresenter;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ActionFragment;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ListFragment;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;

@Presenter(MainActivityPresenter.class)
public class MainActivity extends AppBaseActivity<MainActivityPresenter> {

    public static final int ANNOUNCEMENT = 0;
    public static final int REPAIR = 1;
    public static final int COMPLAINT = 2;

    @Bind(R.id.bottom_title)
    TextView bottomTitle;
    @Bind(R.id.bottom_date)
    TextView bottomDate;
    @Bind(R.id.title_date)
    TextView titleDate;

    private Timer timer = new Timer();

    public TextView getBottomDate() {
        return bottomDate;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomTitle.setText(R.string.property);
        bottomDate.setText(CommonDateUtil.getTitleDate());

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setId(bundle.getLong(PluginConstant.KEY_PRO_FAMILY_ID));
            userInfoEntity.setUser_id(bundle.getLong(PluginConstant.KEY_PRO_USER_ID));
            userInfoEntity.setTicket(bundle.getString(PluginConstant.KEY_PRO_TICKET));
            UserInfoManager.saveUserInfo(userInfoEntity);
        }

        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUser_id(362);
        userInfoEntity.setTicket("b87ec030678160e236c5af6dd1c3cee7f11fded3");
        userInfoEntity.setId(434);
        UserInfoManager.saveUserInfo(userInfoEntity);

        switchFragment(R.id.container, ListFragment.getInstance(MainActivity.ANNOUNCEMENT), false, false);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RxBus.INSTANCE.send(new TimeEvent());
            }
        }, 5000, 60000);

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof PropertyActionEvent) {
                        PropertyActionEvent event = (PropertyActionEvent) o;
                        if (event.isAction() == false) {
                            int listType = event.getListType();
                            replaceFragment(R.id.container, ListFragment.getInstance(listType));
                        }
                    }
                }));
    }
}
