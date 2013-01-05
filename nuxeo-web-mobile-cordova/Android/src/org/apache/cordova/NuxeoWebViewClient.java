package org.apache.cordova;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.DroidGap;
import org.nuxeo.ecm.mobile.android.NuxeoWebApp;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;

/**
 * Nuxeo WebViewClient implementation that handle a list of javascript files to
 * be injected each page loaded. The package is moved to org.apache.cordova to
 * access WebView control directly.
 * 
 * @author arnaud
 */
public class NuxeoWebViewClient extends CordovaWebViewClient {

    private static final String TAG = "NuxeoWebViewClient";

    protected Set<String> filesToBeInject = new HashSet<String>();

    protected Map<String, String> filesContentCache = new HashMap<String, String>();

    protected DroidGap ctx;

    public NuxeoWebViewClient(DroidGap ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        injectFiles(url);
        loadJavascript(String.format("var cordovaBase = '%s'", "file://"
                + NuxeoWebApp.BASE_PATH));
    }

    public void addFileToLoad(String file) {
        filesToBeInject.add(file);
    }

    protected void injectFiles(String url) {
        if (url.startsWith("javascript")) {
            Log.d(TAG, "Do not try to inject javascript inlined as file.");
            return;
        }

        Log.i(TAG, "Inject files for: " + url);
        for (String file : filesToBeInject) {
            loadFile(file);
        }
    }

    protected void loadFile(String uri) {
        try {
            Log.i(TAG, "Try to load file: " + uri);

            if (!filesContentCache.containsKey(uri)) {
                filesContentCache.put(uri, new Scanner(
                        ctx.getAssets().open(uri)).useDelimiter("\\A").next());
            }
            String fileContent = filesContentCache.get(uri);
            loadJavascript(fileContent);
        } catch (IOException e) {
            Log.e(TAG, "Unable to find file: " + e.getMessage());
        }
    }

    public void loadJavascript(String js) {
        ctx.appView.loadUrl("javascript:" + js + ";");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // Override Cordova default behavior, to not clear history.
    }

}
