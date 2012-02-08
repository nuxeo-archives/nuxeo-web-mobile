// For page are JSF redirection this action is directly exposed into the page
// see into view of the Root JAXRS element.
//$( document ).delegate(mobileHomePath, "pagebeforecreate", function() {
//    $.mobile.silentScroll(300);
//});


$(document).bind ("pageload", function (event, data) {
//  alert ("pageload data.url = " + data.url);
});

$(document).bind ("pageloadfailed", function (event, data) {
//  alert ("pageloadfailed data.url = " + data.url);
});

var notificationList = new Array();
var noticationDisplayInProgress = false;

function displayNotification(message) {
  if (noticationDisplayInProgress) {
    notificationList[length] = message;
  } else {
    doDisplayNotification(message);
  }
}

function doDisplayNotification(message) {
  noticationDisplayInProgress = true;
  var notificationItem = '<div class="quickNotification">' + message + '</div>';

  $(notificationItem)
    .insertBefore( $("body").children()[0] )
    .fadeIn('slow')
    .animate({opacity: 1.0}, 3000)
    .fadeOut('slow', function() {
      noticationDisplayInProgress = false;
      if (notificationList.length > 0) {
        message = notificationList.shift();
        displayNotification(message);
      }
      $(this).remove();
      
     });   
}
