package com.anxinxu.bugs.nowebview;

import com.anxinxu.lib.reflections.RefClass;
import com.anxinxu.lib.reflections.type.field.s.RefStaticBoolean;
import com.anxinxu.lib.reflections.type.field.s.RefStaticInt;
import com.anxinxu.lib.reflections.type.field.s.RefStaticObject;
import com.anxinxu.lib.reflections.type.method.RefStaticMethod;

class WebViewFactoryReflection {

    public static Class<?> TYPE = RefClass.load(WebViewFactoryReflection.class, "android.webkit.WebViewFactory", true);

    public static RefStaticObject<Object> sProviderInstance;
    public static RefStaticBoolean sWebViewDisabled;
    public static RefStaticMethod<Object> getProvider;
    public static RefStaticMethod<Class<?>> getFactoryClass; // 5.0, 5.1
    public static RefStaticMethod<Class<?>> getProviderClass; // 6.0 - 13
    public static RefStaticMethod<Object> getUpdateService;

    public static RefStaticObject<String> WEBVIEW_UPDATE_SERVICE_NAME;
    public static RefStaticInt LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES;

}
