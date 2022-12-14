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
        //???????????????
        webView.isHorizontalScrollBarEnabled = false
        webView.isVerticalScrollBarEnabled = false

        webView.settings.apply {
            //??????javascript??????
            javaScriptEnabled = true
            //??????dom??????
            domStorageEnabled = true
            databaseEnabled = true
            databasePath = "/data/data"

            //??????js???????????????
            javaScriptCanOpenWindowsAutomatically = true
            //???????????????
            setSupportMultipleWindows(true)

            //??????????????????
            allowFileAccess = true
            allowFileAccessFromFileURLs = true

            //?????????????????????
            mediaPlaybackRequiresUserGesture = false
            //??????????????????
            defaultTextEncodingName = "UTF-8"
            //???????????????
            savePassword = false

            //??????http/https????????????
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            //???????????????????????????????????????????????????????????????????????????????????????????????????
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = false
            }
            //?????????ua
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