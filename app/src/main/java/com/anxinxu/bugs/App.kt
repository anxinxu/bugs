package com.anxinxu.bugs

import android.app.Application
import android.content.Context
import android.webkit.WebView
import android.widget.Toast
import com.anxinxu.bugs.nowebview.NoWebViewInstalled

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        NoWebViewInstalled.setLogEnable(true)
//        NoWebViewInstalled.fakeError()
//        NoWebViewInstalled.fix(object : NoWebViewInstalled.FixedWebCreateCallback {
//            override fun onCreateWebView(webView: WebView?) {
//                Toast.makeText(this@App, "No WebView installed", Toast.LENGTH_LONG).show()
//            }
//        })
    }
}