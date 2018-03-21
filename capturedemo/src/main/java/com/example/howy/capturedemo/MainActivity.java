package com.example.howy.capturedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {


    private WebView mWebview;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.rootView);
        initWebView(this);
    }

    public void capture(View view) {
        view.setVisibility(View.GONE);
        mWebview.setDrawingCacheEnabled(true);
        mWebview.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(mWebview.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        //保存图片到在本地
        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                Log.e("ssh", sdRoot.toString());
                File file = new File(sdRoot, "demo.png");
                fos = new FileOutputStream(file);
                Toast.makeText(this, "保存成功 ", Toast.LENGTH_SHORT).show();
            } else
                throw new Exception("创建文件失败!");
            //压缩图片 30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rootView.destroyDrawingCache();
    }

    private void initWebView(final Activity activity) {
        mWebview = (WebView) findViewById(R.id.webview);

        WebSettings settings = mWebview.getSettings();

        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        //允许SessionStorage/LocalStorage存储
        settings.setDomStorageEnabled(true);
        //禁用放缩
//        settings.setDisplayZoomControls(false);
//        settings.setBuiltInZoomControls(false);
        //禁用文字缩放（解决视图拉伸问题）
        settings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        settings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(this.getDir("appcache", 0).getPath());
        //不保存密码
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);    //支持javascript
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);     //缩放至屏幕的大小
        settings.setAllowFileAccess(true);      //设置可以访问文件
//        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
        settings.setSupportZoom(true);    //设置支持缩放
        settings.setLoadsImagesAutomatically(true);    //设置自动加载图片
//        settings.setBlockNetworkImage(true);    //设置网页在加载的时候暂时不加载图片
//        settings.setAppCachePath("");   //设置缓存路径
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);   //设置缓存模式


        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        mWebview.loadUrl("http://m.jiadianxi.com/");

    }
}

