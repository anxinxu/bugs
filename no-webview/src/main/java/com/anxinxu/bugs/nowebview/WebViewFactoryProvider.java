package com.anxinxu.bugs.nowebview;

import android.webkit.CookieManager;
import android.webkit.WebView;


import java.lang.reflect.Proxy;

class WebViewFactoryProvider extends BaseProxy {

    private Object statics;
    private final CookieManager cookieManager;
    private final NoWebViewInstalled.FixedWebCreateCallback callback;

    public WebViewFactoryProvider(NoWebViewInstalled.FixedWebCreateCallback callback) {
        cookieManager = new NoopCookieManager();
        this.callback = callback;
    }

    public Object newProxy(Class<?> type) {
        if (type == null || !type.isInterface()) {
            return null;
        }
        return Proxy.newProxyInstance(WebViewFactoryProvider.class.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            String name = method.getName();
            Logger.d("WebViewFactoryProvider", "invoke ", name);
            switch (name) {
                case "createWebView":
                    return createWebView(method.getReturnType(), args);
                case "getStatics":
                    return getStatics(method.getReturnType(), args);
                case "getCookieManager":
                    return cookieManager;
                default:
                    return defaultInvoke(method, args);
            }
        });
    }

    private Object getStatics(Class<?> returnType, Object[] args) {
        if (statics != null) return statics;
        synchronized (this) {
            if (statics == null) {
                statics = new WebViewFactoryProvider_Statics().newProxy(returnType);
            }
        }
        return statics;
    }

    private Object createWebView(Class<?> type, Object[] args) {
        WebView webView = args != null && args.length > 0 && args[0] instanceof WebView ? (WebView) args[0] : null;
        Object privateAccess = args != null && args.length > 1 ? args[1] : null;
        Logger.d("WebViewFactoryProvider", String.format("createWebView type:%s, webView:%s, privateAccess:%s", type, webView, privateAccess));
        if (callback != null) {
            callback.onCreateWebView(webView);
        }
        return new WebViewProvider(webView, privateAccess, cookieManager, callback).newProxy(type);
    }

}
