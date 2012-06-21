var NXCordova = function() {
    if (!cordovaBase) {
      alert('variable cordovaBase not existing');
    }

    return {
      basePath: function() {
        return cordovaBase;
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
        }, nil, options);
      },
      handleOpenURL: function(filePath) {
        // XXX For now ... Directly upload file
        // Timeout it to let the application loading.
        setTimeout(function() {
          console.log('Settimeout called.' + filePath)
          NXCordova.uploadFile(filePath);
          console.log('uploadFile should be called.')
        }, 20);
      }
    };
  }();