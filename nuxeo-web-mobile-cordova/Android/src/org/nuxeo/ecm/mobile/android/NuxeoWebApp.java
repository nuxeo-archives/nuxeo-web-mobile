package org.nuxeo.ecm.mobile.android;

import java.io.IOException;
import java.util.Scanner;

import org.apache.cordova.DroidGap;

import android.os.Bundle;
import android.util.Log;

public class NuxeoWebApp extends DroidGap {

    private static final String TAG = "NuxeoMobileApp";

    private static final String BASE_PATH = "/android_asset/www/";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file://" + BASE_PATH + "index.html");
        
        // Change UA
        String currentUA = appView.getSettings().getUserAgentString();
        appView.getSettings().setUserAgentString(
                currentUA + " Cordova/1.7 (android)");
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        loadFile("www/scripts/nuxeo-cordova-wrapper.js");
        Log.i(TAG, "LoadURL method called.");
    }

    @Override
    public void loadUrl(String url, int time) {
        super.loadUrl(url, time);
        Log.i(TAG, "LoadURL with time method called.");
    }

    protected void loadFile(String uri) {
        try {
            for(String file : this.fileList()) {
                Log.w(TAG, file);
            }
            String fileContent = new Scanner(getAssets().open(uri)).useDelimiter("\\A").next();
            sendJavascript(fileContent);
        } catch (IOException e) {
            Log.e(TAG, "Unable to find file: " + e.getMessage());
        }
    }

}
