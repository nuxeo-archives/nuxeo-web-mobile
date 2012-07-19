var NXCordova = function() {
    //if (!cordovaBase) {
    //  alert('nuxeo-cordova-wrapper added without cordovaBase.');
    //}
    var _ls = window.localStorage;

    var Constants = {
      fileStorageKey: 'nx_saved_file'
    }
    var Plugins = {
      //Constants
      openURL: {
        command: 'NxOpenCommand',
        method: 'openURL'
      },
      openServer: {
        command: 'NxOpenCommand',
        method: 'openServer'
      },
      uploadFile: {
        command: 'NxOpenCommand',
        method: 'askUser'
      },
      presentingDocument: {
        command: 'NxOpenCommand',
        method: 'presentingDocument'
      }
    }

    function callCordova(command, param) {
      //Helper method
      var cordovaRef = window.PhoneGap || window.Cordova || window.cordova;
      //cordovaRef.exec(command, param);
      console.log('try to call.')
      cordovaRef.exec(function() {
        console.log('success')
      }, function() {
        console.log('error')
      }, command.command, command.method, param)
    }

    function addBaseURLIfNeeded(url) {
      url = encodeURI(url);
      if (url.match("^http:")) {
        return url;
      }

      var baseUrl = document.location.protocol + '//' + document.location.hostname;
      if (document.location.port) {
        baseUrl += ":" + document.location.port;
      }

      if (url.match("^/")) {
        return baseUrl + url;
      } else {
        return baseUrl + "/" + url;
      }
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
      backAnchor.setAttribute('href', 'javascript:history.back();');

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
      openServer: function(url, username, password) {
        $.mobile.showPageLoadingMsg();

        var params = [url, username, password];
        callCordova(Plugins.openServer, params);
      },
      logout: function() {
        callCordova(Plugins.openURL, [cordovaBase + "index.html#page_servers_list"]);
      },
      downloadFromURL: function(url) {
        console.log('Try to download: ' + url);
        function failFS() {
          alert('Unable to get the localfilesystem ...')
        }

        function downloadIntoFile(folder) {
          var urlLength = url.length;
          var baseUrl = url;
          if (url.lastIndexOf("?") > -1) {
            urlLength = url.lastIndexOf("?");
            baseUrl = url.substring(0, urlLength);
          }
          var filename = baseUrl.substring(baseUrl.lastIndexOf('/') + 1, urlLength).replace(/\s+/g, '_');

          folder.getFile(filename, {
            create: true,
            exclusive: false
          }, function(entry) {
            //alert('pdf created.')
            $.mobile.showPageLoadingMsg();

            var ft = new FileTransfer();
            var _url = addBaseURLIfNeeded(url);
            ft.download(_url, entry.fullPath, function(entry) {
              $.mobile.hidePageLoadingMsg();
              console.log("download complete: " + entry.fullPath);

              // Read mimetype from parameter
              var mimetype = "";
              var matches = _url.match(/mimetype=([a-z\./]+)/i);
              if (matches) {
                mimetype = matches[1];
                console.log('Mimetype found: ' + mimetype);
              }
              //Presenting downloaded document
              callCordova(Plugins.presentingDocument, [encodeURI(entry.fullPath), mimetype]);
            }, function(error) {
              $.mobile.hidePageLoadingMsg();
              alert('An error occured while trying to download file.')
              console.log("download error source " + error.source);
              console.log("download error target " + error.target);
              console.log("upload error code" + error.code);
            });
          }, failFS);
        }

        // Get an arbitrary downloads folder to store dowloaded file temporaly
        window.requestFileSystem(LocalFileSystem.TEMPORARY, 0, function(fileSystem) {
          //alert('tmp created.')
          fileSystem.root.getDirectory("downloads", {
            create: true,
            exclusive: false
          }, downloadIntoFile, failFS);
        }, failFS);
      },
      openUploadChooser: function(callback) {
        var file = _ls.getItem(Constants.fileStorageKey);
        var currentFile = file ? JSON.parse(file) : null;
        var args = [];
        if (currentFile) {
          args.push(currentFile.fileName)
        }

        callCordova(Plugins.uploadFile, args)
      },
      takePicture: function(onSuccess, onFail) {
        var _onFail = onFail ||
        function onFail(message) {
          alert('Failed because: ' + message);
        };
        var _onSuccess = onSuccess || this.uploadFile;

        navigator.camera.getPicture(_onSuccess, _onFail, {
          quality: 65,
          allowEdit: true,
          destinationType: Camera.DestinationType.FILE_URI
        });
      },
      openLibrary: function(onSuccess, onFail) {
        var _onFail = onFail ||
        function onFail(message) {
          alert('Failed because: ' + message);
        };
        var _onSuccess = onSuccess || this.uploadFile

        navigator.camera.getPicture(_onSuccess, _onFail, {
          quality: 65,
          destinationType: navigator.camera.DestinationType.FILE_URI,
          sourceType: navigator.camera.PictureSourceType.SAVEDPHOTOALBUM
        });
      },
      uploadFile: function(filePath) {
        console.log("Asset: " + filePath);
        // Ensure to be in a Folderish path
        console.log('start uploading file')
        if (!document.URL.match("@folderish")) {
          alert('You must upload a file into a Folderish document. Operation aborded.')
          return;
        }

        // If no filePath, use the one in localStorage
        if (!filePath) {
          var currentFile = JSON.parse(_ls.getItem(Constants.fileStorageKey));
          filePath = currentFile.filePath;
        }

        // Some upload metadata
        var options = new FileUploadOptions();
        options.fileKey = "file:file";
        options.fileName = decodeURIComponent(filePath.substr(filePath.lastIndexOf('/') + 1));

        var params = new Object();
        options.params = params;

        $.mobile.showPageLoadingMsg();
        var ft = new FileTransfer();
        ft.upload(filePath, document.URL, function(r) {
          if ("500" == r.responseCode) {
            alert('You are not allowed to create a new document in this Folderish.')
          } else {
            window.location.reload()
          }
        }, function(error) {
          $.mobile.hidePageLoadingMsg();
          alert("An error has occurred: Code = " + error.code);
          console.log("upload error source " + error.source);
          console.log("upload error target " + error.target);
        }, options);
      },
      handleOpenURL: function(filePath, fileName) {
        if (document.URL.match('^file:')) {
          alert('You should be connected to a server before trying to upload file.');
          return;
        }

        // Timeout it to let the application loading.
        setTimeout(function() {
          //NXCordova.uploadFile(filePath);
          _ls.setItem(Constants.fileStorageKey, JSON.stringify({
            'filePath': filePath,
            'fileName': fileName
          }));
          alert('Document ' + fileName + ' is ready to be uploaded.');
          // XXX need to save it locally ??
        }, 200);
      }
    };
  }();