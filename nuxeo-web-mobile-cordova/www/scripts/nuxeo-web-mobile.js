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

  //  $("body").toggle();
}

function handleOpenURL(url) {
  console.log("Try to open file url: " + url);

  copyFileLocally(url, function() {
    NXCordova.openFileChooser();
  })
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