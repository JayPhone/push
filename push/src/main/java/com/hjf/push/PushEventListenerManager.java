package com.hjf.push;

import java.util.ArrayList;
import java.util.List;

/**
 * author JayPhone
 * description:
 * date :2019/10/11 18:02
 */
public class PushEventListenerManager {
    private static List<PushEventListener> listeners = new ArrayList<PushEventListener>();

    public static void registerListener(PushEventListener listener) {
        listeners.add(listener);
    }

    public static void unRegisterListener(PushEventListener listener) {
        listeners.remove(listener);
    }

    public static void notifyConnectOpend() {
        if (listeners != null && listeners.size() > 0) {
            for (PushEventListener listener : listeners) {
                listener.onConnectOpend();
            }
        }
    }

    public static void notifyReceiveMessage(String msg) {
        if (listeners != null && listeners.size() > 0) {
            for (PushEventListener listener : listeners) {
                listener.onReceiveMessage(msg);
            }
        }
    }
}
