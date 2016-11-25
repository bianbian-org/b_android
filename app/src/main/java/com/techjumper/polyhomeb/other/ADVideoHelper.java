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

import com.bumptech.glide.Glide;
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

    private Context context;
    private ImageView imageView;
    private TextureView textureView;
    private ADEntity.DataBean.AdInfosBean data;
    private String videoFilePath = getExternalStorageDirectory().getAbsolutePath() + File.separator
            + Config.sParentDirName + File.separator + Config.sADVideoDirName;
    private String videoName;

    private MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private String fullPath;

    public ADVideoHelper(Context context, ADEntity.DataBean.AdInfosBean data, ImageView imageView
            , TextureView textureView) {
        this.context = context;
        this.data = data;
        this.imageView = imageView;
        this.textureView = textureView;
        init();
    }

    private void init() {
        textureView.setSurfaceTextureListener(this);
    }

    public void startWork() {
        getVideoName();
    }

    public void stopWork() {

    }

    private void getVideoName() {
        // http://www.baidu.com/google/cn/video.mp4
        String media_url = data.getMedia_url();
        if (TextUtils.isEmpty(media_url) || !media_url.contains("/")) {
            showPlaceHolderPic();
            return;
        }
        String[] split = media_url.split("/");
        if (split.length <= 0) {
            showPlaceHolderPic();
            return;
        }
        videoName = split[split.length - 1];
        boolean exists = checkVideoExists();
        if (exists && getVideoSizeOnSD() > 0) {
            fullPath = "file://" + videoFilePath + File.separator + videoName;
            showVideo();
            return;
        }
        showPlaceHolderPic();
        new SimpleDownloadBuilder()
                .setListener(null, this, this)
                .setName(videoName)
                .setNotifyPercent(2)
                .setPath(videoFilePath)
                .setUrl(Config.sHost + data.getMedia_url())
                .download();
    }

    private boolean checkVideoExists() {
        return FileUtils.isFileExist(createFilePath() + File.separator + videoName);
    }

    private long getVideoSizeOnSD() {
        File f = new File(createFilePath() + File.separator + videoName);
        if (f.exists() && f.isFile())
            return f.length();
        else
            return 0;
    }

    private File createFilePath() {
        File videoFolder = new File(videoFilePath);
        if (!videoFolder.exists()) {
            try {
                videoFolder.mkdirs();
            } catch (Exception e) {
            }
        }
        return videoFolder;
    }

    private void showPlaceHolderPic() {
        if (imageView != null) {
            if (textureView != null) {
                textureView.setVisibility(View.GONE);
            }
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.mipmap.ad_place_holder).into(imageView);
        }
    }

    private void showVideo() {
        if (textureView != null) {
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }
            textureView.setVisibility(View.VISIBLE);
            play();
        }
    }

    private void play() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.start();
    }

    @Override
    public void onDownloadError(Throwable e) {
        showPlaceHolderPic();
    }

    @Override
    public void onDownloadStateChange(State state, File downloadFile) {
        switch (state) {
            case FINISH:
                fullPath = "file://" + downloadFile.getAbsolutePath();
                showVideo();
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
        textureView = null;
        mMediaPlayer.stop();
        mMediaPlayer.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setDataSource(fullPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(mp -> {
                showVideo();
            });
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
