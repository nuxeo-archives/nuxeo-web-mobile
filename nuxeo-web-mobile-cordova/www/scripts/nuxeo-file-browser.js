(function() {
  alert("loading ...");
  document.addEventListener("deviceready", function() {
    alert("file-deviceready");
    refreshFiles();
  });

  function refreshFiles() {
    window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fileSystem) {
      var dropbox = $("#dropbox_files");
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
    }, handleFileError);
  };
}())