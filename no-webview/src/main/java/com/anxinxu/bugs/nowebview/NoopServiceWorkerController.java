package com.anxinxu.bugs.nowebview;

import android.os.Build;
import android.webkit.ServiceWorkerClient;
import android.webkit.ServiceWorkerController;
import android.webkit.ServiceWorkerWebSettings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NoopServiceWorkerController extends ServiceWorkerController {

    private final ServiceWorkerWebSettings settings = new NoopServiceWorkerWebSettings();

    @NonNull
    @Override
    public ServiceWorkerWebSettings getServiceWorkerWebSettings() {
        return settings;
    }

    @Override
    public void setServiceWorkerClient(@Nullable ServiceWorkerClient client) {

    }
}
