package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.mvp.presenter.BaseActivityPresenterImp;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.BaseEntity;
import com.techjumper.polyhomeb.mvp.v.activity.AppBaseActivity;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public abstract class AppBaseActivityPresenter<T extends AppBaseActivity> extends BaseActivityPresenterImp<T> {
    private List<Subscription> mSubList = new ArrayList<>();

    public List<Subscription> addSubscription(Subscription subscription) {
        mSubList.add(subscription);
        return mSubList;
    }

    public void unsubscribeAll() {
        for (Subscription sub : mSubList) {
            RxUtils.unsubscribeIfNotNull(sub);
        }

    }

    @Override
    public void onDestroy() {
        unsubscribeAll();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    protected boolean processNetworkResult(BaseEntity entity) {
        return processNetworkResult(entity, true);
    }

    protected boolean processNetworkResult(BaseEntity entity, boolean shouldShowMessage) {
        if (NetHelper.isSuccess(entity))
            return true;
        if (entity != null) {
            if (shouldShowMessage) {
                getView().showHint(entity.getError_code() + ":" + entity.getError_msg());
            }
            if (entity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                //这部分逻辑是为了,当ticket过期了,或者账号在其他地方登录之后,原设备还在进行操作,此时肯定是弹出"此功能登录后可用",但是界面还可以继续往下走
                //那么做了这部分逻辑,就能避免上述情况.当弹出"此功能登陆后可用"的时候,会跳转到登录界面
                // ,然后关闭当前界面.登陆成功后在登录界面的逻辑里面,跳转ChooseFamilyVillageActivity或者TabHomeActivity
                UserManager.INSTANCE.logout();
                UserManager.INSTANCE.logoutDontNotify(); //目的只是为了清空SP,否则会导致:账号在其他设备2登陆之后,修改了用户信息,然后回到设备1,发现被强制下线,需要登录.这时候需要清空SP,不然用户信息就不是最新的
//                boolean shouldClose = (!(getView() instanceof TabHomeActivity));
                new AcHelper.Builder(getView())
                        .target(LoginActivity.class)
//                        .closeCurrent(shouldClose)
                        .closeCurrent(true)
                        .start();
            } else if (entity.getError_code() == NetHelper.CODE_NO_DATA) {
                getView().showHintShort(getView().getString(R.string.error_no_data));
            } else if (entity.getError_code() == NetHelper.CODE_UPLOAD_FAILED) {
                getView().showHintShort(getView().getString(R.string.upload_failed));
            }
        } else
            getView().showError(null);
        return false;
    }

    public void onDialogDismiss() {

    }
}
