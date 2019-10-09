package com.hjf.push;

import android.content.Context;

/**
 * author JayPhone
 * description:
 * date :2019/10/8 17:16
 */
public class PushManager {

    public static void connect(Context context, String url) {
        ConnectorManager.getInstance(context).createConnect(url);
    }

    public static void sent(Context context, String message) {
        ConnectorManager.getInstance(context).sent(message);
    }

    public static void close(Context context) {
        ConnectorManager.getInstance(context).closeConnect();
    }
}
