var Cordova = function() {


    function changePage(id, isBackNavigation) {
      var _isBackInNavigation = isBackNavigation || false;
      $.mobile.changePage(id, {
        transition: "slide",
        reloadPage: false,
        reverse: _isBackInNavigation
      });
    }

    function handleFileError(evt) {
      switch (evt.code) {
      case FileError.NOT_FOUND_ERR:
        console.error("File not found");
        break;
      case FileError.SECURITY_ERR:
        console.error("Security error");
        break;
      case FileError.ABORT_ERR:
        console.error("Abort error");
        break;
      case FileError.NOT_READABLE_ERR:
        console.error("Not readable file error");
        break;
      case FileError.ENCODING_ERR:
        console.error("Encoding error");
        break;
      case FileError.NO_MODIFICATION_ALLOWED_ERR:
        console.error("Not allowed to modificate");
        break;
      case FileError.INVALID_STATE_ERR:
        console.error("Invalide state error");
        break;
      case FileError.SYNTAX_ERR:
        console.error("Syntax error");
        break;
      case FileError.INVALID_MODIFICATION_ERR:
        console.error("Invalid modification error");
        break;
      case FileError.QUOTA_EXCEEDED_ERR:
        console.error("Quota exeeded error");
        break;
      case FileError.TYPE_MISMATCH_ERR:
        console.error("Type mismatch error");
        break;
      case FileError.PATH_EXISTS_ERR:
        console.error("Path exists error");
        break;
      default:
        console.log("Unknown error");
      }
    }


    function refreshFiles(dropbox) {
      window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fileSystem) {
        //dropbox.empty();
        console.log("Start refreshing files");
        fileSystem.root.createReader().readEntries(function(entries) {
          var html = ""
          for (var i = 0; i < entries.length; i++) {
            var entry = entries[i];
            if (!entry.isFile) {
              continue;
            }

            // class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c"
            html += '<li data-url="' + encodeURI(entry.fullPath) + '">';
            html += entry.name;
            html += '<a href="#" class="delete">delete</a>'
            html += '</li>';

            console.log(entry.name)
          }
          return; //XXX

          dropbox.append(html).find(".delete").click(function() {
            var li = $(this).parents('li');
            console.log(html);
            if (!li) {
              console.error("Unable to find LI parent");
              return;
            }

            window.resolveLocalFileSystemURI("file://" + li.data('url'), function(fileEntry) {
              fileEntry.remove(function() {
                refreshFiles(dropbox);
              }, function(evt) {
                alert("Unable to remove this file.")
                handleFileError(evt);
              });
            }, handleFileError);
            console.log("data-url: " + li.data('url'));
          });
          //dropbox.listview('refresh');

        }, handleFileError);
      }, handleFileError);
    };

    return {
      openFileChooser: function() {
        window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fileSystem) {
          var test = "file://localhost/Users/arnaud/Library/Application%20Support/iPhone%20Simulator/5.1/Applications/B9B30F2F-FB99-4571-8B8F-4B48A6DB9C0F/nuxeo-web-mobile.app/www/index.html?ui-page=dropbox";
          //changePage(test);
          //var dropbox = $("#dropbox_files");
          //dropbox.empty();
          console.log("Try to read fileSystem local content");

          refreshFiles({});

        }, handleFileError);
      }
    };
  }();
