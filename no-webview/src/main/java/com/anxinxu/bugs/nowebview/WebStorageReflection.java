package com.anxinxu.bugs.nowebview;

import android.webkit.WebStorage;

import com.anxinxu.lib.reflections.RefClass;
import com.anxinxu.lib.reflections.type.constructor.RefConstructor;

public class WebStorageReflection {

    public static Class<?> TYPE = RefClass.load(WebStorageReflection.class, WebStorage.class, true);

    public static RefConstructor<WebStorage> constructor;

}
