package com.techjumper.lightwidget.dim_popupwindow;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.techjumper.lightwidget.R;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HPopupWindow {

    private Activity mActivity;
    private ViewGroup mLayout;
    private View mAnchorView;
    private PopupWindow mRealWindow;
    private Dialog mDimDialog;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int mWindowWidth, mWindowHeight;
    private int mOffsetX, mOffsetY;
    private boolean mOnAnchorTop;
    private int mGravity;
    private float mDim = 0.5F;

    private HPopupWindow() {
    }

    public static HPopupWindow with(Activity activity, int layoutRes) {
        return with(activity, (ViewGroup) activity.getLayoutInflater().inflate(layoutRes, null));
    }

    public static HPopupWindow with(Activity activity, ViewGroup layout) {
        HPopupWindow mWindow = new HPopupWindow();
        mWindow.mActivity = activity;
        mWindow.mLayout = layout;
        return mWindow;
    }

    public HPopupWindow width(int px) {
        mWindowWidth = px;
        return this;
    }

    public HPopupWindow height(int px) {
        mWindowHeight = px;
        return this;
    }

    public HPopupWindow offsetX(int px) {
        mOffsetX = px;
        return this;
    }

    public HPopupWindow offsetY(int px) {
        mOffsetY = px;
        return this;
    }

    public HPopupWindow dim(float dim) {
        mDim = dim;
        return this;
    }

    public HPopupWindow gravity(int gravity) {
        mGravity = gravity;
        return this;
    }


    /**
     * 设置弹窗在某个控件的上面或下面
     * true:上面,false下面
     */
    public HPopupWindow anchor(View target, boolean onTop) {
        mAnchorView = target;
        mOnAnchorTop = onTop;
        return this;
    }

    public HPopupWindow create() {
        mWindowWidth = mWindowWidth == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : mWindowWidth;
        mWindowHeight = mWindowHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : mWindowHeight;
        mRealWindow = new PopupWindow(mLayout, mWindowWidth, mWindowHeight, true);
        mRealWindow.setTouchable(true);
        mRealWindow.setOutsideTouchable(true);
        mRealWindow.setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), (Bitmap) null));
        mRealWindow.getContentView().setFocusableInTouchMode(true);
        mRealWindow.getContentView().setFocusable(true);
        mRealWindow.setAnimationStyle(R.style.popup_anim);
//        mRealWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    close();
//                    return true;
//                }
//                return false;
//            }
//        });
        mRealWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mDimDialog != null && mDimDialog.isShowing()) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDimDialog.dismiss();
                        }
                    }, 200);
                }

            }
        });

        mDimDialog = new Dialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
        final View emptymDimDialog = LayoutInflater.from(mActivity).inflate(R.layout.empty, null);
        WindowManager.LayoutParams lp = mDimDialog.getWindow().getAttributes();
        lp.dimAmount = mDim;
        mDimDialog.getWindow().setAttributes(lp);
        mDimDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDimDialog.setContentView(emptymDimDialog);
        mDimDialog.setCanceledOnTouchOutside(true);
        mDimDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                try {
                    mRealWindow.showAtLocation(emptymDimDialog, getGravity(), calcuOffsetX(), calcuOffsetY());
                } catch (Exception ignored) {

                }
            }
        });

        return this;
    }

//    private void setLayoutParams(ViewGroup layout, int newWidth, int newHeight) {
//        ViewGroup.LayoutParams params = layout.getLayoutParams();
//        if (params == null) {
//            params = new ViewGroup.LayoutParams(newWidth, newHeight);
//        } else {
//            params.width = newWidth;
//            params.height = newHeight;
//        }
//        layout.setLayoutParams(params);
//    }

    public void show() {
        if (mDimDialog != null
                && !mDimDialog.isShowing()) {
            try {
                mDimDialog.show();
            } catch (Exception ignored) {
            }
        }
    }

    public void dismiss() {
        if (mRealWindow != null
                && mRealWindow.isShowing()) {
            try {
                mRealWindow.dismiss();
            } catch (Exception ignored) {
            }
        }
    }

    private int getGravity() {
        return mGravity == 0 ? Gravity.START | Gravity.TOP : mGravity;
    }

    private int calcuOffsetX() {
        if (mAnchorView == null) return mOffsetX;
        int width = mAnchorView.getWidth();

        if (mWindowWidth < 1)
            return getLocationInWindow(mAnchorView)[0] - width / 2 + mOffsetX;

        int diff = Math.abs(mWindowWidth - width) / 2;
        return getLocationInWindow(mAnchorView)[0] - diff + mOffsetX;
    }

    private int calcuOffsetY() {
        if (mAnchorView == null) return mOffsetY;
        int height = mAnchorView.getHeight();
        return mOnAnchorTop ? getLocationInWindow(mAnchorView)[1] + mOffsetY
                : getLocationInWindow(mAnchorView)[1] + height + mOffsetY;
    }

    private int[] getLocationInWindow(View view) {
        int[] position = new int[2];
        view.getLocationInWindow(position);
        return position;
    }

}
