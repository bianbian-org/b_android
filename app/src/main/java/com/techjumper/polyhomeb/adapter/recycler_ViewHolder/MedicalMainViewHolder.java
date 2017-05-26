package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.text.TextUtils;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalMainData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MedicalMainData.class, beanName = "MedicalMainBean")
public class MedicalMainViewHolder extends BaseRecyclerViewHolder<MedicalMainData> {

    public static final int LAYOUT_ID = R.layout.item_medical_main;

    public MedicalMainViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MedicalMainData data) {
        if (data == null) return;
        setText(R.id.tv_label, getLabelByPosition(data.getPosition()));
        setText(R.id.tv_data, TextUtils.isEmpty(data.getData()) ? "———" : data.getData());
        setText(R.id.tv_unit, getUnitByPosition(data.getPosition()));
        setImageSrc(R.id.iv_icon, getIconByPosition(data.getPosition()));
    }

    private String getLabelByPosition(int position) {
        switch (position) {
            case 0:
                return getContext().getString(R.string.medical_bp);
            case 1:
                return getContext().getString(R.string.medical_bs);
            case 2:
                return getContext().getString(R.string.medical_bo);
            case 3:
                return getContext().getString(R.string.medical_ecg);
            case 4:
                return getContext().getString(R.string.medical_w);
            case 5:
                return getContext().getString(R.string.medical_activity);
            case 6:
                return getContext().getString(R.string.medical_ah);
            case 7:
                return getContext().getString(R.string.medical_hr);
            case 8:
                return getContext().getString(R.string.medical_sleep);
            case 9:
                return getContext().getString(R.string.medical_bf);
            default:
                return getContext().getString(R.string.medical_unknow);
        }
    }

    private String getUnitByPosition(int position) {
        switch (position) {
            case 0:
                return getContext().getString(R.string.medical_mmhg);
            case 1:
                return getContext().getString(R.string.medical_mol);
            case 2:
                return getContext().getString(R.string.medical_percent);
            case 3:
                return getContext().getString(R.string.medical_current_check);
            case 4:
                return getContext().getString(R.string.medical_kg);
            case 5:
                return getContext().getString(R.string.medical_step);
            case 6:
                return getContext().getString(R.string.medical_t);
            case 7:
                return getContext().getString(R.string.medical_pm);
            case 8:
                return getContext().getString(R.string.medical_mins);
            case 9:
                return getContext().getString(R.string.medical_mgdl);
            default:
                return "———";  //未知单位
        }
    }

    private int getIconByPosition(int position) {
        switch (position) {
            case 0:
                return R.mipmap.icon_medical_blood_pressure;
            case 1:
                return R.mipmap.icon_medical_blood_sugar;
            case 2:
                return R.mipmap.icon_medical_xygen;
            case 3:
                return R.mipmap.icon_medical_cardiograph;
            case 4:
                return R.mipmap.icon_medical_weight;
            case 5:
                return R.mipmap.icon_medical_activity;
            case 6:
                return R.mipmap.icon_medical_animal_heat;
            case 7:
                return R.mipmap.icon_medical_heart_rate;
            case 8:
                return R.mipmap.icon_medical_sleep;
            case 9:
                return R.mipmap.icon_medical_blood_fat;
            default:
                return R.mipmap.icon_medical_weight;
        }
    }

}
