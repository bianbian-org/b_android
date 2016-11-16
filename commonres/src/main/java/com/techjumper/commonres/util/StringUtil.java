package com.techjumper.commonres.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by kevin on 16/4/28.
 */
public class StringUtil {

    public static final String MAC_FILE_ADDRESS = "/sys/class/net/eth0/address";

    private static final String KEY = "jumper_polyhome_b";

    /*
     * Get the STB MacAddress
	 */
    public static String getMacAddress() {
        try {
            return loadFileAsString(MAC_FILE_ADDRESS)
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String loadFileAsString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public static String getVersion(Context context) {
        String version = "";
        PackageManager manager = context.getPackageManager();
        try {
            version = context.getPackageName() + "_" + manager.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static String encrypt(String content) {
        if (TextUtils.isEmpty(content))
            return "加密失败: 内容为空";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(KEY);
        stringBuilder.append(":");
        stringBuilder.append(content);

        return Base64.encodeToString(stringBuilder.toString().getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String encryptString) {
        if (TextUtils.isEmpty(encryptString))
            return "解码失败: 内容为空";

        String result = new String(Base64.decode(encryptString, Base64.DEFAULT));
        String key = result.substring(0, result.indexOf(":"));

        if (TextUtils.isEmpty(key) || !key.equals(KEY))
            return "解码失败: key值错误";

        return result.substring(result.indexOf(":") + 1, result.length());
    }
}
