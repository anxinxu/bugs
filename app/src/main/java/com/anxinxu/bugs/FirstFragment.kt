package com.anxinxu.bugs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.anxinxu.bugs.databinding.FragmentFirstBinding
import com.anxinxu.bugs.nowebview.NoWebViewInstalled

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {
        var isFixed = false
        var isFakeError = true
    }


    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.buttonWeb.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_WebFragment)
        }
        binding.switchWeb.isChecked = isFixed
        checkWebFix()
        binding.switchWeb.setOnCheckedChangeListener { buttonView, isChecked ->
            isFixed = isChecked
            checkWebFix()
        }
        binding.switchWebError.isChecked = isFakeError
        checkWebFakeError()
        binding.switchWebError.setOnCheckedChangeListener { buttonView, isChecked ->
            isFakeError = isChecked
            checkWebFakeError()
        }
    }

    private fun checkWebFix() {
        if (isFixed) {
            NoWebViewInstalled.fix(object : NoWebViewInstalled.FixedWebCreateCallback {
                override fun onCreateWebView(webView: WebView?) {
                    webView?.context?.applicationContext?.let {
                        Toast.makeText(it, "No WebView installed", Toast.LENGTH_LONG).show()
                    }
                }
            })
        } else {
            NoWebViewInstalled.disableFix()
        }
    }

    private fun checkWebFakeError() {
        if (isFakeError) {
            NoWebViewInstalled.fakeError()
        } else {
            NoWebViewInstalled.disableFakeError()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}