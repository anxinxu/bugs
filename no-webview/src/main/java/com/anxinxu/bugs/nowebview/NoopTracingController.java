package com.anxinxu.bugs.nowebview;

import android.os.Build;
import android.webkit.TracingConfig;
import android.webkit.TracingController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.OutputStream;
import java.util.concurrent.Executor;

@RequiresApi(api = Build.VERSION_CODES.P)
public class NoopTracingController extends TracingController {

    @Override
    public void start(@NonNull TracingConfig tracingConfig) {

    }

    @Override
    public boolean stop(@Nullable OutputStream outputStream, @NonNull Executor executor) {
        return false;
    }

    @Override
    public boolean isTracing() {
        return false;
    }
}
