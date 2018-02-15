package com.zhaobf.phonecall;

import android.util.Log;

import com.zhaobf.phonecall.utils.MyLog1;


/**
 * 作者：zhaobf on 2016-06-04 13:02
 * # 公司:杭州天谷信息科技有限公司
 */
public class AppException implements Thread.UncaughtExceptionHandler {
    /**
     * The thread is being terminated by an uncaught exception. Further
     * exceptions thrown in this method are prevent the remainder of the
     * method from executing, but are otherwise ignored.
     *
     * @param thread the thread that has an uncaught exception
     * @param ex     the exception that was thrown
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d("zhaobf", ex.toString());

        MyLog1.write(ex.toString());
        MyLog1.write("======================================================================================================================================");
        MyLog1.write(ex.getMessage());
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            MyLog1.write(stackTraceElements[i].toString());
        }
        MyLog1.e("uncaughtException", ex.toString(), ex);

    }
}
