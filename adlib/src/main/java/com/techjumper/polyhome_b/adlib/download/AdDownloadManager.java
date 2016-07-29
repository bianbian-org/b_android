package com.techjumper.polyhome_b.adlib.download;

import android.os.Environment;
import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.polyhome_b.adlib.net.RetrofitTemplate;
import com.techjumper.polyhome_b.adlib.utils.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/27
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdDownloadManager {

    private static AdDownloadManager INSTANCE;

    private static final String SP_NAME = "downloadKeyPath";
    private static final String CACHE_NAME = "polyhome_b_host" + File.separator + "h_retrofit_cache";

    private boolean mIsDownloading;
    private DiskLruCache mDiskLruCache;

    private LinkedBlockingQueue<DownloadFileEntity> mQueue = new LinkedBlockingQueue<>();

    private AdDownloadManager() {
        try {
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(CACHE_NAME)
                    , 1, 1, 300 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AdDownloadManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AdDownloadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AdDownloadManager();
                }
            }
        }
        return INSTANCE;
    }

    private File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            cachePath = Utils.appContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private void notifyDownloadFinish(IRetrofitDownload iRetrofitDownload, File file) {
        if (iRetrofitDownload != null) {
            iRetrofitDownload.onFileDownloaded(file);
        }
    }


    public void download(String key, String url, IRetrofitDownload iRetrofitDownload) {
        DownloadFileEntity downloadEntity = createDownloadEntity(key, url);
        try {
            mQueue.put(downloadEntity);
        } catch (InterruptedException e) {
            try {
                mQueue.put(downloadEntity);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        download(iRetrofitDownload);
    }

    private void download(IRetrofitDownload iRetrofitDownload) {

        if (mIsDownloading)
            return;

        while (!mQueue.isEmpty()) {
            mIsDownloading = true;
            DownloadFileEntity entity = null;
            try {
                entity = mQueue.take();
            } catch (InterruptedException e) {
                try {
                    entity = mQueue.take();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            if (entity == null || TextUtils.isEmpty(entity.url) || TextUtils.isEmpty(entity.key)) {
                notifyDownloadFinish(iRetrofitDownload, null);
                continue;
            }


            //检查本地是否存在
            DiskLruCache.Snapshot local = null;
            try {
                local = mDiskLruCache.get(entity.key);
            } catch (Exception ignored) {
            }

            if (local != null) {
//                JLog.d("<Lru> 本地已经存在,不再重复下载: " + local.getFile(0));
                notifyDownloadFinish(iRetrofitDownload, local.getFile(0));
                continue;
            }

            final DownloadFileEntity finalEntity = entity;

            RetrofitTemplate.getInstance().donwloadFile(entity.url)
                    .observeOn(Schedulers.io())
                    .map(responseBody -> {
//                        FileUtils.saveInputstreamToPath(responseBody.byteStream(), finalEntity.path, finalEntity.name);
                        DiskLruCache.Editor edit = null;

                        try {
                            edit = mDiskLruCache.edit(finalEntity.key);
                            OutputStream out = edit.newOutputStream(0);
                            FileUtils.saveInputToOutput(responseBody.byteStream(), out);
                            edit.commit();

                        } catch (Exception e) {
                            e.printStackTrace();
                            if (edit != null) {
                                try {
                                    edit.abort();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        return finalEntity;
                    })
                    .map(downloadFileEntity -> {

                        DiskLruCache.Snapshot snapshot = null;
                        try {
                            snapshot = mDiskLruCache.get(downloadFileEntity.key);
                        } catch (Exception e) {
                            throw new RuntimeException("Download " + extraNameFromUrl(downloadFileEntity.url) + " Failed");
                        }
                        if (snapshot == null) {
                            throw new RuntimeException("Download " + extraNameFromUrl(downloadFileEntity.url) + " Failed");
                        }

                        return snapshot.getFile(0);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<File>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            JLog.e("<Lru> 下载失败: " + e);
//                            if (BuildConfig.DEBUG) {
//                            }
                            notifyDownloadFinish(iRetrofitDownload, null);
                        }

                        @Override
                        public void onNext(File file) {
                            JLog.d("<Lru> 从网络下载到本地: " + file + " 成功");
//                            if (BuildConfig.DEBUG) {
//                            }
                            notifyDownloadFinish(iRetrofitDownload, file);
                        }
                    });

        }

        mIsDownloading = false;
    }

    private DownloadFileEntity createDownloadEntity(String key, String url) {
        DownloadFileEntity downloadFileEntity = new DownloadFileEntity();
        downloadFileEntity.key = key;
        downloadFileEntity.url = url;
        return downloadFileEntity;
    }


    private String extraNameFromUrl(String url) {
        if (TextUtils.isEmpty(url))
            return "unnamed";

        int last1 = url.lastIndexOf("/");
        if (last1 == 1) {
            return "unnamed";
        }
        if (last1 + 1 >= url.length()) {
            return "unnamed";
        }

        return url.substring(last1 + 1, url.length());
    }

//    private void savePathTolocal(String key, String path) {
//        if (TextUtils.isEmpty(key))
//            return;
//        getSp().edit().putString(key, path).commit();
//    }
//
//    private String getPathFromLocal(String key) {
//        if (TextUtils.isEmpty(key))
//            return "";
//        return getSp().getString(key, "");
//    }
//
//    private SharedPreferences getSp() {
//        return PreferenceUtils.getPreference(SP_NAME);
//    }

    private static class DownloadFileEntity {
        public String url;
        public String key;
    }

    public interface IRetrofitDownload {
        void onFileDownloaded(File file);
    }
}
