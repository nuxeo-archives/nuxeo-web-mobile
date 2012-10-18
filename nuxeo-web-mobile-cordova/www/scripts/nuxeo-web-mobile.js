var isPhoneGapReady = false;
var db;

String.prototype.trim = function() {
  return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};

String.prototype.trailingSlash = function() {
  return this.replace(/\/$/, '');
};

function init() {
  $('#server_profile_form').submit(function(evt) {
    evt.preventDefault();
    return false;
  })

  $("#create_server_profile").bind("click", function(event) {
    var formElementsArray = $('#server_profile_form').serializeArray();
    var formElements = {}
    for (var index in formElementsArray) {
      var elt = formElementsArray[index];
      formElements[elt.name] = elt.value;
    }

    var server = ServerUtils(formElements);
    server.store(function() {
      refreshServers()
    });
  });

  $('#btnEdit').click(function() {
    var that = $(this)
    $('#servers_list .btnDelete').fadeToggle();
  });

  (function() {
    // Use deviceReady Cordova event to init the app and fallback
    // in case that the event is never sent.
    console.log("Try to start Cordova.")
    var deviceIsReady = false;
    document.addEventListener("deviceready", function() {
      console.log("Start using Cordova.")
      deviceIsReady = true;
      initAndGoToHome();
    }, false);

    setTimeout(function() {
      if (!deviceIsReady) {
        console.log("Start using fallback.")
        initAndGoToHome();
      }
    }, 500)
  }())
}


function initAndGoToHome() { /*   window.clearInterval(intervalID); */
  isPhoneGapReady = true;
  navigateToServerList(false);
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
      console.log(fileSystem.root)

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
// XXX Should be moved to localStorage ...

function refreshServers(servers) {
  if (!servers) {
    ServerUtils().getAllServer(function(servers) {
      refreshServers(servers);
    });
    return;
  }

  var len = servers.length;

  var html = "";
  for (var i = 0; i < len; i++) {
    var server = ServerUtils(servers[i]);
    if ((!server.getURL()) || (!server.get('name'))) {
      alert('a not well formed server has been found');
    } else {
      html += '<li>';
      html += '  <a class="link" href="javascript:NXCordova.openServer(\'' + server.getURL() + '\', \'' + server.get('login') + '\',\'' + server.get('password') + '\')" data-icon="delete">' + server.get('name') + '</a>';
      html += '  <span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span>'; //Hack to force arrow icon.
      html += '  <a class="btnDelete" style="display:none;" href="#" data-icon="delete">Delete</a>';
      html += '</li>';
    }
  }

  $('#servers_list').html(html);
  $('#servers_list .btnDelete').click(function() {
    var that = $(this);
    var serverName = that.parents('li').find('a.link').html();
    ServerUtils({
      name: serverName
    }).remove(function() {
      ServerUtils().getAllServer(function(servers) {
        refreshServers(servers);
      });
    });
  })
  // Not sure about this timeout ...
  setTimeout(function() {
    $('#servers_list').listview('refresh')
  }, 50)
}

function navigateToServerList(isBackNavigation) {
  ServerUtils().getAllServer(function(servers) {
    refreshServers(servers);
    changePage('page_servers_list', isBackNavigation);
    if (servers.length == 0) {
      // If there is no servers added ... Go to the serverCreationPage
      changePage('page_server_creation');
    }
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

var ServerUtils = function(server) {
    var _values = $.extend({}, server);
    var _ls = window.localStorage;
    //_ls.clear();
    var Constant = {
      SERVERS_KEY: "nx_servers"
    }

    function setServers(servers) {
      _ls.setItem(Constant.SERVERS_KEY, JSON.stringify(servers));
    }

    function getServers() {
      var servers = _ls.getItem(Constant.SERVERS_KEY);
      return servers ? JSON.parse(servers) : null;
    }
    
    function buildDefaultServerList() {
        return [{
          name: "demo.nuxeo.com",
          servername: "http://demo.nuxeo.com",
          login: "Administrator",
          password: "Administrator",
          contextPath: "/nuxeo"
        }]
    }

    return {
      get: function(name) {
        var value = _values[name];
        return (!value || !value.trim()) ? null : value;
      },
      getAllServer: function(callback) {
        var servers = getServers()
        if (!servers) {
          servers = buildDefaultServerList();
          setServers(servers)
        }

        if (callback) callback(servers)
        else return servers;
      },
      getURL: function() {
        return this.get('servername') + this.get('contextpath');
      },
      store: function(callback) {
        var servers = this.getAllServer();

        var newServer = {
          name: _values.name ? _values.name.trim() : null,
          servername: _values.servername ? _values.servername.trim() : null,
          login: _values.login ? _values.login.trim() : null,
          password: _values.password ? _values.password.trim() : null,
          contextpath: _values.contextpath ? _values.contextpath.trim().trailingSlash() : null
        };

        // Check if already exists
        var replaceIndex = -1;
        $.each(servers, function(index, server) {
          if (server.name == newServer.name) {
            replaceIndex = index;
            return false;
          }
        });

        if (replaceIndex >= 0) {
          servers[replaceIndex] = newServer;
        } else {
          servers.push(newServer);
        }
        setServers(servers)

        if (callback) callback();
      },
      remove: function(callback) {
        var servers = this.getAllServer();
        var foundIndex = -1;
        $.each(servers, function(index, server) {
          if (server.name == _values.name) {
            foundIndex = index;
            return false;
          }
        });

        if (foundIndex >= 0) {
          servers.splice(foundIndex, 1);
          setServers(servers)
          callback()
        }
      }
    }
  };