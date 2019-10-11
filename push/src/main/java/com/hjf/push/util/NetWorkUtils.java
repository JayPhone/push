package com.hjf.push.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * author JayPhone
 * description:
 * date :2019/10/11 16:18
 */
public class NetWorkUtils {
    public static boolean hasNetWork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            LogUtils.i("不支持网络连接");
            return false;
        }
        // 获取网络状态信息
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                LogUtils.i("已连接WIFI网络");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                LogUtils.i("已连接移动网络");
            }
            return true;
        } else {
            LogUtils.i("未连接网络");
            return false;
        }
    }
}
