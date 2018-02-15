package com.zhaobf.phonecall;

import android.app.Application;

/**
 * Created by zhaobf on 2018/2/14.
 */

public class MyApplication extends UILApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //捕获程序中未处理的异常
        Thread.setDefaultUncaughtExceptionHandler(new AppException());
    }
}
