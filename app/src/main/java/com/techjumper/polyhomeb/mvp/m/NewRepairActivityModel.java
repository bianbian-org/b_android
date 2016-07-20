package com.techjumper.polyhomeb.mvp.m;

import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairPhotoViewChoosedData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairPhotoViewDefaultPlusData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRepairPhotoViewChoosedBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRepairPhotoViewDefaultPlusBean;
import com.techjumper.polyhomeb.mvp.p.activity.NewRepairActivityPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewRepairActivityModel extends BaseModel<NewRepairActivityPresenter> {

    public NewRepairActivityModel(NewRepairActivityPresenter presenter) {
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
            new Thread(() -> {
                for (int i = 0; i < getPresenter().getChoosedPhoto().size(); i++) {
                    Uri uri = Uri.fromFile(new File(getPresenter().getChoosedPhoto().get(i)));
                    try {
                        Bitmap bitmap = Glide.with(getPresenter().getView())
                                .load(uri)
                                .asBitmap()
                                .centerCrop()
                                .into(RuleUtils.dp2Px(70), RuleUtils.dp2Px(70))
                                .get();
                        NewRepairPhotoViewChoosedData newRepairPhotoViewChoosedData = new NewRepairPhotoViewChoosedData();
                        newRepairPhotoViewChoosedData.setChoosed(bitmap);
                        NewRepairPhotoViewChoosedBean newRepairPhotoViewChoosedBean = new NewRepairPhotoViewChoosedBean(newRepairPhotoViewChoosedData);
                        displayBeen.add(newRepairPhotoViewChoosedBean);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            //默认的"加号"图片
            NewRepairPhotoViewDefaultPlusData newRepairPhotoViewDefaultPlusData = new NewRepairPhotoViewDefaultPlusData();
            newRepairPhotoViewDefaultPlusData.setImageResource(R.mipmap.icon_photo_add);
            newRepairPhotoViewDefaultPlusData.setSelectPhotoes(getPresenter().getChoosedPhoto());
            NewRepairPhotoViewDefaultPlusBean newRepairPhotoViewDefaultPlusBean = new NewRepairPhotoViewDefaultPlusBean(newRepairPhotoViewDefaultPlusData);
            displayBeen.add(newRepairPhotoViewDefaultPlusBean);

        } else if (3 == getPresenter().getChoosedPhoto().size()) {
            new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    Uri uri = Uri.fromFile(new File(getPresenter().getChoosedPhoto().get(i)));
                    try {
                        Bitmap bitmap = Glide.with(getPresenter().getView())
                                .load(uri)
                                .asBitmap()
                                .centerCrop()
                                .into(RuleUtils.dp2Px(70), RuleUtils.dp2Px(70))
                                .get();
                        NewRepairPhotoViewChoosedData newRepairPhotoViewChoosedData = new NewRepairPhotoViewChoosedData();
                        newRepairPhotoViewChoosedData.setChoosed(bitmap);
                        NewRepairPhotoViewChoosedBean newRepairPhotoViewChoosedBean = new NewRepairPhotoViewChoosedBean(newRepairPhotoViewChoosedData);
                        displayBeen.add(newRepairPhotoViewChoosedBean);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return displayBeen;
    }
}
