package com.anxinxu.bugs.nowebview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Method;

class BaseProxy {

    protected Object defaultInvoke(Method method, Object[] args) {
        switch (method.getName()) {
            case "hashCode":
                return hashCode();
            case "equals":
                return equals(args[0]);
            case "toString":
                return toString();
            default:
                return defReturnType(method, args);
        }
    }

    protected Object defReturnType(Method method, Object[] args) {
        Class<?> returnType = method.getReturnType();
        Object result;
        if (returnType == Void.TYPE) {
            result = null;
        } else if (returnType == boolean.class) {
            result = false;
        } else if (returnType == int.class) {
            result = 0;
        } else if (returnType == long.class) {
            result = 0L;
        } else if (returnType == short.class) {
            result = 0;
        } else if (returnType == char.class) {
            result = 0;
        } else if (returnType == byte.class) {
            result = 0;
        } else if (returnType == float.class) {
            result = 0f;
        } else if (returnType == double.class) {
            result = 0.0;
        } else if (returnType == String.class) {
            return "";
        } else {
            result = null;
        }
        Logger.d("BaseProxy", "defReturnType result:", result, returnType);
        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

}
