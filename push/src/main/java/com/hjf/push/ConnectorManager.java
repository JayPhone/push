package com.hjf.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;

import com.hjf.push.util.LogUtils;
import com.hjf.push.util.NetWorkUtils;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import static com.hjf.push.PushReceiver.ACTION_CONNECT_OPEN;
import static com.hjf.push.PushReceiver.ACTION_RECEIVE_MESSAGE;

/**
 * author JayPhone
 * description: WebSocket连接管理类
 * date :2019/10/8 17:18
 */
class ConnectorManager {
    //重连间隔
    private static final int RECONNECT_INTERVAL = 2000;
    //单例连接管理器
    private volatile static ConnectorManager instance;
    //WebSocket客户端
    private JWebSocketClient mClient;
    //连接url
    private String mUrl;
    private Context mContext;
    private BroadcastReceiver mReceiver;
    private boolean mIsReconnecting = false;

    private ConnectorManager(Context context) {
        mContext = context;
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
                    createConnect(mUrl);
                }
            }
        }
    };

    void createConnect(final String url) {
        mUrl = url;
        if (TextUtils.isEmpty(url)) {
            LogUtils.e("url为空");
            return;
        }
        URI uri = URI.create(url);
        if (!isConnected()) {
            mClient = new JWebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    super.onOpen(handshakedata);
                    registerNetWorkStateReceiver(mContext);
                    mIsReconnecting = false;
                    onConnectOpen();
                }

                @Override
                public void onError(Exception ex) {
                    super.onError(ex);
                    mIsReconnecting = false;
                    mHandler.postDelayed(mReConnectRunnable, RECONNECT_INTERVAL);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                    mIsReconnecting = false;
                }
            };
            LogUtils.i("开始连接: " + url);
            mClient.connect();
        } else {
            LogUtils.i("已经连接: " + url);
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
            unregisterNetWorkStateReceiver(mContext);
        }
    }

    private boolean isConnected() {
        return mClient != null && mClient.isOpen();
    }

    private void registerNetWorkStateReceiver(Context context) {
        if (mReceiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                        if (NetWorkUtils.hasNetWork(context)) {
//                            if (mClient != null) {
//                                mHandler.postDelayed(mReConnectRunnable, RECONNECT_INTERVAL);
//                            }
                        }
                    }
                }
            };
            context.registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterNetWorkStateReceiver(Context context) {
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
    }
}
