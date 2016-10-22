package com.techjumper.polyhome.slide2unlock.interfaces;

import com.techjumper.polyhome.slide2unlock.view.Slide2UnlockView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IUnLockViewState {
    /**
     * 开始解锁
     */
    void startUnLock(Slide2UnlockView view);

    /**
     * 正在解锁中
     */
    void unLocking(Slide2UnlockView view);

}
