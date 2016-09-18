package com.techjumper.polyhomeb.manager;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/4/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.system.AppUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 专门用来处理assets下的插件
 */
public class PluginAssetsManager {

    private static PluginAssetsManager INSTANCE;

    private static String sAssetsPluginsPath = "plugins";
    private String mAssetsDir = "plugin_aseets";
    private String mWaitInstallDir = "plugin_wait";
    public static final String POSTFIX_PACKAGE = "apk";

    private PluginAssetsManager() {
    }

    public static PluginAssetsManager getInstance() {
        if (INSTANCE == null) {
            synchronized (PluginAssetsManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PluginAssetsManager();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<CopyEntity> copyAssetsPluginToInstallDir() {
        return Observable
                .create((Observable.OnSubscribe<List<String>>) subscriber -> {
                    try {
                        initPluginDir();
                        initDir(getPluginBaseDir(), mAssetsDir);
                        String[] list = Utils.appContext.getAssets().list(sAssetsPluginsPath);
                        subscriber.onNext(Arrays.asList(list));

                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                    subscriber.onCompleted();
                })
                .flatMap(Observable::from)
                .map(name -> {
                    CopyEntity copyEntity = new CopyEntity("");
                    if (!POSTFIX_PACKAGE.equals(FileUtils.fetchFilePostfix(name)))
                        return copyEntity;
                    String targetAssetsPath;
                    try {
                        targetAssetsPath = getPluginBaseDir()
                                + File.separator + mAssetsDir;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return copyEntity;
                    }
                    File targetFile = new File(targetAssetsPath + File.separator + name);

                    deletePreviousVersion(name, targetFile.getParent());
                    if (!targetFile.exists()) {
                        FileUtils.saveAssetsFileToPath(sAssetsPluginsPath, name, targetFile.getParent());
                    }

                    if (!targetFile.exists()) return copyEntity;
                    copyEntity.setPath(targetFile.getAbsolutePath());
//                    try {
//                        if (!AppUtils.hasUpdate(targetFile.getAbsolutePath())) {
//                            return copyEntity;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return copyEntity;
//                    }
                    return copyEntity;
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 删除之前的版本
     */
    private void deletePreviousVersion(String apkName, String path) {
        int lastIndex = apkName.lastIndexOf("_");
        if (lastIndex == -1 || lastIndex == 0) return;
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] fileList = dir.listFiles();
        if (fileList == null) return;

        String preName = apkName.substring(0, lastIndex);
        for (File file : fileList) {
            if (file == null || !file.exists() || file.getName().equalsIgnoreCase(apkName))
                continue;
            if (file.getName().contains(preName)) {
                file.delete();
            }
        }

    }

    public void initPluginDir() throws IOException {
        initPluginDir(getPluginBaseDir());
    }

    public void initPluginDir(String pluginRootDir) throws IOException {
        initDir(pluginRootDir, mWaitInstallDir);
    }

    public void initDir(String pluginRootDir, String dirName) throws IOException {
        File file = new File(pluginRootDir + File.separator + dirName);
        if (!file.exists()) {
            if (!file.mkdirs()) throw new IOException("无法创建目录 " + file.getAbsolutePath());
        } else if (!file.isDirectory()) {
            if (file.delete()) {
                initDir(pluginRootDir, dirName);
            }
        }
    }

    public String getPluginWaitInstallDir() throws IOException {
        return getPluginBaseDir() + File.separator + mWaitInstallDir;
    }

    public String getPluginBaseDir() throws IOException {
        String rootDir = FileUtils.getSDCardBaseDir();
        if (TextUtils.isEmpty(rootDir))
            throw new IOException("机器没有存储设备或者没有权限");
        return rootDir + File.separator + AppUtils.getPackageName();
    }

    public static class CopyEntity {
        private String path;

        public CopyEntity() {
        }

        public CopyEntity(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
