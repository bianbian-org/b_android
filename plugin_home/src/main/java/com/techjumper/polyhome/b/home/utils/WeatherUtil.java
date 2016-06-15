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
            resId = R.mipmap.icon_qing;
        } else if (imgNo == 1) {
            resId = R.mipmap.icon_duoyun;
        } else if (imgNo == 2) {
            resId = R.mipmap.icon_yin;
        } else if (imgNo == 4) {
            resId = R.mipmap.icon_leizhengyu;
        } else if (imgNo == 5) {
            resId = R.mipmap.icon_leizhenyubingbao;
        } else if (imgNo == 6) {
            resId = R.mipmap.icon_yuxue;
        } else if (imgNo == 18) {
            resId = R.mipmap.icon_wuqi;
        } else if (imgNo == 53) {
            resId = R.mipmap.icon_wumai;
        } else if ((imgNo >= 29 && imgNo <= 31) || imgNo == 20) {
            resId = R.mipmap.icon_shachenbao;
        } else if ((imgNo >= 13 && imgNo <= 17) || (imgNo >= 26 && imgNo <= 28)) {
            resId = R.mipmap.icon_xue;
        } else {
            resId = R.mipmap.icon_yu;
        }
        return resId;
    }
}
