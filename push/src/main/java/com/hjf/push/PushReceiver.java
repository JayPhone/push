package com.hjf.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import static com.hjf.push.Constants.KEY_MESSAGE;

/**
 * author JayPhone
 * description:
 * date :2019/10/11 17:35
 */
public abstract class PushReceiver extends BroadcastReceiver {
    public static final String ACTION_CONNECT_OPEN = "ACTION_CONNECT_OPEN";
    public static final String ACTION_RECEIVE_MESSAGE = "ACTION_RECEIVE_MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_CONNECT_OPEN://连接成功
                    onConnectOpen();
                    break;
                case ACTION_RECEIVE_MESSAGE://接收消息
                    onReceiveMessage(intent.getStringExtra(KEY_MESSAGE));
                    break;
            }
        }
    }

    public abstract void onConnectOpen();

    public abstract void onReceiveMessage(String message);
}
