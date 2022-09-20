package com.anxinxu.bugs.nowebview;

import android.os.Build;
import android.webkit.ServiceWorkerWebSettings;
import android.webkit.WebSettings;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NoopServiceWorkerWebSettings extends ServiceWorkerWebSettings {
    @Override
    public void setCacheMode(int mode) {

    }

    @Override
    public int getCacheMode() {
        return WebSettings.LOAD_NO_CACHE;
    }

    @Override
    public void setAllowContentAccess(boolean allow) {

    }

    @Override
    public boolean getAllowContentAccess() {
        return false;
    }

    @Override
    public void setAllowFileAccess(boolean allow) {

    }

    @Override
    public boolean getAllowFileAccess() {
        return false;
    }

    @Override
    public void setBlockNetworkLoads(boolean flag) {

    }

    @Override
    public boolean getBlockNetworkLoads() {
        return false;
    }
}
