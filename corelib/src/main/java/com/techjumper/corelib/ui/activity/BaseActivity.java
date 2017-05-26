package com.techjumper.corelib.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.techjumper.corelib.utils.UI;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.StatusbarHelper;

import java.util.LinkedList;

import butterknife.ButterKnife;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/10
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public abstract class BaseActivity extends AppCompatActivity {

    private static LinkedList<Activity> sActivityList = new LinkedList<>();

    protected BaseActivity mThis;
    protected View mViewRoot;
    protected UI mUi;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sActivityList.add(this);
        mThis = this;
        mUi = UI.create(this);
        initData(savedInstanceState);
//        View contentRoot = findViewById(android.R.id.content);
//        //适配虚拟按键
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !AppUtils.isMeizu()) {
//            contentRoot.setPadding(contentRoot.getLeft(), contentRoot.getPaddingTop(), contentRoot.getRight()
//                    , contentRoot.getBottom() + StatusbarHelper.getNavigationBarHeight(this));
//        }
        mViewRoot = inflateView(savedInstanceState);
        if (mViewRoot == null) {
            mViewRoot = new View(this);
        }
        if (mViewRoot.getParent() == null) {
            setContentView(mViewRoot);
        }
        ButterKnife.bind(this);

        initView(savedInstanceState);

        if (useStatusBarTransform()) {
            StatusbarHelper.Builder statusbarBuidler = StatusbarHelper.from(this)
                    .noActionBar(true)
                    .setLightStatusBar(false) // 改变状态栏颜色true,状态栏字体颜色是黑色,false是白色
                    .setTransparentStatusbar(true)
//                .setLayoutRoot(findViewById(android.R.id.content))
                    ;

            onStatusbarTransform(statusbarBuidler).process();
        }
        find(android.R.id.content).post(() -> onViewInited(savedInstanceState));

    }

    protected StatusbarHelper.Builder onStatusbarTransform(StatusbarHelper.Builder builder) {
        return builder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sActivityList.remove(this);
        ButterKnife.unbind(this);
    }

    public static void finishAll() {
        for (Activity ac : sActivityList) {
            if (ac != null
                    && !ac.isFinishing()) {
                ac.finish();
            }
        }

    }

    @Override
    public void onBackPressed() {

        if (!getSupportFragmentManager().popBackStackImmediate()) {
            AcHelper.finish(this);
        }


    }

    protected <T extends View> T find(int id) {
        return mUi.findById(id);
    }

    protected <T extends View> T inflate(int id) {
        return inflate(id, false);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T inflate(int id, boolean attachToRoot) {
        return (T) getLayoutInflater().inflate(id, attachToRoot ?
                (ViewGroup) findViewById(android.R.id.content) : null);

    }


    protected abstract void initData(Bundle savedInstanceState);

    protected abstract View inflateView(Bundle savedInstanceState);

    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 界面初始化完毕,此时控件已获得宽高,可以弹对话框等等
     */
    protected abstract void onViewInited(Bundle savedInstanceState);

    protected boolean useStatusBarTransform() {
        return true;
    }


}
