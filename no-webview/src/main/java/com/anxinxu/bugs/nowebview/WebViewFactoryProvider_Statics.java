package com.anxinxu.bugs.nowebview;


import android.net.Uri;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class WebViewFactoryProvider_Statics extends BaseProxy {

    public Object newProxy(Class<?> type) {
        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("getSafeBrowsingPrivacyPolicyUrl") && Uri.class == method.getReturnType()) {
                    return Uri.EMPTY;
                }
                Logger.d("Factory_Statics", "invoke ", method.getName());
                return defaultInvoke(method, args);
            }
        });
    }
}
