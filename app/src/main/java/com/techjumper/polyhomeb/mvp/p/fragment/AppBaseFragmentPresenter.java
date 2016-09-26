package com.techjumper.polyhomeb.mvp.p.fragment;

import com.techjumper.corelib.mvp.presenter.BaseFragmentPresenterImp;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.BaseEntity;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.AppBaseFragment;
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
public abstract class AppBaseFragmentPresenter<T extends AppBaseFragment> extends BaseFragmentPresenterImp<T> {
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
    public void onDestroyView() {
        unsubscribeAll();
        super.onDestroyView();
    }

    protected boolean processNetworkResult(BaseEntity entity) {
        return processNetworkResult(entity, true);
    }

    protected boolean processNetworkResult(BaseEntity entity, boolean notifyNoData) {
        if (NetHelper.isSuccess(entity))
            return true;
        if (entity != null) {
            if (entity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                UserManager.INSTANCE.logout();
                UserManager.INSTANCE.logoutDontNotify(); //目的只是为了清空SP,否则会导致:账号在其他设备2登陆之后,修改了用户信息,然后回到设备1,发现被强制下线,需要登录.这时候需要清空SP,不然用户信息就不是最新的
//                boolean shouldClose = (!(getView().getActivity() instanceof TabHomeActivity));
                new AcHelper.Builder(getView().getActivity())
                        .target(LoginActivity.class)
//                        .closeCurrent(shouldClose)
                        .closeCurrent(true)
                        .start();
                getView().showHint(entity.getError_code() + ":" + entity.getError_msg());
            } else if (entity.getError_code() == NetHelper.CODE_NO_DATA) {
                if (notifyNoData)
                    getView().showHintShort(getView().getString(R.string.error_no_data));
            } else {
                getView().showHint(entity.getError_code() + ":" + entity.getError_msg());
            }
        } else
            getView().showError(null);
        return false;
    }

    public void onDialogDismiss() {

    }
}
