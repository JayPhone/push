package com.hjf.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hjf.push.util.LogUtils;

import static com.hjf.push.Constants.KEY_MESSAGE;
import static com.hjf.push.Constants.KEY_URL;

/**
 * author JayPhone
 * description:
 * date :2019/10/8 17:16
 */
public class PushManager {
    private Context mContext;
    private static volatile PushManager instance;
    private ConnectorManager mConnectorManager;
    private ConnectorManager.ConnectConfig mConnectConfig;

    private void PushManager() {

    }

    public static PushManager getInstance() {
        if (instance == null) {
            synchronized (PushManager.class) {
                if (instance == null) {
                    instance = new PushManager();
                }
            }
        }
        return instance;
    }

    public void setConnectConfig(ConnectorManager.ConnectConfig config) {
        if (config != null) {
            if (config.getUrl() == null || TextUtils.isEmpty(config.getUrl())) {
                throw new IllegalArgumentException("ConnectConfig url不能为null");
            }
            mConnectConfig = config;
        } else {
            throw new IllegalArgumentException("ConnectConfig 不能为null");
        }
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public boolean isInit() {
        return mContext != null;
    }

    public PushManager checkInit(Context context) {
        if (!isInit()) {
            init(context);
        }
        return this;
    }

    public boolean isStarted() {
        return mConnectorManager != null;
    }

    public void startPush() {
        if (isInit()) {
            mContext.startService(new Intent(mContext, PushService.class));
        }
    }

    public void stopPush() {
        if (isInit()) {
            mContext.stopService(new Intent(mContext, PushService.class));
        }
    }

    synchronized void destroy() {
        if (mConnectorManager != null) {

        }
    }

    public void create() {
        if (mConnectConfig != null) {
            mConnectConfig.build();
        } else {
            LogUtils.i("ConnectConfig 为空");
        }
    }
}
