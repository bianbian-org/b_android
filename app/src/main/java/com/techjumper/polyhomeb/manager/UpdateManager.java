package com.techjumper.polyhomeb.manager;

import android.os.Environment;

import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.polyhomeb.Config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UpdateManager {

    public static void writeFile2Disk(ResponseBody responseBody, ICurrentProgress iCurrentProgress) {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + Config.sParentDirName + File.separator
                + Config.sAPKDirName;

        if (!FileUtils.isSDCardMounted()) {
            return;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long currentLength = 0;

        BufferedInputStream bis = null;
        FileOutputStream fileOutputStream = null;

        File apkFile = new File(path, "temp.apk");
        long totalLength = responseBody.contentLength();

        try {
            bis = new BufferedInputStream(responseBody.byteStream());
            fileOutputStream = new FileOutputStream(apkFile);
            int len;
            byte[] buffer = new byte[1024 * 4];
            while ((len = bis.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
                currentLength += len;
                JLog.e("当前进度:" + currentLength);
                if (iCurrentProgress != null) {
                    iCurrentProgress.progressDatas(String.valueOf(currentLength * 100 / totalLength) + "%");
                }
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            apkFile.delete();
            e.printStackTrace();
            if (iCurrentProgress != null) {
                iCurrentProgress.onDownloadError();
            }
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (iCurrentProgress != null) {
                    iCurrentProgress.onDownloadError();
                }
            }
        }
    }

    public interface ICurrentProgress {
        void progressDatas(String percent);

        void onDownloadError();
    }
}
