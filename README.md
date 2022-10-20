# bugs
[![](https://jitpack.io/v/anxinxu/bugs.svg)](https://jitpack.io/#anxinxu/bugs)

collection bugs

### 1. No WebView installed

You may see errors like this `android.webkit.WebViewFactory$MissingWebViewPackageException: Failed to load WebView provider: No WebView installed`, so this device may not have the webview installed

ok, use this can simple fix it.

#### Step 1.download

1. download aar or jar from GitHub's [releases page][1].

or use gradle
### Step 1. Add the JitPack repository to your build file
```gradle
repositories {
    // add this
    maven { url 'https://jitpack.io' }
}
```

### Step 2. Add the dependency
```gradle
dependencies {
  implementation 'com.github.anxinxu:bugs:1.7'
}
```

#### Step 2. use
```java
        NoWebViewInstalled.setLogEnable(logEnable);
        try {
            String result = NoWebViewInstalled.fix(new NoWebViewInstalled.FixedWebCreateCallback() {
                @Override
                public void onCreateWebView(@Nullable WebView webView) {
                    // when webview fixed, create WebView call method
                    // you can toast or other case
                    Toast.makeText(context, "No WebView installed", Toast.LENGTH_SHORT).show();
                }
            });
            // can use result do something
        } catch (Throwable e) {
            e.printStackTrace();
        }
```
 
 you can use `NoWebViewInstalled.fakeError()` fake this error.


[1]: https://github.com/anxinxu/bugs/releases
