package com.techjumper.corelib.utils.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.system.AppUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

public class FileUtils {

    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        // return Environment.getExternalStorageState().equals("mounted");
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 获取SD卡的根目录
    public static String getSDCardBaseDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    // 分情况得到缓存目录
    public static String getCacheDir() {
        String cachePath;
        if (isSDCardMounted()) {
            File outCacheFile = Utils.appContext.getExternalCacheDir();
            if (outCacheFile != null) {
                cachePath = outCacheFile.getAbsolutePath();
            } else {
                String packageName = AppUtils.getPackageName();
                cachePath = FileUtils.getSDCardBaseDir()
                        + File.separator + "appCache"
                        + File.separator + packageName;
            }
        } else {
            cachePath = Utils.appContext.getCacheDir().getAbsolutePath();
        }
        File file = new File(cachePath);
        if (!file.exists()) file.mkdirs();
        return cachePath;
    }

    // 获取全部完整空间大小，返回MB
    public static long getPhoneSelfSize() {
        File rootFile = Environment.getRootDirectory();
        long rootSize = getBlockSize(rootFile);

        //缓存目录没有包含在root目录里面，所以要单独计算
        File cacheFile = Environment.getDownloadCacheDirectory();
        long cacheSize = getBlockSize(cacheFile);

        return (long) ((rootSize + cacheSize) / 1024.F / 1024);

    }

    //得到手机内置存储大小，返回MB
    public static long getPhoneInternalSDSize() {
        String esState = Environment.getExternalStorageState();
        //判断是否挂载，如果未挂载则直接返回0
        if (!esState.equals(Environment.MEDIA_MOUNTED)) {
            return 0;
        }
        File file = Environment.getExternalStorageDirectory();
        return (long) (getBlockSize(file) / 1024.F / 1024);
    }

    //得到外置SD卡的大小，返回MB
    public static long getPhoneOutSDSize() {
        String sdCardPath = getPhoneOutSDPath();
        long size;
        try {
            File file = new File(sdCardPath);
            size = (long) (getBlockSize(file) / 1024.F / 1024);
        } catch (Exception e) {
            size = 0;
        }
        return size;
    }

    //设备自身的空闲大小，返回MB
    public static long getPhoneSelfFreeSize() {
        File file = Environment.getRootDirectory();
        long rootFreeSize = getFreeBlockSize(file);

        file = Environment.getDownloadCacheDirectory();
        long cacheFreeSize = getFreeBlockSize(file);

        return (long) ((rootFreeSize + cacheFreeSize) / 1024.F / 1024);
    }

    //手机内置存储的空闲大小
    public static long getPhoneInternalSDFreeSize() {
        File file = Environment.getExternalStorageDirectory();
        return (long) (getFreeBlockSize(file) / 1024.F / 1024);
    }

    // 往SD卡的公有目录下保存文件
    public static boolean saveFileToSDCardPublicDir(byte[] data, String type,
                                                    String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(bos);
            }
        }
        return false;
    }

    // 往SD卡的自定义目录下保存文件
    public static boolean saveFileToSDCardCustomDir(byte[] data, String dir,
                                                    String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = new File(getSDCardBaseDir() + File.separator + dir);
            if (!file.exists()) {
                file.mkdirs();// 递归创建自定义目录
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(bos);
            }
        }
        return false;
    }

    // 往SD卡的私有Files目录下保存文件
    public static boolean saveFileToSDCardPrivateFilesDir(byte[] data,
                                                          String type, String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Utils.appContext.getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(bos);
            }
        }
        return false;
    }

    // 往SD卡的私有Cache目录下保存文件
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data,
                                                          String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Utils.appContext.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(bos);
            }
        }
        return false;
    }

    // 保存bitmap图片到SDCard的私有Cache目录
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap,
                                                            String fileName) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            // 获取私有的Cache缓存目录
            File file = Utils.appContext.getExternalCacheDir();

            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                if (!TextUtils.isEmpty(fileName)
                        && (fileName.contains(".png") || fileName
                        .contains(".PNG"))) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(bos);
            }
            return true;
        } else {
            return false;
        }
    }

    // 从SD卡获取文件
    public static byte[] loadFileFromSDCard(String fileDir) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            bis = new BufferedInputStream(
                    new FileInputStream(new File(fileDir)));
            byte[] buffer = new byte[8 * 1024];
            int c;
            while ((c = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(baos);
            closeStream(bis);
        }
        return null;
    }

    // 从SDCard中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmapFromSDCard(String filePath) {
        byte[] data = loadFileFromSDCard(filePath);
        if (data != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bm != null) {
                return bm;
            }
        }
        return null;
    }

    // 获取SD卡公有目录的路径
    public static String getSDCardPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).toString();
    }

    // 获取SD卡私有Cache目录的路径
    public static String getSDCardPrivateCacheDir() {
        String path = null;
        try {
            path = Utils.appContext.getExternalCacheDir().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    // 获取SD卡私有Files目录的路径
    public static String getSDCardPrivateFilesDir(String type) {
        String path = null;
        try {
            path = Utils.appContext.getExternalFilesDir(type).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }

    // 从sdcard中删除文件
    public static boolean removeFileFromSDCard(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                file.delete();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    //关闭流
    public static void closeStream(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static long getBlockSize(File file) {
        StatFs stat = new StatFs(file.getPath());
        long blockSize;
        long blockCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            blockCount = stat.getBlockCountLong(); //所有的block数量
        } else {
            blockSize = stat.getBlockSize();
            blockCount = stat.getBlockCount();
        }
        return blockSize * blockCount;
    }

    private static long getFreeBlockSize(File file) {
        StatFs stat = new StatFs(file.getPath());
        long blockSize;
        long blockCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            blockCount = stat.getAvailableBlocksLong(); //得到未使用的block数量
        } else {
            blockSize = stat.getBlockSize();
            blockCount = stat.getAvailableBlocks();
        }
        return blockSize * blockCount;
    }

    //得到外置SD卡的路径
    public static String getPhoneOutSDPath() {
        Map<String, String> map = System.getenv();
        if (map.containsKey("SECONDARY_STORAGE")) {
            String paths = map.get("SECONDARY_STORAGE");
            String[] pathArray = paths.split(":");
            if (pathArray.length <= 0) {
                return null;
            }
            return pathArray[0];
        }
        return null;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath       文件路径
     * @param isDeleteFolder 是否删除目录
     * @throws IOException
     */
    public static void deleteFolderFile(String filePath, boolean isDeleteFolder)
            throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), isDeleteFolder);
                }
                if (isDeleteFolder && file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                    file.delete();
                }
            } else {
                file.delete();
            }
        }
    }

    /**
     * 格式化文件大小，以B、K、M、G的格式显示
     */
    public static String formatFileSize(long size) {
        StringBuilder sb = new StringBuilder();

        //将数字格式化，""里面填写规则。 #代表小数点前。小数点后面几个0就代表保留几个小数
        DecimalFormat df = new DecimalFormat("#.00");

        if (size < 1) {  //如果是空的,显示0M
            sb.append(0);
            sb.append("M");
        } else if (size < 1024) {//显示B
            sb.append(size);
            sb.append("B");
        } else if (size < 1024 * 1024) {  //显示KB
            sb.append(df.format(size / 1024.0));
            sb.append("K");
        } else if (size < 1024.0 * 1024 * 1024) {  //显示MB
            sb.append(df.format(size / 1024.0 / 1024));
            sb.append("M");
        } else {  //显示G
            sb.append(df.format(size / 1024 / 1024.0 / 1024));
            sb.append("G");
        }
        return sb.toString();
    }

}
