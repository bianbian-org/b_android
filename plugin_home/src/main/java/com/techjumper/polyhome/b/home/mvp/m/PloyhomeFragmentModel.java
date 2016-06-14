package com.techjumper.polyhome.b.home.mvp.m;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.net.KeyValueCreator;
import com.techjumper.polyhome.b.home.net.NetHelper;
import com.techjumper.polyhome.b.home.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/4/28.
 */
public class PloyhomeFragmentModel extends BaseModel<PloyhomeFragmentPresenter> {

    public PloyhomeFragmentModel(PloyhomeFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<InfoEntity> getInfo(int page) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.getInfo(ComConstant.defaultUserId, ComConstant.defaultTicket, String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }
}
