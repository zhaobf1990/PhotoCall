package com.zhaobf.phonecall.utils;

import android.os.Environment;
import android.util.Log;

/**
 * 我的日志
 * 作者：zhaobf on 2016-05-03 15:36
 * # 公司:杭州天谷信息科技有限公司
 */
public class MyLog1 {
    public static String rootPath = Environment.getExternalStorageDirectory() + "/phoneCall/log/";

    public static void write(String msg) {
        String fileName = rootPath + "log" + TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE) + ".txt";
        msg = "【" + TimeUtils.getCurrentTimeInString() + "】" + "=================================" + msg + "\r\n\r\n";
        FileUtils.writeFile(fileName, msg, true);
    }

    public static void d(String tag, String msg) {
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable ta) {
            Log.e(tag, msg, ta);
    }

    public static void i(String tag, String msg) {
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
            Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
            Log.w(tag, msg);
    }




}
