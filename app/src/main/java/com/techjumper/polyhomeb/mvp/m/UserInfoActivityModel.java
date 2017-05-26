package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.AvatarEntity;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.mvp.p.activity.UserInfoActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UserInfoActivityModel extends BaseModel<UserInfoActivityPresenter> {

    public UserInfoActivityModel(UserInfoActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<LoginEntity> setUserInfo(String username, String sex, String birthday, String email) {
        KeyValuePair keyValuePair = KeyValueCreator.setUserInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , username
                , sex
                , birthday
                , email);
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .setUserInfo(entity)
                .compose(CommonWrap.wrap());
    }

    /**
     * 将图片上传至服务器,返回图片在服务器的地址
     */
    public Observable<UploadPicEntity> uploadPic(String base64) {
        KeyValuePair keyValuePair = KeyValueCreator.uploadPic(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , base64);
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .uploadPic(entity)
                .compose(CommonWrap.wrap());
    }

    /**
     * 将上一个接口得到的地址,给服务器,表明这个图片是头像
     */
    public Observable<AvatarEntity> updateAvatar(String url) {
        KeyValuePair keyValuePair = KeyValueCreator.updateAvatar(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , url);
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .updateAvatar(entity)
                .compose(CommonWrap.wrap());
    }
}
