package com.anxinxu.bugs.nowebview;

import com.anxinxu.lib.reflections.MethodReflectParams;
import com.anxinxu.lib.reflections.RefClass;
import com.anxinxu.lib.reflections.type.constructor.RefConstructor;
import com.anxinxu.lib.reflections.type.field.RefInt;
import com.anxinxu.lib.reflections.type.field.RefObject;

public class WebViewProviderResponseReflection {

    public static Class<?> TYPE = RefClass.load(WebViewProviderResponseReflection.class, "android.webkit.WebViewProviderResponse", true);

    public static RefObject<Object> packageInfo;
    public static RefInt status;

    @MethodReflectParams({"android.content.pm.PackageInfo", "int"})
    public static RefConstructor<Object> constructor;

}
