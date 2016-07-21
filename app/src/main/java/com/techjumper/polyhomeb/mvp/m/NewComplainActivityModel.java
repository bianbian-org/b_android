package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairPhotoViewChoosedData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairPhotoViewDefaultPlusData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRepairPhotoViewChoosedBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRepairPhotoViewDefaultPlusBean;
import com.techjumper.polyhomeb.mvp.p.activity.NewComplainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

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
}
