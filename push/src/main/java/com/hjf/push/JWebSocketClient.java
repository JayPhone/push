package com.hjf.push;


import com.hjf.push.util.LogUtils;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * author JayPhone
 * description:
 * date :2019/10/8 16:55
 */
public class JWebSocketClient extends WebSocketClient {
    private URI mURI;

    public JWebSocketClient(URI serverUri, int heartBeatInterval) {
        super(serverUri);
        mURI = serverUri;
        //服务端60秒检测一次，3次没有收到ping或消息则踢掉
        setConnectionLostTimeout(heartBeatInterval);
    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        super.onWebsocketPing(conn, f);
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
        super.onWebsocketPong(conn, f);
        LogUtils.i("onWebSocketPong");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LogUtils.i("onOpen " + mURI.toString() + " status:" + handshakedata.getHttpStatus() + " message:" + handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        LogUtils.i("onMessage: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LogUtils.i("onClose code: " + code + " reason: " + reason + " remote: " + remote);
    }

    @Override
    public void onError(Exception ex) {
        LogUtils.wtf("onError", ex);
    }
}
