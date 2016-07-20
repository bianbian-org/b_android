package com.techjumper.polyhome.b.home.utils;

import com.techjumper.polyhome.b.home.R;

/**
 * Created by kevin on 16/6/16.
 */
public class WeatherUtil {

    public static int getImgRes(String imgType) {
        int imgNo = Integer.valueOf(imgType);
        int resId = 0;
        if (imgNo == 0) {
            resId = R.mipmap.icon_qing_new;
        } else if (imgNo == 1) {
            resId = R.mipmap.icon_duoyun_new;
        } else if (imgNo == 2) {
            resId = R.mipmap.icon_yin_new;
        } else if (imgNo == 4) {
            resId = R.mipmap.icon_leizhengyu_new;
        } else if (imgNo == 5) {
            resId = R.mipmap.icon_leizhenyubingbao_new;
        } else if (imgNo == 6) {
            resId = R.mipmap.icon_yuxue_new;
        } else if (imgNo == 18) {
            resId = R.mipmap.icon_wuqi_new;
        } else if (imgNo == 53) {
            resId = R.mipmap.icon_wumai_new;
        } else if ((imgNo >= 29 && imgNo <= 31) || imgNo == 20) {
            resId = R.mipmap.icon_shachenbao_new;
        } else if ((imgNo >= 13 && imgNo <= 17) || (imgNo >= 26 && imgNo <= 28)) {
            resId = R.mipmap.icon_xue_new;
        } else {
            resId = R.mipmap.icon_yu_new;
        }
        return resId;
    }
}
