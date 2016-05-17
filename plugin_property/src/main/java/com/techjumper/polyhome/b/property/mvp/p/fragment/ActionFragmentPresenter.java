package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.m.ActionFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ActionFragment;

import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by kevin on 16/5/12.
 */
public class ActionFragmentPresenter extends AppBaseFragmentPresenter<ActionFragment> {

    ActionFragmentModel model = new ActionFragmentModel(this);

    @OnClick(R.id.lc_submit)
    void lcSubmit() {
        int type = getView().getLcType();
        String content = getView().getLcContent().getText().toString();

        submitComplaint(type, content);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public void submitComplaint(int type, String content) {
        getView().showLoading(false);

        addSubscription(model.submitComplaint(type, content)
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {
                        getView().dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                        getView().dismissLoading();
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (trueEntity == null ||
                                trueEntity.getData() == null)
                            return;

                        if (trueEntity.getData().getResult().equals("true")) {
                            ToastUtils.show(getView().getResources().getString(R.string.property_submit_success));
                        }
                    }
                }));
    }
}
