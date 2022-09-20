package com.anxinxu.bugs.nowebview;

import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ServiceWorkerController;
import android.webkit.TracingController;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;


import java.lang.reflect.Proxy;

class WebViewFactoryProvider extends BaseProxy {

    private Object statics;
    private final CookieManager cookieManager;
    private final NoWebViewInstalled.FixedWebCreateCallback callback;
    private final GeolocationPermissions geolocationPermissions;
    private final TracingController tracingController;
    private final ServiceWorkerController serviceWorkerController;
    private final WebStorage webStorage;
    private final WebViewDatabase webViewDatabase;
    private final PacProcessor pacProcessor;

    public WebViewFactoryProvider(NoWebViewInstalled.FixedWebCreateCallback callback) {
        cookieManager = new NoopCookieManager();
        this.callback = callback;
        geolocationPermissions = GeolocationPermissionsReflection.constructor.newInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            tracingController = new NoopTracingController();
        } else {
            tracingController = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceWorkerController = new NoopServiceWorkerController();
        } else {
            serviceWorkerController = null;
        }
        webStorage = WebStorageReflection.constructor.newInstance();
        webViewDatabase = new NoopWebViewDatabase();
        pacProcessor = new PacProcessor();
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
                case "getGeolocationPermissions":
                    if (method.getReturnType() == GeolocationPermissions.class) {
                        return geolocationPermissions;
                    } else {
                        return null;
                    }
                case "getTracingController":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (method.getReturnType() == TracingController.class) {
                            return tracingController;
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                case "getServiceWorkerController":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (method.getReturnType() == ServiceWorkerController.class) {
                            return serviceWorkerController;
                        }
                    } else {
                        return null;
                    }
                case "getWebStorage":
                    if (method.getReturnType() == WebStorage.class) {
                        return webStorage;
                    } else {
                        return null;
                    }
                case "getWebViewDatabase":
                    if (method.getReturnType() == WebViewDatabase.class) {
                        return webViewDatabase;
                    } else {
                        return null;
                    }
                case "getPacProcessor":
                    try {
                        return pacProcessor.getInstance(method.getReturnType());
                    } catch (Exception e) {
                        return null;
                    }
                case "createPacProcessor":
                    try {
                        return pacProcessor.createInstance(method.getReturnType());
                    } catch (Exception e) {
                        return null;
                    }
                case "getWebViewClassLoader":
                    return WebViewFactoryProvider.class.getClassLoader();
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
