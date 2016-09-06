package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.SplashActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(SplashActivityPresenter.class)
public class SplashActivity extends AppBaseActivity<SplashActivityPresenter> {

    @Bind(R.id.iv_logo)
    ImageView mIvLogo;

    private boolean mCanBack;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_splash);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCanBack = false;
        if (Build.VERSION.SDK_INT >= 19) {
            AcHelper.fullScreen(this, true);
        }
    }

    @Override
    protected void onViewInited(Bundle savedInstanceState) {
        super.onViewInited(savedInstanceState);
        mIvLogo.setAlpha(0.F);
        ViewCompat.animate(mIvLogo)
                .alpha(1.F)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setStartDelay(500)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        PicassoHelper.load(R.mipmap.ic_launcher).noFade().into(mIvLogo);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        getPresenter().getFamilyAndVillage();
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK && !mCanBack) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AcHelper.fullScreen(this, false);
    }

    public void setCanBack(boolean bool) {
        mCanBack = bool;
    }


    @Override
    protected boolean canSlide2Close() {
        return false;
    }
}
