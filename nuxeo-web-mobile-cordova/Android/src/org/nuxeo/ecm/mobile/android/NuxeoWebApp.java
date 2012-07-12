package org.nuxeo.ecm.mobile.android;

import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.DroidGap;
import org.apache.cordova.NuxeoWebViewClient;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class NuxeoWebApp extends DroidGap {

    private static final String TAG = "NuxeoMobileApp";

    private static final String BASE_PATH = "/android_asset/www/";

    protected NuxeoWebViewClient nxWebView;

    @Override
    public void init() {
        nxWebView = new NuxeoWebViewClient(this);
        nxWebView.addFileToLoad("www/scripts/nuxeo-cordova-wrapper.js");
        
        this.init(new WebView(this), nxWebView, new CordovaChromeClient(this));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        loadUrl("file://" + BASE_PATH + "index.html");

        // Change UA
        String currentUA = appView.getSettings().getUserAgentString();
        appView.getSettings().setUserAgentString(
                currentUA + " Cordova/1.7 (android)");
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        super.loadUrl(String.format("javascript:var cordovaBase = '%s'",
                "file://" + BASE_PATH));
        Log.i(TAG, "LoadURL method called.");
    }

}
