alert('toto');
var isPhoneGapReady = false;
var db;
var servers = new Array();


function init() {
  document.addEventListener("deviceready", initServerList, false);
    
  // For BB <5.0
/*
  var intervalID = window.setInterval(function() {
    if (PhoneGap.available) {
      onDeviceReady();
    }
  });
*/
}


function initServerList() {
/*   window.clearInterval(intervalID); */
  isPhoneGapReady = true;
  alert('phone gap ready');
  
  initDatabase();
  
}

function initDatabase() {
  db = window.openDatabase("nuxeo", "1.0", "Nuxeo Client DB", 1000000);
  db.transaction(initDB, errorTransaction1, navigateToServerList);
}

window.onload = init;


//********************* DB MANAGEMENT *********************

function initDB(tx) {
  tx.executeSql('DROP TABLE IF EXISTS SERVER');
  tx.executeSql('CREATE TABLE SERVER (name, servername, contextpath, login, password)');
  tx.executeSql('INSERT INTO SERVER (name, servername, contextpath, login, password) VALUES ("Local", "http://localhost:8080", "/nuxeo", "Administrator", "Administrator")');
  tx.executeSql('INSERT INTO SERVER (name, servername, contextpath, login, password) VALUES ("Local2", "http://127.0.0.1:8080", "/nuxeo", "Administrator", "Administrator")');
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



/**
  navigate to the last page before application close
*/
function navigateToServerList() {
  db.transaction( function (tx) {
    tx.executeSql('SELECT * FROM SERVER', [], function (tx, results) {
      var len = results.rows.length, i;
      var html = "";
      for (i = 0; i < len; i++) {
        var url = results.rows.item(i).servername + results.rows.item(i).contextpath;
        html += '<li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c"><a href="' + url + '">'+results.rows.item(i).name+'</a></li>';
      }
      $('#servers_list').append(html);
      $.mobile.changePage ($("#server_list"));
    });
  }, errorTransaction2, successTransaction);
}

