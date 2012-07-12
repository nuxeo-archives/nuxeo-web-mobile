package org.nuxeo.ecm.mobile.android;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cordova.DirectoryManager;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.sax.StartElementListener;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class OpenCommandPlugin extends Plugin {

    private static final String TAG = "OpenCommandPlugin";

    protected enum Actions {
        openUrl, openServer, presentingDocument
    };

    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId) {
        Status status = Status.NO_RESULT;
        try {
            if (Actions.openUrl.toString().equals(action)) {
                String url = data.getString(0);

                status = openUrl(url);
            } else if (Actions.openServer.toString().equals(action)) {
                String url = data.getString(0);
                String username = data.getString(1);
                String password = data.getString(2);

                status = openServer(url, username, password);
            } else if (Actions.presentingDocument.toString().equals(action)) {
                String documentUrl = data.getString(0);
                String mimetype = data.getString(1);
                status = presentingDocument(documentUrl, mimetype);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            status = Status.JSON_EXCEPTION;
        }
        return new PluginResult(status);
    }

    /**
     * Method used to open any URL. Useful to request a local file (file://)
     * when the WenContext is browsing a http resource.
     * 
     * @param url complete url with protocol.
     */
    protected Status openUrl(String url) {
        return loadUrl(url, new HashMap<String, String>());
    }

    /**
     * Method to handle basic authentification on a Nuxeo base URL
     * (http://localhost:8080/nuxeo for instance).
     * 
     * @param url Nuxeo base server url.
     * @param username that will be passed as Basic Auth
     * @param password that will be passed as Basic Auth
     */
    protected Status openServer(String url, String username, String password) {
        Map<String, String> extraHeaders = new HashMap<String, String>();
        String credentials = username + ":" + password;
        String basicAuth = "basic "
                + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
        extraHeaders.put("authorization", basicAuth);

        url += "/site/mobile";
        return loadUrl(url, extraHeaders);
    }

    /**
     * Method to send document to another application, or save it somewhere with
     * Android.
     * 
     * @param documentUrl of an existing document.
     * @param downloadUrl
     */
    protected Status presentingDocument(String documentUrl, String mimetype) {
        Log.i(TAG, "Try to presenting url: " + documentUrl + " with mimetype: " + mimetype);

        // Create a file object to ensure file is downloaded.
        File document = new File(documentUrl.substring(7, documentUrl.length()));
        if (!document.exists()) {
            Log.e(TAG, "File doesn't exist: " + documentUrl);
            return Status.MALFORMED_URL_EXCEPTION;
        }

        // Create an intent with mimetype
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (mimetype == null || "".equals(mimetype.trim())) {
            intent.setData(Uri.fromFile(document));
        } else {
            intent.setDataAndType(Uri.fromFile(document), mimetype.trim());
        }

        // Open Package Manager
        PackageManager packageManager = ctx.getApplicationContext().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                intent, 0);
        if (activities.size() > 0) {
            ctx.startActivity(intent);
            return Status.OK;
        } else {
            Log.d(TAG, "No application associated with this intent.");
            webView.loadUrl("javascript:alert('No application associated with this kind of document.');");
            return Status.NO_RESULT;
        }
    }

    private Status loadUrl(String url, Map<String, String> extraHeaders) {
        webView.loadUrl(url, extraHeaders);
        return Status.OK;
    }
}
