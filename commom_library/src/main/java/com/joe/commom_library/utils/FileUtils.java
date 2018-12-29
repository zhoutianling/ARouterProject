package com.joe.commom_library.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Joe
 * @time 10/25/2018 2:52 PM
 */
public class FileUtils {


    /***
     * 获取根目录
     * @return
     */
    public static String getRootDirPath(Context context) {
        String cachePath;
        // /mnt/sdcard判断有无SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cachePath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/";
        } else {
            // 没有就创建到手机内存
            cachePath = context.getCacheDir() + "/" + context.getPackageName() + "/";
        }
        File file = new File(cachePath);
        if (!file.exists()) {
            // 创建文件夹
            file.mkdirs();
        }
        return cachePath;
    }


    private static void createDirs(String downloadPath) {
        File file = new File(downloadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private String getFileName(String url) {
        String filename;
        try {
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            if (((HttpURLConnection) conn).getResponseCode() == 200) {
                String file = conn.getURL().getFile();
                filename = file.substring(file.lastIndexOf('/') + 1);
                return filename;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
