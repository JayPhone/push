package com.hjf.push;

import android.content.Context;
import android.content.Intent;

import static com.hjf.push.Constants.KEY_URL;

/**
 * author JayPhone
 * description:
 * date :2019/10/8 17:16
 */
public class PushManager {
    //创建连接
    public static final String ACTION_CREATE_CONNECT = "ACTION_CREATE_CONNECT";

    public static void connect(Context context, String url) {
        Intent intent = new Intent(context, PushService.class);
        intent.setAction(ACTION_CREATE_CONNECT);
        intent.putExtra(KEY_URL, url);
        context.startService(intent);
    }

    public static void sent(Context context, String message) {
        ConnectorManager.getInstance(context.getApplicationContext()).sent(message);
    }

    public static void close(Context context) {
        ConnectorManager.getInstance(context.getApplicationContext()).closeConnect();
    }
}
