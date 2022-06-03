package com.app.yellodriver.fragment;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.yellodriver.R;

public class WebViewFragment extends BaseFragment {

    private WebView webView;

    @Override
    protected void initializeComponent(View view) {
        webView = view.findViewById(R.id.fragment_webView);

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.about_us);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        webView.loadUrl("https://google.com");

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_webview;
    }
}
