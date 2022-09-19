package com.anxinxu.bugs.nowebview;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class ScrollDelegate extends BaseProxy {

    public Object newProxy(Class<?> type) {
        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                Logger.d("ScrollDelegate", "invoke ", name);

                return defaultInvoke(method, args);
            }
        });
    }
}
