package com.techjumper.polyhome_b.adlib.window;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.RequestCreator;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.widget.MyVideoView;

import java.io.File;

public class AdWindowManager {

    private final WindowManager mWindowManager;
    private FrameLayout mContainer;
    private WindowManager.LayoutParams mContainerParams;
    private ImageView mImageView;
    private MyTextureView myVideoView;
    private boolean mIsAttach;
    private IAdWindow iAdWindow;

    private ParentClickListener mParentClickListener;

    private AdWindowManager() {
        mWindowManager = (WindowManager) Utils.appContext.getSystemService(Context.WINDOW_SERVICE);


        mContainer = new FrameLayout(Utils.appContext);
        mContainer.setBackgroundColor(0xFF000000);
        mContainerParams = new WindowManager.LayoutParams();
        mContainerParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mContainerParams.format = PixelFormat.RGBA_8888;
        mContainerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        mContainerParams.gravity = Gravity.START | Gravity.TOP;
        mContainerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mContainerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mContainerParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        mContainerParams.x = 0;
//        mContainerParams.y = 0;

        //添加ImageView
        mImageView = new ImageView(Utils.appContext);
        FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT);
        mContainer.addView(mImageView, imageLayoutParams);

        //添加VideoView
        myVideoView = new MyTextureView(Utils.appContext);
        FrameLayout.LayoutParams videoLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT);
        mContainer.addView(myVideoView, videoLayoutParams);
    }

    public void setOnAdsClickListener(ParentClickListener listener) {
        mParentClickListener = listener;
        mContainer.setOnClickListener(mParentClickListener);
    }

    public void unregisterClickListener() {
        mContainer.setOnClickListener(null);
        mParentClickListener = null;
    }

    public void setOnWindowShowListener(IAdWindow iAdWindow) {
        this.iAdWindow = iAdWindow;
    }

    public void unregisterWindowShowListener() {
        iAdWindow = null;
    }

    public void showImage(AdEntity.AdsEntity adsEntity, File file) {
        show(mImageView);
        if (mParentClickListener != null) {
            mParentClickListener.update(adsEntity, file);
        }

        showWindow();
        RequestCreator requestCreator;
        if (file != null && file.exists()) {
            requestCreator = PicassoHelper.load(file);
        } else {
            JLog.d("本地图片不存在, 从网络上加载: " + adsEntity.getMedia_url());
            requestCreator = PicassoHelper.load(adsEntity.getMedia_url());
        }
        requestCreator
                .config(Bitmap.Config.RGB_565)
                .noPlaceholder()
                .noFade()
                .into(mImageView);
    }

    public void showVideo(AdEntity.AdsEntity adsEntity, File file) {
        show(myVideoView);
        if (mParentClickListener != null) {
            mParentClickListener.update(adsEntity, file);
        }
        showWindow();

        myVideoView.setOnStateChangeListener(new MyTextureView.OnStateChangeListener() {
            @Override
            public void onSurfaceTextureDestroyed(SurfaceTexture surface) {

            }

            @Override
            public void onBuffering() {

            }

            @Override
            public void onPlaying() {

            }

            @Override
            public void onStart() {
                if (file != null && file.exists()) {
                    myVideoView.play(file.getAbsolutePath());
                } else {
                    myVideoView.play(adsEntity.getMedia_url());
                }
            }

            @Override
            public void onSeek(int max, int progress) {

            }

            @Override
            public void onStop() {

            }
        });
    }

    private void showWindow() {
        if (mIsAttach) return;
        try {
            mWindowManager.addView(mContainer, mContainerParams);
            mIsAttach = true;
            if (iAdWindow != null) {
                iAdWindow.onAdWindowShow();
            }
        } catch (Exception ignored) {
        }
    }

    public void closeWindow(boolean byUser) {
        try {
            mImageView.setImageBitmap(null);
            if (myVideoView != null) {
                myVideoView.stop();
            }
            mWindowManager.removeView(mContainer);
            mIsAttach = false;
            if (iAdWindow != null) {
                iAdWindow.onAdWindowClose(byUser);
            }
        } catch (Exception ignored) {
        }
    }

    private void show(View inView) {
        try {
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                View childView = mContainer.getChildAt(i);
                if (childView == inView) {
                    if (inView.getVisibility() != View.VISIBLE) {
                        inView.setVisibility(View.VISIBLE);
                    }
                } else {
                    childView.setVisibility(View.GONE);
                }
            }
        } catch (Exception ignored) {
        }
    }


    private static class SingletonInstance {
        private static final AdWindowManager INSTANCE = new AdWindowManager();
    }

    public static AdWindowManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public static abstract class ParentClickListener implements View.OnClickListener {

        private AdEntity.AdsEntity mAdsEntity;
        private File mFile;

        public void update(AdEntity.AdsEntity adsEntity, File file) {
            mAdsEntity = adsEntity;
            mFile = file;
        }

        @Override
        public void onClick(View v) {
            onAdsClick(mAdsEntity, mFile);
        }

        public abstract void onAdsClick(AdEntity.AdsEntity adsEntity, File file);
    }

    public interface IAdWindow {
        void onAdWindowShow();

        void onAdWindowClose(boolean byUser);
    }

}