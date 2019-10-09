package com.hjf.push.util;

import android.util.Log;

/**
 * author JayPhone
 * description:
 * date :2019/10/8 17:24
 */
public class LogUtils {
    public static String tag = "Log";
    public static boolean isDebug = true;

    public static void setTag(String tag) {
        LogUtils.tag = tag;
    }

    public static void v(String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (isDebug) {
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (isDebug) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (isDebug) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (isDebug) {
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (isDebug) {
            Log.e(tag, msg, tr);
        }
    }

    public static void wtf(String msg) {
        if (isDebug) {
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String msg, Throwable tr) {
        if (isDebug) {
            Log.wtf(tag, msg, tr);
        }
    }
}
