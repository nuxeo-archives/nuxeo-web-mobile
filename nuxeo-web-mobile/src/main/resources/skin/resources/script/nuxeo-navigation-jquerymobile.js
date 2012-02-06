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

function displayNotification(message) {
  var notificationItem = '<div class="quick-notification">' + message + '</div>';

  $(notificationItem)
    .insertAfter( $("body") )
    .fadeIn('slow')
    .animate({opacity: 1.0}, 3000)
    .fadeOut('slow', function() {
      $(this).remove();
     });   
}