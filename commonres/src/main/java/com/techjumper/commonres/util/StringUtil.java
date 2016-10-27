package com.techjumper.commonres.util;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by kevin on 16/4/28.
 */
public class StringUtil {

    public static final String MAC_FILE_ADDRESS = "/sys/class/net/eth0/address";

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
        StringBuffer fileData = new StringBuffer(1000);
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
}
