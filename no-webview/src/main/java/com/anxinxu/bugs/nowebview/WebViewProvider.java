package com.anxinxu.bugs.nowebview;

import android.view.textclassifier.TextClassifier;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class WebViewProvider extends BaseProxy {
    private Object viewDelegate;
    private Object scrollDelegate;

    private final WebView webView;
    private final Object privateAccess;
    private final WebSettings webSettings;
    private final Object textClassifier;
    private final WebChromeClient webChromeClient;
    private final WebViewClient webViewClient;
    private final CookieManager cookieManager;
    private final NoWebViewInstalled.FixedWebCreateCallback callback;

    public WebViewProvider(WebView webView, Object privateAccess, CookieManager cookieManager, NoWebViewInstalled.FixedWebCreateCallback callback) {
        this.webView = webView;
        this.privateAccess = privateAccess;
        webSettings = new NoopWebSettings();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            textClassifier = new TextClassifier() {
                @NonNull
                @Override
                public String toString() {
                    return "TextClassifier.NO_OP";
                }
            };
        } else {
            textClassifier = null;
        }
        webChromeClient = new WebChromeClient();
        webViewClient = new WebViewClient();
        this.cookieManager = cookieManager;
        this.callback = callback;
    }

    public Object newProxy(Class<?> type) {
        return Proxy.newProxyInstance(WebViewProvider.class.getClassLoader(), new Class[]{type}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                Logger.d("WebViewProvider", "invoke ", name);
                switch (name) {
                    case "getViewDelegate":
                        return getViewDelegate(method.getReturnType());
                    case "getScrollDelegate":
                        return getScrollDelegate(method.getReturnType());
                    case "getTextClassifier":
                        return textClassifier;
                    case "getSettings":
                        return webSettings;
                    case "getWebChromeClient":
                        return webChromeClient;
                    case "getWebViewClient":
                        return webViewClient;
                    case "saveState":
                    case "restoreState":
                    case "copyBackForwardList":
                        return new NoopWebBackForwardList();
                    default:
                        return defaultInvoke(method, args);
                }
            }
        });
    }

    private Object getScrollDelegate(Class<?> type) {
        if (scrollDelegate != null) return scrollDelegate;
        synchronized (this) {
            if (scrollDelegate == null) {
                scrollDelegate = new ScrollDelegate().newProxy(type);
            }
        }
        return scrollDelegate;
    }

    private Object getViewDelegate(Class<?> type) {
        if (viewDelegate != null) return viewDelegate;
        synchronized (this) {
            if (viewDelegate == null) {
                viewDelegate = new ViewDelegate(webView, privateAccess, cookieManager, callback).newProxy(type);
            }
        }
        return viewDelegate;
    }
}
