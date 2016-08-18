package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ReplyCommentPhotoViewDefaultPlusData;
import com.techjumper.polyhomeb.adapter.recycler_Data.ReplyPhotoViewChoosedData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.ReplyCommentPhotoViewDefaultPlusBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.ReplyPhotoViewChoosedBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.mvp.p.activity.ReplyCommentActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/16
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ReplyCommentActivityModel extends BaseModel<ReplyCommentActivityPresenter> {

    public ReplyCommentActivityModel(ReplyCommentActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    private String getArticleId() {
        return getExtras().getString(Constant.JS_REPLY_ARTICLE_ID, "");
    }

    private String getCommentId() {
        return getExtras().getString(Constant.JS_REPLY_COMMENT_ID, "");
    }

    public List<DisplayBean> getDatas() {

        List<DisplayBean> displayBeen = new ArrayList<>();

        if (0 == getPresenter().getChoosedPhoto().size()) {
            //默认的相机图片
            ReplyCommentPhotoViewDefaultPlusData replyCommentPhotoViewDefaultPlusData = new ReplyCommentPhotoViewDefaultPlusData();
            replyCommentPhotoViewDefaultPlusData.setImageResource(R.mipmap.icon_camera);
            replyCommentPhotoViewDefaultPlusData.setSelectPhotoes(getPresenter().getChoosedPhoto());
            ReplyCommentPhotoViewDefaultPlusBean replyCommentPhotoViewDefaultPlusBean = new ReplyCommentPhotoViewDefaultPlusBean(replyCommentPhotoViewDefaultPlusData);
            displayBeen.add(replyCommentPhotoViewDefaultPlusBean);
        } else if (1 == getPresenter().getChoosedPhoto().size()) {
            ReplyPhotoViewChoosedData replyPhotoViewChoosedData = new ReplyPhotoViewChoosedData();
            replyPhotoViewChoosedData.setMapPath(getPresenter().getChoosedPhoto().get(0));
            ReplyPhotoViewChoosedBean replyPhotoViewChoosedBean = new ReplyPhotoViewChoosedBean(replyPhotoViewChoosedData);
            displayBeen.add(replyPhotoViewChoosedBean);
        }
        return displayBeen;
    }

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

    public Observable<TrueEntity> replyComment(String content, List<String> urls) {
        KeyValuePair keyValueCreator = KeyValueCreator.replyComment(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getArticleId()
                , getCommentId()
                , content
                , list2String(urls));
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValueCreator);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .replyComment(entity)
                .compose(CommonWrap.wrap());

    }

    private String list2String(List<String> mUrls) {
        if (mUrls.size() == 0) return "";
        return mUrls.get(0);
    }


}
