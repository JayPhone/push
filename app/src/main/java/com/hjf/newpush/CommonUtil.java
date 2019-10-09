package com.hjf.newpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * author JayPhone
 * description:
 * date :2019/3/28 14:33
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";

    /**
     * 获取设备唯一标识
     *
     * @param context context
     * @return 设备唯一标识
     */
    public static String getDeviceId(Context context) {
        //生成规则：D0 + 设备编号
        //D是固定前缀字母，0代表身份核验终端设备类型，后面是设备编号
        return "D20" + getAndroidId(context);
    }

    /**
     * 获取androidId
     *
     * @param context context
     * @return androidId
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @param context
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取版本名称
     *
     * @param context
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 安装APK
     */
    public static void installApk(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (uri != null) {
            Log.d(TAG, uri.toString());
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            if ((Build.VERSION.SDK_INT >= 24)) {//判读版本是否在7.0以上
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (install.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(install);
            } else {
//                ViewUtil.showShortToast(context, "下载完成，请点击下拉列表的通知手动安装");
            }
        } else {
            Log.e(TAG, "download error");
        }
    }

}
