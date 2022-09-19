package com.anxinxu.bugs.nowebview;

import android.os.IInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anxinxu.lib.reflection.android.ServiceManagerReflection;

import java.lang.reflect.Method;

public class NoWebViewInstalled {

    public interface FixedWebCreateCallback {
        void onCreateWebView(@Nullable WebView webView);

        default void onWebViewVisibilityChanged(@Nullable WebView webView, int visibility) {
            Logger.d("FixedWebCreateCallback", "onWebViewVisibilityChanged", webView, visibility);
        }

        default void onWindowVisibilityChanged(int visibility) {
            Logger.d("FixedWebCreateCallback", "onWindowVisibilityChanged", visibility);
        }

        default void onWindowFocusChanged(boolean hasWindowFocus) {
            Logger.d("FixedWebCreateCallback", "onWindowFocusChanged", hasWindowFocus);
        }
    }

    private final static String TAG = "NoInstalledWebView";


    public static final String NO_FIX = "no_fix";
    public static final String NO_FIX_EX = "no_fix_ex";
    public static final String FIXED = "fixed";
    public static final String ERROR_RWVFC = "WebViewFactory";
    public static final String ERROR_RSPI = "sProviderInstance";
    public static final String ERROR_RSPIT = "sProviderInstanceType";
    public static final String ERROR_RSPITI = "sProviderInstanceType_not_interface";
    public static final String ERROR_RGP = "getProvider";
    public static final String ERROR_RGPC = "getProviderClass";
    public static final String ERROR_NWVFPP = "newWebViewFactoryProviderProxy";
    public static final String ERROR_RSPIS = "sProviderInstanceSet";
    public static final String ERROR_HSPF = "hadSetProviderFactory";
    public static final String ERROR_HSPFF = "hadSetProviderFactoryFix";

    static boolean logEnable = false;
    private static WebViewFactoryProvider webViewFactoryProvider;
    private static Object webViewFactoryProviderProxy;
    private static ServiceManagerReflection.IHookBinderResult hookBinderResult;

    public static void disableFix() {
        Logger.d(TAG, "disableFix start...");
        if (webViewFactoryProvider != null && webViewFactoryProviderProxy != null && WebViewFactoryReflection.sProviderInstance.get() == webViewFactoryProviderProxy) {
            WebViewFactoryReflection.sProviderInstance.set(null);
            webViewFactoryProvider = null;
            webViewFactoryProviderProxy = null;
            Logger.d(TAG, "disableFix succeed");
        } else {
            Logger.e(TAG, "disableFix failed", webViewFactoryProvider, webViewFactoryProviderProxy, WebViewFactoryReflection.sProviderInstance.get());
        }
    }

    public static String fix(FixedWebCreateCallback callback) throws Throwable {
        Logger.d(TAG, "fix start...");
        try {
            if (WebViewFactoryReflection.TYPE == null) {
                Logger.e(TAG, "fix reflect error", ERROR_RWVFC);
                return ERROR_RWVFC;
            }
            if (!WebViewFactoryReflection.sProviderInstance.isReflectSucceed()) {
                Logger.e(WebViewFactoryReflection.sProviderInstance.getError(), TAG, "fix result reflect error", ERROR_RSPI);
                return ERROR_RSPI;
            }
            Class<?> sProviderInstanceClass = WebViewFactoryReflection.sProviderInstance.fieldType();
            if (sProviderInstanceClass == null) {
                Logger.e(TAG, "fix result reflect error", ERROR_RSPIT);
                return ERROR_RSPIT;
            }
            if (!sProviderInstanceClass.isInterface()) {
                Logger.e(TAG, "fix result reflect error", ERROR_RSPITI, sProviderInstanceClass);
                return ERROR_RSPITI;
            }
            Object o = WebViewFactoryReflection.sProviderInstance.get();
            if (o != null) {
                if (o == webViewFactoryProviderProxy) {
                    Logger.e(TAG, "fix result provider is not null", ERROR_HSPFF, o);
                    return ERROR_HSPFF;
                } else {
                    Logger.e(TAG, "fix result provider is not null", ERROR_HSPF, o);
                    return ERROR_HSPF;
                }
            }

            if (!WebViewFactoryReflection.getProvider.isReflectSucceed()) {
                Logger.e(WebViewFactoryReflection.getProvider.getError(), TAG, "fix result reflect error", ERROR_RGP);
                return ERROR_RGP;
            }
            if (!WebViewFactoryReflection.getProviderClass.isReflectSucceed() && !WebViewFactoryReflection.getFactoryClass.isReflectSucceed()) {
                Logger.e(TAG, "fix result reflect error", ERROR_RGPC);
                return ERROR_RGPC;
            }

            Class<?> providerClazz;
            Throwable error;
            if (WebViewFactoryReflection.getProviderClass.isReflectSucceed()) {
                providerClazz = WebViewFactoryReflection.getProviderClass.invoke();
                error = WebViewFactoryReflection.getProviderClass.getError();
            } else {
                providerClazz = WebViewFactoryReflection.getFactoryClass.invoke();
                error = WebViewFactoryReflection.getFactoryClass.getError();
            }
            if (providerClazz == null) {
                Logger.e(error, TAG, "fix provider class is null");
                Object provider = WebViewFactoryReflection.getProvider.invoke();
                if (provider == null) {
                    Logger.e(WebViewFactoryReflection.getProvider.getError(), TAG, "fix getProvider is null");
                    WebViewFactoryProvider webViewFactoryProvider = new WebViewFactoryProvider(callback);
                    Object webViewFactoryProviderProxy = webViewFactoryProvider.newProxy(sProviderInstanceClass);
                    if (webViewFactoryProviderProxy != null) {
                        WebViewFactoryReflection.sProviderInstance.set(webViewFactoryProviderProxy);
                        Throwable sProviderInstanceError = WebViewFactoryReflection.sProviderInstance.getError();
                        if (sProviderInstanceError == null) {
                            Logger.d(TAG, "fix result fixed", FIXED);
                            NoWebViewInstalled.webViewFactoryProvider = webViewFactoryProvider;
                            NoWebViewInstalled.webViewFactoryProviderProxy = webViewFactoryProviderProxy;
                            return FIXED;
                        } else {
                            Logger.e(sProviderInstanceError, TAG, "fix result set provider instance error", ERROR_RSPIS);
                            return ERROR_RSPIS;
                        }
                    } else {
                        Logger.e(TAG, "fix result newProxy error", ERROR_NWVFPP);
                        return ERROR_NWVFPP;
                    }
                } else {
                    Logger.d(TAG, "fix result not need fix ex", NO_FIX_EX);
                    return NO_FIX_EX;
                }
            }
        } catch (Throwable throwable) {
            Logger.e(throwable, TAG, "fix result throw error");
            throw throwable;
        }
        Logger.d(TAG, "fix result not need", NO_FIX);
        return NO_FIX;
    }

    public static void setLogEnable(boolean logEnable) {
        NoWebViewInstalled.logEnable = logEnable;
    }

    public static void disableFakeError() {
        Logger.d(TAG, "disableFakeError", hookBinderResult);
        if (hookBinderResult != null) {
            hookBinderResult.unhook();
            hookBinderResult = null;
        }
    }

    public static void fakeError() {
        Logger.e(TAG, "**************NOTE: this for fake error for android.webkit.WebViewFactory$MissingWebViewPackageException: Failed to load WebView provider: No WebView installed");
        Logger.d(TAG, "fakeError start...");
        if (!WebViewFactoryReflection.WEBVIEW_UPDATE_SERVICE_NAME.isSucceed()) {
            Logger.e(WebViewFactoryReflection.WEBVIEW_UPDATE_SERVICE_NAME.getError(), TAG, "fakeError serviceName reflect error maybe hidden api");
            return;
        }

        int errorStatus = WebViewFactoryReflection.LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES.get();
        if (!WebViewFactoryReflection.LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES.isSucceed()) {
            Logger.e(WebViewFactoryReflection.LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES.getError(), TAG, "fakeError status not install code reflect error");
            return;
        }

        String serviceName = WebViewFactoryReflection.WEBVIEW_UPDATE_SERVICE_NAME.get();
        if (serviceName == null || serviceName.isEmpty()) {
            Logger.e(WebViewFactoryReflection.WEBVIEW_UPDATE_SERVICE_NAME.getError(), TAG, "fakeError get serviceName null");
            return;
        }
        if (!WebViewFactoryReflection.getUpdateService.isSucceed()) {
            Logger.e(WebViewFactoryReflection.getUpdateService.getError(), TAG, "fakeError getUpdateService reflect error");
            return;
        }
        if (!WebViewProviderResponseReflection.packageInfo.isSucceed()) {
            Logger.e(WebViewProviderResponseReflection.packageInfo.getError(), TAG, "fakeError packageInfo reflect error");
            return;
        }

        if (!WebViewProviderResponseReflection.status.isSucceed()) {
            Logger.e(WebViewProviderResponseReflection.status.getError(), TAG, "fakeError status reflect error");
            return;
        }

        if (!WebViewProviderResponseReflection.constructor.isSucceed()) {
            Logger.e(WebViewProviderResponseReflection.constructor.getError(), TAG, "fakeError constructor reflect error");
            return;
        }
        Class<?> serviceInterface = WebViewFactoryReflection.getUpdateService.method().getReturnType();
        try {
            hookBinderResult = ServiceManagerReflection.hookBinder(serviceName, serviceInterface, new ServiceManagerReflection.ServiceCall() {
                @Override
                public boolean beforeCall(@NonNull IInterface originService, @NonNull Method method, @Nullable Object[] args) {
                    ServiceManagerReflection.ServiceCall.super.beforeCall(originService, method, args);
                    return method.getName().equals("waitForAndGetProvider");
                }

                @Override
                public Result afterCall(@NonNull IInterface originService, @NonNull Method method, @Nullable Object[] args, @Nullable Object originReturn) {
                    if (method.getName().equals("waitForAndGetProvider")) {
                        ServiceManagerReflection.ServiceCall.super.afterCall(originService, method, args, originReturn);
                        Object packageInfo = WebViewProviderResponseReflection.packageInfo.get(originReturn);
                        int status = WebViewProviderResponseReflection.status.get(originReturn);
                        Logger.d(TAG, "waitForAndGetProvider", status, packageInfo);
                        return new ServiceManagerReflection.ServiceCall.Result(true, WebViewProviderResponseReflection.constructor.newInstanceOrDefault(originReturn, packageInfo, errorStatus));
                    } else {
                        return ServiceManagerReflection.ServiceCall.super.afterCall(originService, method, args, originReturn);
                    }
                }
            });
            Logger.d(TAG, "fakeError succeed");
        } catch (IllegalStateException e) {
            Logger.e(e, "fakeError error");
        }
    }
}
