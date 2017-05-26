package com.techjumper.polyhomeb.utils;

import android.app.Activity;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class LoginHelper {

    private LoginHelper() {
    }

    /**
     * 某些功能需要登录后才能使用, 那么可以通过这个方法处理
     * PS: 会自动回到主线程
     */
    public static Observable<Object> processLogin(Activity activity) {
        LoginEvent event = new LoginEvent(false);
        if (UserManager.INSTANCE.isLogin()) {
            event.setIsLogin(true);
            Object o = event; //编译器对泛型懵了……必须这么写……
            return Observable.just(o)
                    .observeOn(AndroidSchedulers.mainThread());
        }

        startLoginActivity(activity);
        return RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void startLoginActivity(Activity activity) {
        new AcHelper.Builder(activity)
                .target(LoginActivity.class)
                .start();
    }

}
