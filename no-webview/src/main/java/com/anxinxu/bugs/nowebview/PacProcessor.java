package com.anxinxu.bugs.nowebview;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PacProcessor extends BaseProxy {

    private Object singleInstance;

    public Object getInstance(Class<?> type) {
        if (singleInstance != null) {
            return singleInstance;
        }
        synchronized (this) {
            if (singleInstance == null) {
                singleInstance = createInstance(type);
            }
        }
        return singleInstance;
    }

    public Object createInstance(Class<?> type) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                switch (name) {
                    case "setNetwork":
                    case "getNetwork":
                    case "findProxyForUrl":
                        return null;
                }
                return defaultInvoke(method, args);
            }
        });
    }
}
