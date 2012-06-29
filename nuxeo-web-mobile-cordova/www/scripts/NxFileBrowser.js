// Based on work from Jesse MacFadyen, Nitobi under MIT License with their ChildBrowser plugins.


(function() {

var cordovaRef = window.PhoneGap || window.Cordova || window.cordova; // old to new fallbacks

function NxFileBrowser() {
    // Does nothing
}

// Callback when the location of the page changes
// called from native
NxFileBrowser._onLocationChange = function(newLoc)
{
    window.plugins.NxFileBrowser.onLocationChange(newLoc);
};

// Callback when the user chooses the 'Done' button
// called from native
NxFileBrowser._onClose = function()
{
    window.plugins.NxFileBrowser.onClose();
};

// Callback when the user chooses the 'open in Safari' button
// called from native
NxFileBrowser._onOpenExternal = function()
{
    window.plugins.NxFileBrowser.onOpenExternal();
};

// Pages loaded into the NxFileBrowser can execute callback scripts, so be careful to
// check location, and make sure it is a location you trust.
// Warning ... don't exec arbitrary code, it's risky and could fuck up your app.
// called from native
NxFileBrowser._onJSCallback = function(js,loc)
{
    // Not Implemented
    //window.plugins.NxFileBrowser.onJSCallback(js,loc);
};

/* The interface that you will use to access functionality */

// Show a webpage, will result in a callback to onLocationChange
NxFileBrowser.prototype.showWebPage = function(loc)
{
    cordovaRef.exec("NxFileBrowserCommand.showWebPage", loc);
};

// close the browser, will NOT result in close callback
NxFileBrowser.prototype.close = function()
{
    cordovaRef.exec("NxFileBrowserCommand.close");
};

// Not Implemented
NxFileBrowser.prototype.jsExec = function(jsString)
{
    // Not Implemented!!
    //PhoneGap.exec("NxFileBrowserCommand.jsExec",jsString);
};

// Note: this plugin does NOT install itself, call this method some time after deviceready to install it
// it will be returned, and also available globally from window.plugins.NxFileBrowser
NxFileBrowser.install = function()
{
    if(!window.plugins) {
        window.plugins = {};
    }
        if ( ! window.plugins.nxFileBrowser ) {
        window.plugins.nxFileBrowser = new NxFileBrowser();
    }
};

if (cordovaRef && cordovaRef.addConstructor) {
    cordovaRef.addConstructor(NxFileBrowser.install);
} else {
    console.log("NxFileBrowser Cordova Plugin could not be installed.");
    return null;
}

})();