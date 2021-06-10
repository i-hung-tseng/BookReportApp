package com.example.bookreports.bookstore//package com.example.bookreports.bookstore
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import androidx.annotation.RequiresApi
//import androidx.core.content.ContextCompat.startActivity
//import androidx.navigation.fragment.findNavController
//import com.example.bookreports.R
//import com.example.bookreports.databinding.FragmentBookStoreBinding
//import com.example.bookreports.utils.MainActivity
//
//
//class BookStoreFragment : Fragment(),IonBackPressed {
//
//
//    lateinit var webView:WebView
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val binding = FragmentBookStoreBinding.inflate(inflater)
//
//        webView = binding.webView
//        webView.apply {
//            loadUrl("https://www.books.com.tw/web/books?loc=menu_sec_0_001")
//            webViewClient = MyWebViewClient()
//        }
//
//
//
//
//
//
//        return binding.root
//
//    }
//
//    override fun onBackPressed(): Boolean {
//        Log.d("Testing","onBackPressed")
//        return if(webView.canGoBack()){
//            webView.goBack()
//            true
//        }else{
//            false
//        }
//    }
//
//}
//
//
//private class MyWebViewClient : WebViewClient() {
//
//
//
//
//    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//        if (Uri.parse(url).host == "https://www.books.com.tw/") {
//            Log.d("Testing","enter if hos")
//            return false
//        }else{
//            return true
//        }
//
//
//        return super.shouldOverrideUrlLoading(view, url)
//    }
//
//}
//
//interface IonBackPressed{
//    fun onBackPressed(): Boolean
//}
