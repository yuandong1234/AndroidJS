package com.yuong.androidjs;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String url = "file:////android_asset/Test.html";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWebconfig();

        //加载网页
        loadUrl(url);
    }

    private void initView() {
        webView = findViewById(R.id.webView);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
    }
    
    private void initWebconfig(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSettings.supportMultipleWindows();//支持多窗口

        webSettings.setUseWideViewPort(true);//调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局

        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setDisplayZoomControls(false);//支持直接隐藏缩放控件

        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);//允许访问文件

        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片

        webSettings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        webSettings.setBlockNetworkImage(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(new AndroidJS(), "test");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }


        });
        webView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                androidToJS();
                break;
            case R.id.btn2:
                androidToJS2();
                break;
            case R.id.btn3:
                androidToJS3();
                break;
        }
    }

    //Android调用js方法
    private void androidToJS() {
        webView.loadUrl("javascript:(androidToJS())");
    }

    //Android调用js方法
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void androidToJS2() {
        webView.evaluateJavascript("javascript:(androidToJS())", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
                Log.e("MainActivity", "value : " + value);
            }
        });
    }

    //Android调用js方法(有参数)
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void androidToJS3() {
        String params = "android 调用js方法";
        webView.evaluateJavascript("javascript:(androidToJS2(" + "'" + params + "'" + "))", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
                Log.e("MainActivity", "value : " + value);
            }
        });
    }

    private void loadUrl(String url) {
        webView.loadUrl(url);
    }

    private class AndroidJS {
        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void hello(String msg) {
            Log.e("MainActivity", msg);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
