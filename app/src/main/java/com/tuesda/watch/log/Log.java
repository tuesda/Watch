package com.tuesda.watch.log;

/**
 * Created by zhanglei on 15/7/24.
 */
public class Log {
    public static final boolean DBG = true;

    public static final String TAG = "DribleWatch";

    public static void i(String s) {
        android.util.Log.i(TAG, s);
    }

    public static void e(String s) {
        android.util.Log.e(TAG, s);
    }
}
