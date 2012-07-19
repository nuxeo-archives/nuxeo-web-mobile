# Nuxeo Web Mobile device native apps using Cordova 

These application allow you to access your Nuxeo server directly into your smartphone. You can download file, previewing them, upload new file / picture directly from your mobile device.
For now, we are working with Cordova 1.7.0.


## How to build

### iOS version (only OS X)

To build iOS native app, you need to install [Apache Cordova v1.7.0](https://github.com/apache/incubator-cordova-ios/zipball/1.7.0)  into Xcode. You can find lots of useful informations on their [github webpage](https://github.com/apache/incubator-cordova-ios).

* Install [Xcode](http://itunes.apple.com/fr/app/xcode/id497799835?mt=12).
* Check that `nuxeo-web-mobile-cordova/www/index.html` is referencing the right cordova javascript file. Line 13, you should have `cordova-1.7.0-iOS.js`.
* Open Xcode project:

    $ cd nuxeo-web-mobile-cordova/iOS
    $ open nuxeo-web-mobile.xcodeproj

Build, run, that's all.

### Android version

To build Android native (API level 10, at least), you do not need to install [Apache Cordova](http://incubator.apache.org/cordova/) it's embedded.

* Install [Eclipse](http://www.eclipse.org/) with [Android SDK](http://developer.android.com/sdk/index.html).

* Create a link between the shared www folder, and www that will be expect by Eclipse.

    $ cd nuxeo-web-mobile-cordova/Android/assets
    $ ln -s ../../www www

* Check that `nuxeo-web-mobile-cordova/www/index.html` is referencing the right cordova javascript file. Line 13, you should have `cordova-1.7.0-android.js`.
* Import the project into you Eclipse as an Android application.
* Compile, run, that's all.

## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>

