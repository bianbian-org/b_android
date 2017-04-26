package com.techjumper.polyhomeb.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.lightwidget.ratio.RatioFrameLayout;
import com.techjumper.polyhomeb.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PolyTab extends RatioFrameLayout {

    @Bind({R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4, R.id.tab_5})
    List<PolyTabView> mPolyViewList;

    private int mCurrIndex;
    private ITabClick iTabClick;

    public PolyTab(Context context) {
        super(context);
    }

    public PolyTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolyTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PolyTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean init(AttributeSet attrs) {
        if (!super.init(attrs)) return false;

        View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_tab, null);
        ButterKnife.bind(this, viewInflate);
        addView(viewInflate);
        return true;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

    private PolyTabView getCurrItem() {
        return getItemByIndex(mCurrIndex);
    }

    private PolyTabView getItemByIndex(int position) {
        return mPolyViewList.get(position);
    }

    public void check(int index) {
        check(index, true);
    }

    public void check(int index, boolean notify) {
        if (index != 2) {
            mCurrIndex = index;
        }
        if (notify && iTabClick != null) iTabClick.onTabClick(index);
        changeTextColorAndIcon(index);
    }

    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4, R.id.tab_5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_1:
                if (mCurrIndex == 0)
                    break;
                check(0);
                break;
            case R.id.tab_2:
                if (mCurrIndex == 1)
                    break;
                check(1);
                break;
            case R.id.tab_3:
                if (mCurrIndex == 2)
                    break;
                check(2);
                break;
            case R.id.tab_4:
                if (mCurrIndex == 3)
                    break;
                check(3);
                break;
            case R.id.tab_5:
                if (mCurrIndex == 4)
                    break;
                check(4);
                break;
        }
    }

    public int getCurrIndex() {
        return mCurrIndex;
    }

    public void setOnTabClickListener(ITabClick iTabClick) {
        this.iTabClick = iTabClick;
    }

    private void changeTextColorAndIcon(int currIndex) {
        if (currIndex != 2) {
            mCurrIndex = currIndex;
        }
        changeIcon(currIndex);
        changeTextColor(currIndex);
    }

    private void changeIcon(int currIndex) {
        switch (currIndex) {
            case 0:
                getCurrItem().setIcon(R.mipmap.icon_home_choose);
                mPolyViewList.get(1).setIcon(R.mipmap.icon_friend);
                mPolyViewList.get(2).setIcon(R.mipmap.icon_lvdi);
                mPolyViewList.get(3).setIcon(R.mipmap.icon_shopping);
                mPolyViewList.get(4).setIcon(R.mipmap.icon_service);
                break;
            case 1:
                getCurrItem().setIcon(R.mipmap.icon_friend_choose);
                mPolyViewList.get(0).setIcon(R.mipmap.icon_home);
                mPolyViewList.get(2).setIcon(R.mipmap.icon_lvdi);
                mPolyViewList.get(3).setIcon(R.mipmap.icon_shopping);
                mPolyViewList.get(4).setIcon(R.mipmap.icon_service);
                break;
            case 2://绿地图标不换(小艾)
//                getCurrItem().setIcon(R.mipmap.icon_lvdi_choose);
//                mPolyViewList.get(0).setIcon(R.mipmap.icon_home);
//                mPolyViewList.get(1).setIcon(R.mipmap.icon_friend);
//                mPolyViewList.get(3).setIcon(R.mipmap.icon_shopping);
//                mPolyViewList.get(4).setIcon(R.mipmap.icon_service);
                break;
            case 3:
                getCurrItem().setIcon(R.mipmap.icon_shopping_choose);
                mPolyViewList.get(0).setIcon(R.mipmap.icon_home);
                mPolyViewList.get(1).setIcon(R.mipmap.icon_friend);
                mPolyViewList.get(2).setIcon(R.mipmap.icon_lvdi);
                mPolyViewList.get(4).setIcon(R.mipmap.icon_service);
                break;
            case 4:
                getCurrItem().setIcon(R.mipmap.icon_service_choose);
                mPolyViewList.get(0).setIcon(R.mipmap.icon_home);
                mPolyViewList.get(1).setIcon(R.mipmap.icon_friend);
                mPolyViewList.get(2).setIcon(R.mipmap.icon_lvdi);
                mPolyViewList.get(3).setIcon(R.mipmap.icon_shopping);
                break;
        }
    }

    private void changeTextColor(int currIndex) {
        if (currIndex == 2) return;//绿地颜色不变(小艾)
        mPolyViewList.get(currIndex).setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
        for (int i = 0; i < mPolyViewList.size(); i++) {
            if (i == currIndex) continue;
            mPolyViewList.get(i).setTextColor(ResourceUtils.getColorResource(R.color.color_acacac));
        }
    }

    public interface ITabClick {
        void onTabClick(int index);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SaveState ss = new SaveState(parcelable);
        ss.mCurrIndex = this.mCurrIndex;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SaveState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SaveState ss = (SaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mCurrIndex = ss.mCurrIndex;
        post(() -> {
            changeTextColorAndIcon(mCurrIndex);
            postInvalidate();
        });

    }

    private static class SaveState extends BaseSavedState {

        int mCurrIndex;

        public SaveState(Parcel source) {
            super(source);
            this.mCurrIndex = source.readInt();

        }

        public SaveState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mCurrIndex);
        }

        public static final Parcelable.Creator<SaveState> CREATOR =
                new Parcelable.Creator<SaveState>() {
                    public SaveState createFromParcel(Parcel in) {
                        return new SaveState(in);
                    }

                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };
    }

}

