package com.hjf.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hjf.push.util.LogUtils;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import static com.hjf.push.PushReceiver.ACTION_CONNECT_OPEN;

/**
 * author JayPhone
 * description: WebSocket连接管理类
 * date :2019/10/8 17:18
 */
public class ConnectorManager {
    //单例连接管理器
    private volatile static ConnectorManager instance;
    //WebSocket客户端
    private JWebSocketClient mClient;
    private Context mContext;
    //连接配置器
    private ConnectConfig mConnectConfig;
    private BroadcastReceiver mReceiver;
    private boolean mIsReconnecting = false;

    private ConnectorManager(Context context) {
        mContext = context;
    }

    private ConnectorManager(Context context, ConnectConfig connectConfig) {
        mContext = context;
        mConnectConfig = connectConfig;
    }

    static ConnectorManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ConnectorManager.class) {
                if (instance == null) {
                    instance = new ConnectorManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private static ConnectorManager getInstance(Context context, ConnectConfig connectConfig) {
        if (instance == null) {
            synchronized (ConnectorManager.class) {
                if (instance == null) {
                    instance = new ConnectorManager(context.getApplicationContext(), connectConfig);
                }
            }
        }
        return instance;
    }

    //重连Handler
    private Handler mHandler = new Handler();
    private Runnable mReConnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsReconnecting) {
                mIsReconnecting = true;
                if (mClient != null && mClient.isClosed()) {
                    LogUtils.i("WebSocketClient不为空，开始重连");
                    mClient.reconnect();
                } else {
                    LogUtils.i("WebSocketClient为空，重新创建");
                    createConnect();
                }
            }
        }
    };

    void createConnect() {
        if (TextUtils.isEmpty(mConnectConfig.url)) {
            LogUtils.e("url为空");
            return;
        }
        URI uri = URI.create(mConnectConfig.url);
        if (!isConnected()) {
            mClient = new JWebSocketClient(uri, mConnectConfig.heartBeatInterval) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    super.onOpen(handshakedata);
//                    registerNetWorkStateReceiver(mContext);
                    mIsReconnecting = false;
                    onConnectOpen();
                }

                @Override
                public void onError(Exception ex) {
                    super.onError(ex);
                    mIsReconnecting = false;
                    mHandler.postDelayed(mReConnectRunnable, mConnectConfig.reconnectInterval);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                    mIsReconnecting = false;
                }
            };
            LogUtils.i("开始连接: " + mConnectConfig.url);
            mClient.connect();
        } else {
            LogUtils.i("已经连接: " + mConnectConfig.url);
        }
    }

    /**
     * 连接打开
     */
    private void onConnectOpen() {
        Intent intent = new Intent();
        intent.setPackage(mContext.getPackageName());
        intent.setAction(ACTION_CONNECT_OPEN);
        mContext.sendBroadcast(intent);
    }

    private void onReceiveMessage(String message) {

    }

    void sent(String message) {
        if (isConnected()) {
            mClient.send(message);
        } else {
            LogUtils.e("WebSocket没有创建");
        }
    }

    void closeConnect() {
        try {
            if (mClient != null) {
                mClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mClient = null;
//            unregisterNetWorkStateReceiver(mContext);
        }
    }

    private boolean isConnected() {
        return mClient != null && mClient.isOpen();
    }

//    private void registerNetWorkStateReceiver(Context context) {
//        if (mReceiver == null) {
//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            mReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//                        if (NetWorkUtils.hasNetWork(context)) {
////                            if (mClient != null) {
////                                mHandler.postDelayed(mReConnectRunnable, RECONNECT_INTERVAL);
////                            }
//                        }
//                    }
//                }
//            };
//            context.registerReceiver(mReceiver, intentFilter);
//        }
//    }
//
//    private void unregisterNetWorkStateReceiver(Context context) {
//        if (mReceiver != null) {
//            context.unregisterReceiver(mReceiver);
//        }
//    }

    public static class ConnectConfig {
        private Context mContext;
        //连接url
        private String url;
        //重连间隔：毫秒
        private long reconnectInterval = 2000;
        //心跳间隔:秒
        private int heartBeatInterval = 30;

        public ConnectConfig(Context context) {
            mContext = context;
        }

        ConnectorManager build() {
            return ConnectorManager.getInstance(mContext, this);
        }

        public String getUrl() {
            return url;
        }

        public ConnectConfig setUrl(String url) {
            this.url = url;
            return this;
        }

        public long getReconnectInterval() {
            return reconnectInterval;
        }

        public ConnectConfig setReconnectInterval(long reconnectInterval) {
            this.reconnectInterval = reconnectInterval;
            return this;
        }

        public long getHeartBeatInterval() {
            return heartBeatInterval;
        }

        public ConnectConfig setHeartBeatInterval(int heartBeatInterval) {
            this.heartBeatInterval = heartBeatInterval;
            return this;
        }
    }

}
