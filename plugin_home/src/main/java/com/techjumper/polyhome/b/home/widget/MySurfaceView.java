package com.techjumper.polyhome.b.home.widget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by kevin on 16/7/28.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Context context;
    private MediaPlayer mediaPlayer;
    private MediaState mediaState;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public interface OnStateChangeListener {
        public void onSurfaceTextureDestroyed(SurfaceHolder holder);

        public void onBuffering();

        public void onPlaying();

        public void onStart();

        public void onSeek(int max, int progress);

        public void onStop();
    }

    OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    //监听视频的缓冲状态
    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            if (onStateChangeListener != null) {
                onStateChangeListener.onPlaying();
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    onStateChangeListener.onBuffering();
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    onStateChangeListener.onPlaying();
                }
            }
            return false;
        }
    };

    //视频缓冲进度更新
    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (onStateChangeListener != null) {
                if (mediaState == MediaState.PLAYING) {
                    onStateChangeListener.onSeek(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
                }
            }
        }
    };

    public void init() {
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    mediaState = MediaState.PLAYING;
                }
            });
            mediaPlayer.setOnInfoListener(onInfoListener);
            mediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
        }

        mediaPlayer.setDisplay(holder);
        mediaState = MediaState.INIT;

        if (onStateChangeListener != null) {
            onStateChangeListener.onStart();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (onStateChangeListener != null) {
            onStateChangeListener.onSurfaceTextureDestroyed(holder);
        }
    }

    //停止播放
    public void stop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mediaState == MediaState.INIT) {
                        return;
                    }
                    if (mediaState == MediaState.PREPARING) {
                        mediaPlayer.reset();
                        mediaState = MediaState.INIT;
                        return;
                    }
                    if (mediaState == MediaState.PAUSE) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaState = MediaState.INIT;
                        return;
                    }
                    if (mediaState == MediaState.PLAYING) {
                        mediaPlayer.pause();
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaState = MediaState.INIT;
                        return;
                    }
                } catch (Exception e) {
                    mediaPlayer.reset();
                    mediaState = MediaState.INIT;
                } finally {
                    if (onStateChangeListener != null) {
                        onStateChangeListener.onStop();
                    }
                }
            }
        }).start();
    }

    public void play(String videoUrl) {
        if (mediaState == MediaState.PREPARING) {
            stop();
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.setLooping(true);
            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(videoUrl);
                mediaPlayer.setDisplay(getHolder());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
        }
        mediaState = MediaState.PREPARING;
    }

    //暂停播放
    public void pause() {
        mediaPlayer.pause();
        mediaState = MediaState.PAUSE;
    }

    //播放视频
    public void start() {
        mediaPlayer.setDisplay(getHolder());
        mediaPlayer.start();
        mediaState = MediaState.PLAYING;
    }

    //状态（初始化、正在准备、正在播放、暂停、释放）
    public enum MediaState {
        INIT, PREPARING, PLAYING, PAUSE, RELEASE;
    }

    //获取播放状态
    public MediaState getState() {
        return mediaState;
    }
}
