package com.techjumper.commonres.entity.event;

/**
 * 是否显示首页广告
 * Created by kevin on 16/7/8.
 */

public class AdShowEvent {

    private boolean isShow;

    public AdShowEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
