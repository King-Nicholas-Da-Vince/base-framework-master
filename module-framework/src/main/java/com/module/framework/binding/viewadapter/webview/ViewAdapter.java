package com.module.framework.binding.viewadapter.webview;

import android.text.TextUtils;
import android.webkit.WebView;

import androidx.databinding.BindingAdapter;

/**
 *  on 2017/6/18.
 */
public class ViewAdapter {
    @BindingAdapter({"render"})
    public static void loadHtml(WebView webView, final String html) {
        if (!TextUtils.isEmpty(html)) {
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }
    @BindingAdapter({"loadUrl"})
    public static void loadUrl(WebView webView, final String url) {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);        }
    }
}
