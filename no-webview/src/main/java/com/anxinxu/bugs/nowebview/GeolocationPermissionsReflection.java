package com.anxinxu.bugs.nowebview;

import android.webkit.GeolocationPermissions;

import com.anxinxu.lib.reflections.RefClass;
import com.anxinxu.lib.reflections.type.constructor.RefConstructor;

public class GeolocationPermissionsReflection {

    public static Class<?> TYPE = RefClass.load(GeolocationPermissionsReflection.class, GeolocationPermissions.class, true);

    public static RefConstructor<GeolocationPermissions> constructor;
}
