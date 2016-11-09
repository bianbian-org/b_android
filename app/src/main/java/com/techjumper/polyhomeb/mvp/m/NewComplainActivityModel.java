package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairPhotoViewChoosedData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairPhotoViewDefaultPlusData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRepairPhotoViewChoosedBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRepairPhotoViewDefaultPlusBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.mvp.p.activity.NewComplainActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewComplainActivityModel extends BaseModel<NewComplainActivityPresenter> {

    public NewComplainActivityModel(NewComplainActivityPresenter presenter) {
        super(presenter);
    }

    public List<DisplayBean> getDatas() {

        List<DisplayBean> displayBeen = new ArrayList<>();

        if (0 == getPresenter().getChoosedPhoto().size()) {
            //默认的相机图片
            NewRepairPhotoViewDefaultPlusData newRepairPhotoViewDefaultPlusData = new NewRepairPhotoViewDefaultPlusData();
            newRepairPhotoViewDefaultPlusData.setImageResource(R.mipmap.icon_camera);
            newRepairPhotoViewDefaultPlusData.setSelectPhotoes(getPresenter().getChoosedPhoto());
            NewRepairPhotoViewDefaultPlusBean newRepairPhotoViewDefaultPlusBean = new NewRepairPhotoViewDefaultPlusBean(newRepairPhotoViewDefaultPlusData);
            displayBeen.add(newRepairPhotoViewDefaultPlusBean);
        } else if (1 == getPresenter().getChoosedPhoto().size() || 2 == getPresenter().getChoosedPhoto().size()) {
            for (int i = 0; i < getPresenter().getChoosedPhoto().size(); i++) {
                NewRepairPhotoViewChoosedData newRepairPhotoViewChoosedData = new NewRepairPhotoViewChoosedData();
                newRepairPhotoViewChoosedData.setMapPath(getPresenter().getChoosedPhoto().get(i));
                NewRepairPhotoViewChoosedBean newRepairPhotoViewChoosedBean = new NewRepairPhotoViewChoosedBean(newRepairPhotoViewChoosedData);
                displayBeen.add(newRepairPhotoViewChoosedBean);
            }
            //默认的"加号"图片
            NewRepairPhotoViewDefaultPlusData newRepairPhotoViewDefaultPlusData = new NewRepairPhotoViewDefaultPlusData();
            newRepairPhotoViewDefaultPlusData.setImageResource(R.mipmap.icon_photo_add);
            newRepairPhotoViewDefaultPlusData.setSelectPhotoes(getPresenter().getChoosedPhoto());
            NewRepairPhotoViewDefaultPlusBean newRepairPhotoViewDefaultPlusBean = new NewRepairPhotoViewDefaultPlusBean(newRepairPhotoViewDefaultPlusData);
            displayBeen.add(newRepairPhotoViewDefaultPlusBean);

        } else if (3 == getPresenter().getChoosedPhoto().size()) {
            for (int i = 0; i < 3; i++) {
                NewRepairPhotoViewChoosedData newRepairPhotoViewChoosedData = new NewRepairPhotoViewChoosedData();
                newRepairPhotoViewChoosedData.setMapPath(getPresenter().getChoosedPhoto().get(i));
                NewRepairPhotoViewChoosedBean newRepairPhotoViewChoosedBean = new NewRepairPhotoViewChoosedBean(newRepairPhotoViewChoosedData);
                displayBeen.add(newRepairPhotoViewChoosedBean);
            }
        }
        return displayBeen;
    }

    public Observable<TrueEntity> commitComplain(String types, String content, List<String> mUrls) {
        KeyValuePair keyValuePair = KeyValueCreator.newComplain(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getPresenter().getEtPhone().getEditableText().toString().trim()
                , types
                , content
                , list2Array(mUrls));
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .newComplain(entity)
                .compose(CommonWrap.wrap());
    }

//    public Observable<UploadPicEntity> uploadPic(String base64) {
//        KeyValuePair keyValuePair = KeyValueCreator.uploadPic(
//                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
//                , UserManager.INSTANCE.getTicket()
//                , base64);
//        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
//        return RetrofitHelper.<ServiceAPI>createDefault()
//                .uploadPic(entity)
//                .compose(CommonWrap.wrap());
//    }

    public Observable<UploadPicEntity> uploadPic(String filePath) {
//        File file = new File("/storage/emulated/0/DCIM/Camera/IMG_20161108_161909_HDR.jpg");
        File file = new File(filePath);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), new File(filePath)))
                .setType(MultipartBody.FORM)
                .build();
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .uploadPicFile(multipartBody)
                .compose(CommonWrap.wrap());
    }

    private String[] list2Array(List<String> mUrls) {
        if (mUrls.size() == 0) return new String[0];
        String[] arrays = new String[mUrls.size()];
        for (int i = 0; i < mUrls.size(); i++) {
            arrays[i] = mUrls.get(i);
        }
        return arrays;
    }

    public int getComplainStatus() {
        return getPresenter().getView().getIntent().getExtras().getInt(Constant.PROPERTY_COMPLAIN_STATUS, -1);
    }
}
