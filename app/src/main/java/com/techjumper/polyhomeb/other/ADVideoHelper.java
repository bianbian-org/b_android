package com.techjumper.polyhomeb.other;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.lib2.downloader.SimpleDownloadBuilder;
import com.techjumper.lib2.downloader.listener.IDownloadError;
import com.techjumper.lib2.downloader.listener.IDownloadState;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.ADEntity;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ADVideoHelper implements IDownloadError, IDownloadState
        , TextureView.SurfaceTextureListener {

    private Context mContext;
    /**
     * 视频界面上，展示占位图的IV
     */
    private ImageView mIvPlaceHolder;
    private TextureView mTextureView;
    /**
     * 视频界面上，显示在视频上的蒙版iv，包含一个播放按钮
     */
    private ImageView mIvPlayView;
    private ADEntity.DataBean.AdInfosBean mData;
    private String mVideoFilePath = getExternalStorageDirectory().getAbsolutePath() + File.separator
            + Config.sParentDirName + File.separator + Config.sADVideoDirName;
    private String mVideoName;

    private MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private String mFullPath;
    /**
     * mediaPlayer是否已经准备好
     */
    private boolean mIsPrepare = false;

    public ADVideoHelper(Context context, ADEntity.DataBean.AdInfosBean data, View rootView) {
        this.mContext = context;
        this.mData = data;
        this.mTextureView = (TextureView) rootView.findViewById(R.id.ad_video);
        this.mIvPlayView = (ImageView) rootView.findViewById(R.id.iv_play);
        this.mIvPlaceHolder = (ImageView) rootView.findViewById(R.id.iv_place_holder);
        this.mTextureView.setSurfaceTextureListener(this);
    }

    /**
     * 一切准备就绪，开始视频item的渲染工作和数据的判断，初始化工作.
     */
    public void startWork() {
        getVideoName();
    }

    /**
     * 停止播放视频，将iv播放按钮显示出来.
     */
    public void stopWork() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            stopVideo();
        }
    }

    /**
     * 从服务器下发的数据中获取视频文件名字，下载url链接
     */
    private void getVideoName() {
        String media_url = mData.getMedia_url();
        if (TextUtils.isEmpty(media_url) || !media_url.contains("/")) {
            showPlaceHolderPic();
            return;
        }
        String[] split = media_url.split("/");
        if (split.length <= 0) {
            showPlaceHolderPic();
            return;
        }
        mVideoName = split[split.length - 1];
        boolean exists = checkVideoExists();
        if (exists && getVideoSizeOnSD() > 0) {
            mFullPath = "file://" + mVideoFilePath + File.separator + mVideoName;
            showVideoView();
            return;
        }
        showPlaceHolderPic();
        new SimpleDownloadBuilder()
                .setListener(null, this, this)
                .setName(mVideoName)
                .setNotifyPercent(2)
                .setPath(mVideoFilePath)
                .setUrl(Config.sHost + mData.getMedia_url())
                .download();
    }

    /**
     * 检查指定名字的视频文件是否已经存在于SD卡
     */
    private boolean checkVideoExists() {
        return FileUtils.isFileExist(createFilePath() + File.separator + mVideoName);
    }

    /**
     * 获取指定名字的视频文件大小
     */
    private long getVideoSizeOnSD() {
        File f = new File(createFilePath() + File.separator + mVideoName);
        if (f.exists() && f.isFile())
            return f.length();
        else
            return 0;
    }

    /**
     * 创建视频下载的文件夹
     */
    private File createFilePath() {
        File videoFolder = new File(mVideoFilePath);
        if (!videoFolder.exists()) try {
            videoFolder.mkdirs();
        } catch (Exception ignored) {
        }
        return videoFolder;
    }

    /**
     * 视频正在加载中，正在下载中，下载失败，服务器返回的视频连接有误，那么直接展示占位图就是了
     */
    private void showPlaceHolderPic() {
        if (mIvPlaceHolder != null) {
            mIvPlaceHolder.setVisibility(View.VISIBLE);
        }
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.GONE);
        }
        if (mTextureView != null) {
            mTextureView.setVisibility(View.GONE);
        }
    }

    /**
     * 视频文件下载完毕，或者视频文件已经存在，可以准备开始播放
     */
    private void showVideoView() {
        if (mTextureView != null) {
            mTextureView.setVisibility(View.VISIBLE);
        }
        if (mIvPlaceHolder != null) {
            mIvPlaceHolder.setVisibility(View.GONE);
        }
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 控制播放视频时的界面隐藏与显示
     */
    private void playVideo() {
        showVideoView();
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.GONE);
        }
    }

    /**
     * 控制停止视频时的界面显示与隐藏
     */
    private void stopVideo() {
        if (mIvPlaceHolder != null) {
            mIvPlaceHolder.setVisibility(View.GONE);
        }
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.VISIBLE);
        }
        if (mTextureView != null) {
            mTextureView.setVisibility(View.VISIBLE);
        }
    }

    private void showFirstFrame() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.seekTo(0);
    }

    @Override
    public void onDownloadError(Throwable e) {
        showPlaceHolderPic();
    }

    @Override
    public void onDownloadStateChange(State state, File downloadFile) {
        switch (state) {
            case FINISH:
                mFullPath = "file://" + downloadFile.getAbsolutePath();
                showVideoView();
                break;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        initMediaPlayer();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurface = null;
        mTextureView = null;
        mMediaPlayer.stop();
        mMediaPlayer.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 初始化mediaPlayer
     */
    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        try {
//            mMediaPlayer.setVolume(0, 0);//静音
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setDataSource(mFullPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(mp -> {
                mIsPrepare = true;
                showFirstFrame();
            });
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击item之后的逻辑
     */
    public void onClick(View v) {
        if (!mIsPrepare) return;
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            stopVideo();
            mMediaPlayer.pause();
            return;
        }
        playVideo();
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
    }
}
