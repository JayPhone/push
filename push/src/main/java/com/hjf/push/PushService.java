package com.hjf.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.hjf.push.util.LogUtils;

import static com.hjf.push.Constants.KEY_URL;
import static com.hjf.push.PushManager.ACTION_CREATE_CONNECT;

/**
 * author JayPhone
 * description:
 * date :2019/10/10 9:38
 */
public class PushService extends Service {
    private ConnectorManager mConnectorManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mConnectorManager = ConnectorManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_CREATE_CONNECT://创建连接
                    String url = intent.getStringExtra(KEY_URL);
                    if (!TextUtils.isEmpty(url)) {
                        mConnectorManager.createConnect(url);
                    } else {
                        LogUtils.e("onStartCommand: url为空");
                    }
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
