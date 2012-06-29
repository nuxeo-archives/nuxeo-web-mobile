var NXCordova = function() {
    if (!cordovaBase) {
      alert('nuxeo-cordova-wrapper added without cordovaBase.');
    }

    var Nx = {
      //Constants
      openURL: "NxOpenCommand.openURL"
    }

    function callCordova(command, param) {
      //Helper method
      var cordovaRef = window.PhoneGap || window.Cordova || window.cordova;
      cordovaRef.exec(command, param);
    }

    // Add a button to body element in case that the URL matches the specified
    (function(matchingUrls) {
      var found = false;
      for (var index in matchingUrls) {
        var pattern = matchingUrls[index];
        if (document.URL.match(pattern)) {
          found = true;
          break;
        }
      }
      if (!found) return;

      // - Add a small back btn form preview screens
      var body = document.getElementsByTagName("body").item(0);

      var containerDiv = document.createElement('div');
      containerDiv.style.position = 'fixed'
      containerDiv.style.zIndex = 1337
      containerDiv.style.backgroundColor = 'rgba(0,0,0,0.7)';
      containerDiv.style.border = '1px solid #111111'
      containerDiv.style.color = '#FFFFFF'
      containerDiv.style.fontWeight = 'bold'
      containerDiv.style.textShadow = '0 1px 1px #111111'
      containerDiv.style.borderRadius = '15px'
      containerDiv.style.boxShadow = '1px 1px 1px rgba(215,215,215,0.3)';

      var backBtn = document.createElement('div');
      backBtn.style.borderTop = '1px solid rgba(255, 255, 255, 0.3)'
      backBtn.style.padding = '3px 15px'
      backBtn.style.borderRadius = '15px'

      var backAnchor = document.createElement('a');
      backAnchor.style.textDecoration = 'none'
      backAnchor.style.color = '#eee'
      backAnchor.style.fontWeight = 'bold'
      backAnchor.style.fontFamily = 'Helvetica, Arial, sans-serif'
      backAnchor.setAttribute('href', 'javascript:history.back()');

      backAnchor.appendChild(document.createTextNode('Back'));
      backBtn.appendChild(backAnchor);
      containerDiv.appendChild(backBtn)

      if (body.firstChild) {
        body.insertBefore(containerDiv, body.firstChild)
      } else {
        body.appendChild(containerDiv);
      }
    })(['restAPI/preview'])

    // Module returned
    return {
      basePath: function() {
        return cordovaBase;
      },
      logout: function() {
        callCordova(Nx.openURL, cordovaBase + "index.html#page_servers_list");
      },
      openFileChooser: function(callback) {

        /* WIP 
        var cb = window.plugins.nxFileBrowser;
        if (cb != null) {
          cb.onClose = function() {
            if (callback) {
              callback();
            }
          }
          cb.showWebPage(cordovaBase + 'file-browser.html');
        } else {
          alert("cb null ...")
        }*/
      },
      uploadFile: function(filePath) {
        console.log('start uploading file')
        if (!document.URL.match("@folderish")) {
          alert('You must upload a file into a Folderish document. Operation aborded.')
          return;
        }

        var options = new FileUploadOptions();
        options.fileKey = "file:file";
        options.fileName = decodeURIComponent(filePath.substr(filePath.lastIndexOf('/') + 1));

        var params = new Object();
        options.params = params;

        var ft = new FileTransfer();
        ft.upload(filePath, document.URL, function(r) {
          if ("500" == r.responseCode) {
            alert('You are not allowed to create a new document in this Folderish.')
          } else {
            window.location.reload()
          }
        }, function(error) {
          alert("An error has occurred: Code = " + error.code);
          console.log("upload error source " + error.source);
          console.log("upload error target " + error.target);
        }, options);
      },
      handleOpenURL: function(filePath) {
        // XXX For now ... Directly upload file
        // Timeout it to let the application loading.
        setTimeout(function() {
          console.log('Settimeout called.' + filePath)
          NXCordova.uploadFile(filePath);
          console.log('uploadFile should be called.')
        }, 200);
      }
    };
  }();