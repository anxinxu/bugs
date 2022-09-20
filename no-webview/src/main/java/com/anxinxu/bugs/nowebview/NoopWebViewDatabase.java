package com.anxinxu.bugs.nowebview;

import android.webkit.WebViewDatabase;

import androidx.annotation.Nullable;

public class NoopWebViewDatabase extends WebViewDatabase {
    @Override
    public boolean hasUsernamePassword() {
        return false;
    }

    @Override
    public void clearUsernamePassword() {

    }

    @Override
    public boolean hasHttpAuthUsernamePassword() {
        return false;
    }

    @Override
    public void clearHttpAuthUsernamePassword() {

    }

    @Override
    public void setHttpAuthUsernamePassword(String host, String realm, String username, String password) {

    }

    @Nullable
    @Override
    public String[] getHttpAuthUsernamePassword(String host, String realm) {
        return null;
    }

    @Override
    public boolean hasFormData() {
        return false;
    }

    @Override
    public void clearFormData() {

    }
}
