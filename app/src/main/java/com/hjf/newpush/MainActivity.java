package com.hjf.newpush;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.hjf.push.ConnectorManager;
import com.hjf.push.JWebSocketClient;
import com.hjf.push.PushManager;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PushManager.connect("ws://echo.websocket.org");
//        PushManager.connect("ws://192.168.1.62:1884/kyzh?deviceCode=" + CommonUtil.getDeviceId(this));
//        PushManager.connect(this, "ws://sockettest.keyunzhihui.com/kyzh?deviceCode=" + CommonUtil.getDeviceId(this));

        ConnectorManager.ConnectConfig connectConfig = new ConnectorManager.ConnectConfig(this)
                .setUrl("ws://sockettest.keyunzhihui.com/kyzh?deviceCode=" + CommonUtil.getDeviceId(this));
        PushManager.getInstance().checkInit(getApplicationContext()).setConnectConfig(connectConfig);
        PushManager.getInstance().checkInit(getApplicationContext()).startPush();
    }
}
