package com.anxinxu.bugs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anxinxu.bugs.databinding.FragmentWebBinding

class WebFragment : Fragment() {
    private var _binding: FragmentWebBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                kotlin.runCatching {

                    WebView.setDataDirectorySuffix(BuildConfig.APPLICATION_ID)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonPrevious.setOnClickListener {
            findNavController().navigate(R.id.action_WebFragment_to_FirstFragment)
        }
        binding.buttonGoBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                findNavController().navigate(R.id.action_WebFragment_to_FirstFragment)
            }
        }
        binding.webView.apply {
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            addJavascriptInterface(JsBridgeApi(), "bugs-no-webview-installed")
            setWebSettings(this)
            loadUrl("file:android_asset/h5.html")
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                findNavController().navigate(R.id.action_WebFragment_to_FirstFragment)
            }
        }
    }

    private fun setWebSettings(webView: WebView) {
        //隐藏滚动条
        webView.isHorizontalScrollBarEnabled = false
        webView.isVerticalScrollBarEnabled = false

        webView.settings.apply {
            //开启javascript支持
            javaScriptEnabled = true
            //开启dom支持
            domStorageEnabled = true
            databaseEnabled = true
            databasePath = "/data/data"

            //开启js新窗口支持
            javaScriptCanOpenWindowsAutomatically = true
            //支持多窗口
            setSupportMultipleWindows(true)

            //文件相关设置
            allowFileAccess = true
            allowFileAccessFromFileURLs = true

            //视频播放相关，
            mediaPlaybackRequiresUserGesture = false
            //设置编码格式
            defaultTextEncodingName = "UTF-8"
            //不记住密码
            savePassword = false

            //允许http/https混合内容
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            //关闭安全浏览模式（出现过一次不安全提示，关闭这个可以避免这个情况）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = false
            }
            //自定义ua
            userAgentString = "$userAgentString bus/test"
        }
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
        _binding = null
    }

}

class JsBridgeApi {

    @JavascriptInterface
    fun exec(json: String?): String {
        return "not impl"
    }
}