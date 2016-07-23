package com.techjumper.polyhomeb.mvp.p.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.mvp.m.PropertyDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewComplainActivity;
import com.techjumper.polyhomeb.mvp.v.activity.NewRepairActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyDetailActivityPresenter extends AppBaseActivityPresenter<PropertyDetailActivity> {

    private PropertyDetailActivityModel mModel = new PropertyDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        processIME();
    }

    public int comeFromWitchButton() {
        return mModel.comeFromWitchButton();
    }

    public void onTitleRightClick() {

        switch (getView().getViewPager().getCurrentItem()) {
            case 1:
                new AcHelper.Builder(getView()).target(NewRepairActivity.class).start();
                break;
            case 2:
                new AcHelper.Builder(getView()).target(NewComplainActivity.class).start();
                break;
        }
    }

    /**
     * 判断键盘是不是弹出的,是的话就隐藏起来,不是就不管
     */
    private void processIME() {
        final View decorView = getView().getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            int displayHight = rect.bottom - rect.top;
            int hight = decorView.getHeight();
            boolean visible = (double) displayHight / hight < 0.8;
            if (visible) {
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getView().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getView().getWindow().getDecorView().getWindowToken(), 0);
                }
            } else {
                //并没什么可做的
            }
        });
    }
}
