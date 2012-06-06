var isPhoneGapReady = false;
var db;

String.prototype.trim = function() {
  return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};

function init() {
  document.addEventListener("deviceready", initAndGoToHome, false);

  $("#create_server_profile").bind("click", function(event) {
    var formElements = $('#server_profile_form input');
    var len = formElements.length,
      i;
    var values = new Array();
    for (i = 0; i < len; i++) {
      values[i] = {
        name: formElements[i].name,
        value: formElements[i].value
      };
    }
    var server = new Server(values);

    server.store(function() {
      var isBackNavigation = true;
      navigateToServerList(isBackNavigation);
    });
  });
}


function initAndGoToHome() { /*   window.clearInterval(intervalID); */
  isPhoneGapReady = true;

  initDatabase(function() {
    var isBackNavigation = false;
    navigateToServerList(isBackNavigation);
  });

  $("body").toggle();
}

function handleOpenURL(url) {
  console.log("Try to open file url: " + url);

  copyFileLocally(url, navigateToDropbox)
}

function navigateToDropbox() {
  window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fileSystem) {
    var dropbox = $("#dropbox_files");
    dropbox.empty();

    console.log("Try to read fileSystem local content");

    refreshFiles();
    changePage('dropbox');

    function refreshFiles() {
      dropbox.empty();
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
        }
        dropbox.append(html).find(".delete").click(function() {
          var li = $(this).parents('li');
          console.log(dropbox.html());
          if (!li) {
            console.error("Unable to find LI parent");
            return;
          }

          window.resolveLocalFileSystemURI("file://" + li.data('url'), function(fileEntry) {
            fileEntry.remove(function() {
              refreshFiles();
            }, function(evt) {
              alert("Unable to remove this file.")
              handleFileError(evt);
            });
          }, handleFileError);
          console.log("data-url: " + li.data('url'));
        });
        dropbox.listview('refresh');

      }, handleFileError);
    };
  }, handleFileError);
}


function copyFileLocally(url, callback) {
  // Try to resolve url parameters as a FS file
  window.resolveLocalFileSystemURI(url, function(fileEntry) {
    console.log("FileEntry gets: " + fileEntry.name);

    if (!fileEntry.isFile) {
      alert("Nuxeo mobile app do not allowed not regular file.")
      return;
    }

    // Try to request FS
    window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fileSystem) {
      console.log("Filesystem opened")
      console.log(console.log(fileSystem.root))

      console.log("Check if file already exists")
      // Try to find if file already exists
      var destinationPath = "file://" + encodeURI(fileSystem.root.fullPath + "/" + fileEntry.name);
      window.resolveLocalFileSystemURI(destinationPath, function(oldFile) {
        console.log("File already exists: " + oldFile.name);
        oldFile.remove(function() {
          console.log("Oldfile removed.")
          copyFile();
        }, handleFileError);
      }, function(evt) {
        // CopyFile only if it's file not found err
        if (evt.code == FileError.NOT_FOUND_ERR) {
          copyFile();
        } else {
          handleFileError(evt);
        }
      });

      function copyFile() {
        console.log("Try to copy file")
        fileEntry.copyTo(fileSystem.root, null, function(newEntry) {
          console.log("Copy successful: " + newEntry.fullPath);

          callback();
        }, handleFileError);
      }
    }, handleFileError);
  }, handleFileError);
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

window.onload = init;


//********************* DB MANAGEMENT *********************

function initDatabase(callback) {
  db = window.openDatabase("nuxeo", "1.0", "Nuxeo Client DB", 1000000);
  db.transaction(function(tx) {
    //tx.executeSql('DROP TABLE SERVER');
    tx.executeSql('CREATE TABLE IF NOT EXISTS SERVER (name, servername, contextpath, login, password)');
  }, errorTransaction1, callback);
}


function errorTransaction1(err) {
  alert('ERROR 1 during transaction');
  alert(err.message);
}

function errorTransaction2(err) {
  alert('ERROR 2 during transaction');
  alert(err.message);
}

function successTransaction() {}

function navigateToServerList(isBackNavigation) {
  getServers(function(servers) {
    var len = servers.length,
      i;
    var html = "";
    for (i = 0; i < len; i++) {
      var server = servers[i];
      if ((!server.getURL()) || (!server.get('name'))) {
        alert('a not well formed server has been found');
      } else {
        html += '<li>';
        html += '  <a href="' + server.getURL() + '">' + server.get('name') + '</a>';
        html += '</li>';
      }
    }
    $('#servers_list').append(html);

    changePage('page_servers_list', isBackNavigation);
    handleOpenURL('file://localhost/Users/arnaud/Library/Application%20Support/iPhone%20Simulator/5.1/Applications/42437C8E-F7A6-48F6-A368-751B61FE0E37/Documents/Inbox/PDF%20Document-8.pdf');
  });
}

function changePage(id, isBackNavigation) {
  var _isBackInNavigation = isBackNavigation || false;
  $.mobile.changePage('#' + id, {
    transition: "slide",
    reloadPage: false,
    reverse: _isBackInNavigation
  });
}

// TODO: Replace this by Backbone after data/layout splitting

function Server(_values) {
  if (_values == null) {
    alert('No element found to create server!!');
    return;
  }

  this.values = _values;
  var len = _values.length,
    i;

  //  for (i = 0; i < len; i++) {
  //    alert('formElements: ' + _values[i].name + "/" + _values[i].value);
  //  }
  this.get = function(name) {
    var len = this.values.length,
      i;
    for (i = 0; i < len; i++) {
      if (this.values[i].name == name) {
        if (!this.values[i].value || this.values[i].value.trim() == '') {
          return null;
        } else {
          return this.values[i].value;
        }
      }
    }
    return null;
  };

  this.getURL = function() {
    if ((!this.get('servername')) || (!this.get('contextpath'))) {
      return null;
    } else {
      return this.get('servername') + this.get('contextpath');
    }
  }

  this.store = function(callback) {
    var server = this;
    // remove profile displayed in the server list
    $('#servers_list').empty();

    db.transaction(function(tx) {
      var query = 'INSERT INTO SERVER (name, servername, contextpath, login, password) VALUES (?, ?, ?, ?, ?)'
      var data = [server.get('name'), server.get('servername'), server.get('contextpath'), server.get('login'), server.get('password')];
      alert("data: " + data);

      tx.executeSql(query, data, function() {
        callback();
      });
    }, errorTransaction1, successTransaction);
  }
}


function getServers(callback) {
  db.transaction(function(tx) {
    tx.executeSql('SELECT * FROM SERVER', [], function(tx, results) {
      var _servers = new Array();
      var len = results.rows.length,
        i;
      var html = "";
      if (len > 0) {
        for (i = 0; i < len; i++) {

          var formElement = new Array();
          formElement[0] = {
            name: 'name',
            value: results.rows.item(i).name
          };
          formElement[1] = {
            name: 'servername',
            value: results.rows.item(i).servername
          };
          formElement[2] = {
            name: 'login',
            value: results.rows.item(i).login
          };
          formElement[3] = {
            name: 'password',
            value: results.rows.item(i).password
          };
          formElement[4] = {
            name: 'contextpath',
            value: results.rows.item(i).contextpath
          };

          _servers[i] = new Server(formElement);
        }
      }
      callback(_servers);
    });
  }, errorTransaction2, successTransaction);
}
