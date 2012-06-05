var isPhoneGapReady = false;
var db;

String.prototype.trim=function(){return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');};

function init() {
  //var requires = ['jquery-1.7.min', 'jquery.mobile-1.0.min', 'cordova-1.7.0'];
  document.addEventListener("deviceready", initAndGoToHome, false);

  $("#create_server_profile").bind ("click", function (event) {
    var formElements = $('#server_profile_form input');
    var len = formElements.length, i;
    var values = new Array();
    for (i = 0; i < len; i++) {
      values[i] = { name : formElements[i].name, value : formElements[i].value};
    }
    var server = new Server(values);

    server.store(function() {
      var isBackNavigation = true;
      navigateToServerList(isBackNavigation);
    });
  });
  
  // For BB <5.0
/*
  var intervalID = window.setInterval(function() {
    if (PhoneGap.available) {
      onDeviceReady();
    }
  });
*/
}


function initAndGoToHome() {
/*   window.clearInterval(intervalID); */
  isPhoneGapReady = true;
  
  initDatabase(function() {
    var isBackNavigation = false;
    navigateToServerList(isBackNavigation);
  });

  $("body").toggle();
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

function successTransaction() {
}

function navigateToServerList(isBackNavigation) {
  getServers(function (servers) {
    var len = servers.length, i;
    var html = "";
    for (i = 0; i < len; i++) {
      var server = servers[i];
      if ((!server.getURL()) || (!server.get('name'))) {
        alert('a not well formed server has been found');
      } else {
        alert('A good one found');
        html += '<li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">';
        html += '  <a href="' + server.getURL() + '">' + server.get('name') + '</a>';
        html += '</li>';
      }
    }
    $('#servers_list').append(html);

    $.mobile.changePage ("#page_servers_list", {
      transition : "slide",
      reloadPage: false,
      reverse: isBackNavigation
    });
  });
}


// TODO: Replace this by Backbone after data/layout splitting
function Server(_values) {
  if (_values == null) {
    alert('No element found to create server!!');
    return;
  }

  this.values = _values;
  var len = _values.length, i;

//  for (i = 0; i < len; i++) {
//    alert('formElements: ' + _values[i].name + "/" + _values[i].value);
//  }

  this.get = function(name) {
    var len = this.values.length, i;
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

    db.transaction( function (tx) {
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
  db.transaction( function (tx) {
    tx.executeSql('SELECT * FROM SERVER', [], function (tx, results) {
      var _servers = new Array();
      var len = results.rows.length, i;
      var html = "";
      if (len > 0) {
        for (i = 0; i < len; i++) {

          var formElement = new Array();
          formElement[0] = { name : 'name', value : results.rows.item(i).name};
          formElement[1] = { name : 'servername', value : results.rows.item(i).servername};
          formElement[2] = { name : 'login', value : results.rows.item(i).login};
          formElement[3] = { name : 'password', value : results.rows.item(i).password};
          formElement[4] = { name : 'contextpath', value : results.rows.item(i).contextpath};

          _servers[i] = new Server(formElement);
        }
      }
      callback(_servers);
    });
  }, errorTransaction2, successTransaction);
}