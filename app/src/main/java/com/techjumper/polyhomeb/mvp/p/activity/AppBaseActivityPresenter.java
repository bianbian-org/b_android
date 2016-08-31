package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.mvp.presenter.BaseActivityPresenterImp;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.BaseEntity;
import com.techjumper.polyhomeb.mvp.v.activity.AppBaseActivity;
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
                UserManager.INSTANCE.logout();
//                boolean shouldClose = (!(getView() instanceof TabHomeActivity));
//                new AcHelper.Builder(getView())
//                        .target(LoginActivity.class)
//                        .closeCurrent(shouldClose)
//                        .start();
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
