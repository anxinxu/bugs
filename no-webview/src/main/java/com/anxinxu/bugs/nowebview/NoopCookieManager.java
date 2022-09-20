package com.anxinxu.bugs.nowebview;

import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import androidx.annotation.Nullable;

class NoopCookieManager extends CookieManager {

    @Override
    public void setAcceptCookie(boolean accept) {

    }

    @Override
    public boolean acceptCookie() {
        return false;
    }

    @Override
    public void setAcceptThirdPartyCookies(WebView webview, boolean accept) {

    }

    @Override
    public boolean acceptThirdPartyCookies(WebView webview) {
        return false;
    }

    @Override
    public void setCookie(String url, String value) {

    }

    @Override
    public void setCookie(String url, String value, @Nullable ValueCallback<Boolean> callback) {

    }

    @Override
    public String getCookie(String url) {
        return "";
    }

    @Override
    public void removeSessionCookie() {

    }

    @Override
    public void removeSessionCookies(@Nullable ValueCallback<Boolean> callback) {

    }

    @Override
    public void removeAllCookie() {

    }

    @Override
    public void removeAllCookies(@Nullable ValueCallback<Boolean> callback) {

    }

    @Override
    public boolean hasCookies() {
        return false;
    }

    @Override
    public void removeExpiredCookie() {

    }

    @Override
    public void flush() {

    }
}
