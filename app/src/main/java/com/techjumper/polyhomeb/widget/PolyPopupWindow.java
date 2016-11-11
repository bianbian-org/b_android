package com.techjumper.polyhomeb.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhomeb.R;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/16
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PolyPopupWindow extends PopupWindow
        implements PopupWindow.OnDismissListener, Animation.AnimationListener {

    private Activity mActivity;
    private View rootView, anchorView;
    private ListView mListView;
    private DataAdapter mAdapter;
    private View mRootView;
    private OnPopDismiss mOnPopDismiss;
    private float mMarginRight, mMarginTop = -1F;
    private Drawable mBgPic;
    private int xoff;

    public enum AnimStyle {
        LEFTANIM, RIGHTANIM, ALPHA
    }

    private ScaleAnimation leftShowAnim, rightShowAnim, leftExitAnim, rightExitAnim;
    private AlphaAnimation alphaShowAnim, alphaExitAnim;
    private ItemClickCallBack mCallBack;
    private int animStyle;

    public PolyPopupWindow(Activity activity, int animStyle, ItemClickCallBack callBack, View rootView, OnPopDismiss onPopDismiss) {
        this.mActivity = activity;
        this.mCallBack = callBack;
        this.animStyle = animStyle;
        this.mRootView = rootView;
        this.mOnPopDismiss = onPopDismiss;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mActivity).inflate(R.layout.layout_polypopupwindow, null);
        mListView = (ListView) rootView.findViewById(R.id.lv_popup_list);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOnDismissListener(this);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(animStyle);
        setOutsideTouchable(true);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            if (mCallBack != null && mAdapter != null) {
                mCallBack.callBack(position, mAdapter.getItem(position));
                this.dismiss();
            }
        });
    }

    /**
     * 获取数据
     */
    public void initData(List<String> datas) {
        mAdapter = new DataAdapter(datas);
        mListView.setAdapter(mAdapter);
    }

    public void show(AnimStyle style) {

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.root_view);
        if (mBgPic != null) {
            layout.setBackground(mBgPic);
        } else {
            layout.setBackground(ResourceUtils.getDrawableRes(R.drawable.bg_pop));
        }

        Rect frame = new Rect();
        mActivity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        if (mMarginRight == -1F || mMarginTop == -1F) {
            showAsDropDown(anchorView, -xoff, 0);
        } else {
            int marginRight = RuleUtils.dp2Px(mMarginRight);
            int marginTop = RuleUtils.dp2Px(mMarginTop);
            showAtLocation(mRootView, Gravity.RIGHT, marginRight, -marginTop);
        }
        showAnim(style);
    }

    /*********************
     * showAtLocation
     *****************/
    public void setMarginRight(float marginRight) {
        this.mMarginRight = marginRight;
    }

    public void setMarginTop(float marginTop) {
        this.mMarginTop = marginTop;
    }

    /********************
     * showAsDropDown
     *****************/
    public void setAnchorView(View anchorView) {
        this.anchorView = anchorView;
    }

    public void setXoff(int xoff) {
        this.xoff= xoff;
    }

    public void setBgPic(Drawable mBgPic) {
        this.mBgPic = mBgPic;
    }

    /**
     * 显示动画效果
     */
    private void showAnim(AnimStyle style) {
        switch (style) {
            case LEFTANIM:
                if (leftShowAnim == null) {
                    leftShowAnim = new ScaleAnimation(0f, 1f, 0f, 1f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    leftShowAnim.setDuration(250);
                    leftShowAnim.setFillAfter(true);
                }
                rootView.startAnimation(leftShowAnim);
                break;
            case RIGHTANIM:
                if (rightShowAnim == null) {
                    rightShowAnim = new ScaleAnimation(0f, 1f, 0f, 1f,
                            Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    rightShowAnim.setDuration(250);
                    rightShowAnim.setFillAfter(true);
                }
                rootView.startAnimation(rightShowAnim);
                break;
            case ALPHA:
                if (alphaShowAnim == null) {
                    alphaShowAnim = new AlphaAnimation(0, 1);
                    alphaShowAnim.setDuration(300);
                    alphaShowAnim.setFillAfter(true);
                }
                rootView.startAnimation(alphaShowAnim);
                break;
        }
    }

    /**
     * 退出动画效果
     */
    public void thisDismiss(AnimStyle style) {
        switch (style) {
            case LEFTANIM:
                if (leftExitAnim == null) {
                    leftExitAnim = new ScaleAnimation(1f, 0f, 1f, 0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    leftExitAnim.setDuration(250);
                    leftExitAnim.setFillAfter(true);
                    leftExitAnim.setAnimationListener(this);
                }
                rootView.startAnimation(leftExitAnim);
                break;

            case RIGHTANIM:
                if (rightExitAnim == null) {
                    rightExitAnim = new ScaleAnimation(1f, 0f, 1f, 0f,
                            Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    rightExitAnim.setDuration(250);
                    rightExitAnim.setFillAfter(true);
                    rightExitAnim.setAnimationListener(this);
                }
                rootView.startAnimation(rightExitAnim);
                break;
            case ALPHA:
                if (alphaExitAnim == null) {
                    alphaExitAnim = new AlphaAnimation(1, 0);
                    alphaExitAnim.setDuration(300);
                    alphaExitAnim.setFillAfter(true);
                }
                rootView.startAnimation(alphaExitAnim);
//                dismiss();
                break;
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        this.dismiss();
    }

    private void popupShowAlpha() {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 0.6f;
        window.setAttributes(params);
    }

    private void popupExitAlpha() {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1.0f;
        window.setAttributes(params);
    }

    private class DataAdapter extends BaseAdapter {

        private List<String> datas;

        public DataAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            if (datas != null) {
                return datas.size();
            }
            return 0;
        }

        @Override
        public String getItem(int position) {
            if (datas != null) {
                return datas.get(position);
            }
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_polypopupwindow, null);
                viewHolder.dataView = (TextView) convertView.findViewById(R.id.tv_list_item);
                viewHolder.divider = convertView.findViewById(R.id.divider);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.dataView.setText(datas.get(position));
            if (getCount() == 1 || getCount() == 0) {
                viewHolder.divider.setVisibility(View.GONE);
            } else {
                if (position == getCount() - 1) {
                    viewHolder.divider.setVisibility(View.GONE);
                } else {
                    viewHolder.divider.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }

        class ViewHolder {
            TextView dataView;
            View divider;
        }
    }

    public interface ItemClickCallBack {
        void callBack(int position, String s);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onDismiss() {
//        popupExitAlpha();
        if (mOnPopDismiss != null) {
            mOnPopDismiss.onDismiss();
        }
    }

    public interface OnPopDismiss {
        void onDismiss();
    }
}