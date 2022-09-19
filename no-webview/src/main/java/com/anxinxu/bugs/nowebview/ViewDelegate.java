package com.anxinxu.bugs.nowebview;

import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;

class ViewDelegate extends BaseProxy {

    private static final String METHOD_PREFIX = "super_";

    private final HashMap<String, Method> privateAccessMethodMap = new HashMap<>();
    private final HashSet<String> privateAccessSuperMap = new HashSet<>();

    private final WebView webView;
    private final Object privateAccess;
    private final CookieManager cookieManager;
    private final NoWebViewInstalled.FixedWebCreateCallback callback;

    public ViewDelegate(WebView webView, Object privateAccess, CookieManager cookieManager, NoWebViewInstalled.FixedWebCreateCallback callback) {
        this.webView = webView;
        this.privateAccess = privateAccess;
        this.cookieManager = cookieManager;
        this.callback = callback;
        initPrivateAccessMethods(privateAccess);
    }

    private void initPrivateAccessMethods(Object privateAccess) {
        if (privateAccess == null) return;
        try {
            Method[] declaredMethods = privateAccess.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                try {
                    method.setAccessible(true);
                    String name = method.getName();
                    privateAccessMethodMap.put(name, method);
                    if (name.startsWith(METHOD_PREFIX)) {
                        privateAccessSuperMap.add(name.substring(METHOD_PREFIX.length()));
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            Logger.d("ViewDelegate", "privateAccessSuperMap:", privateAccessMethodMap);
            Logger.d("ViewDelegate", "privateAccessSuperMap:", privateAccessSuperMap);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public Object newProxy(Class<?> type) {
        return Proxy.newProxyInstance(ViewDelegate.class.getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                Logger.d("ViewDelegate", "invoke ", name);
                if (privateAccessSuperMap.contains(name)) {
                    return invokePrivateAccessMethod(method, args, defReturnType(method, args));
                } else if ("getCookieManager".equals(name)) {
                    return cookieManager;
                } else if ("getHandler".equals(name)) {
                    return args != null && args.length > 0 && args[0] instanceof Handler ? args[0] : null;
                } else if ("findFocus".equals(name)) {
                    return args != null && args.length > 0 && args[0] instanceof View ? args[0] : null;
                } else if ("onWindowFocusChanged".equals(name)) {
                    if (callback != null) {
                        callback.onWindowFocusChanged(args != null && args.length > 0 && args[0] instanceof Boolean && (boolean) args[0]);
                    }
                    return defReturnType(method, args);
                } else if ("onVisibilityChanged".equals(name)) {
                    if (callback != null) {
                        callback.onWebViewVisibilityChanged(webView, args != null && args.length > 1 && args[1] instanceof Integer ? (int) args[1] : -1);
                    }
                    return defReturnType(method, args);
                } else if ("onWindowVisibilityChanged".equals(name)) {
                    if (callback != null) {
                        callback.onWindowVisibilityChanged(args != null && args.length > 0 && args[0] instanceof Integer ? (int) args[0] : -1);
                    }
                    return defReturnType(method, args);
                } else {
                    return defaultInvoke(method, args);
                }
            }
        });
    }

    private Object invokePrivateAccessMethod(Method proxyMethod, Object[] args, Object defReturn) {
        String methodName = proxyMethod.getName();
        Method callMethod = privateAccessMethodMap.get(METHOD_PREFIX + methodName);
        if (callMethod == null) {
            callMethod = privateAccessMethodMap.get(methodName);
        }
        if (callMethod == null) {
            Logger.e("ViewDelegate", "invokePrivateAccessMethod not found ", methodName);
            return defReturn;
        }

        try {
            return callMethod.invoke(privateAccess, args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Logger.e(throwable, "ViewDelegate", "invokePrivateAccessMethod invoker error", methodName);
            return defReturn;
        }
    }
}
