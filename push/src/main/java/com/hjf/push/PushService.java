package com.hjf.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.hjf.push.util.LogUtils;

import static com.hjf.push.Constants.KEY_URL;

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
        if (!PushManager.getInstance().isStarted()) {
            PushManager.getInstance().checkInit(this).create();
        }
        if (PushManager.getInstance().isStarted()) {
            if (PushReceiver.hasNetwork(this)) {
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
