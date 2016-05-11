package com.techjumper.polyhome.mvp.m;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.LoginEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.Config;
import com.techjumper.polyhome.UserManager;
import com.techjumper.polyhome.mvp.p.fragment.SettingFragmentPresenter;
import com.techjumper.polyhome.net.KeyValueCreator;
import com.techjumper.polyhome.net.NetHelper;
import com.techjumper.polyhome.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/5/11.
 */
public class SettingFragmentModel extends BaseModel<SettingFragmentPresenter> {

    public SettingFragmentModel(SettingFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<LoginEntity> changePassword(String password, String new_password, String new_password_check) {
        String user_id = String.valueOf(UserManager.INSTANCE.getUserId());
        KeyValuePair changePasswordPair = KeyValueCreator.changePassword(user_id,
                UserManager.INSTANCE.getTicket(), password, new_password, new_password_check);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(changePasswordPair);

        return RetrofitHelper.<ServiceAPI>create(Config.sBaseBUrl, ServiceAPI.class)
                .changePassword(argument)
                .compose(CommonWrap.wrap());
    }

    public Observable<LoginEntity> updateUserInfo(String username, String sex, String birthday, String email) {
        String user_id = String.valueOf(UserManager.INSTANCE.getUserId());
        KeyValuePair changePasswordPair = KeyValueCreator.updateUserInfo(user_id,
                UserManager.INSTANCE.getTicket(), username, sex, birthday, email);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(changePasswordPair);

        return RetrofitHelper.<ServiceAPI>create(Config.sBaseBUrl, ServiceAPI.class)
                .updateUserInfo(argument)
                .compose(CommonWrap.wrap());
    }
}
