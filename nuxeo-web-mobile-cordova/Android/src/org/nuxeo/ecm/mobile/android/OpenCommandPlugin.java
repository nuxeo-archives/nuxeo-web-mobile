package org.nuxeo.ecm.mobile.android;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Base64;
import android.util.Log;

public class OpenCommandPlugin extends Plugin {

    private static final String TAG = "OpenCommandPlugin";

    protected enum Actions {
        openUrl, openServer
    };

    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId) {
        Map<String, String> extraHeaders = new HashMap<String, String>();
        try {
            String url = data.getString(0);
            if (Actions.openUrl.toString().equals(action)) {
                Log.i(TAG, "openUrl called.");
                //Nothing to do yet.
            } else if (Actions.openServer.toString().equals(action)) {
                Log.i(TAG, "openServer called.");

                String username = data.getString(1);
                String password = data.getString(2);

                String credentials = username + ":" + password;
                String basicAuth = "basic "
                        + Base64.encodeToString(credentials.getBytes(),
                                Base64.DEFAULT);
                extraHeaders.put("authorization", basicAuth);

                url += "/site/mobile";
            }
            
            webView.loadUrl(url, extraHeaders);
            return new PluginResult(Status.OK);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return new PluginResult(Status.JSON_EXCEPTION);
        }
    }
}
