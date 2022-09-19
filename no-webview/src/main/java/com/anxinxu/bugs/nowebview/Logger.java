package com.anxinxu.bugs.nowebview;


import android.util.Log;

class Logger {

    private final static String TAG = "NoWebView";

    static void d(Object... msg) {
        if (!NoWebViewInstalled.logEnable) {
            return;
        }
        Log.d(TAG, getMsg(msg));
    }

    static void e(Object... msg) {
        if (!NoWebViewInstalled.logEnable) {
            return;
        }
        Log.e(TAG, getMsg(msg));
    }

    static void e(Throwable throwable, Object... msg) {
        if (!NoWebViewInstalled.logEnable) {
            return;
        }
        Log.e(TAG, getMsg(msg), throwable);
    }

    private static String getMsg(Object... msg) {
        if (msg == null || msg.length <= 0) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        for (Object o : msg) {
            builder.append(o).append(" ");
        }
        return builder.toString();
    }
}
