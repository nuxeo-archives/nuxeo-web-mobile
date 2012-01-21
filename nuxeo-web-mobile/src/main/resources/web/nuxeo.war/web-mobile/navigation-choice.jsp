<html>
  <head>
    <title>Nuxeo DM</title>
    <meta name="viewport" content="user-scalable=no,width=device-width"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <link rel="apple-touch-icon" href="icons/nuxeo_logo.png" />
    <link rel="apple-touch-startup-image" href="image/nuxeo_splash_screen.png" />
    <script src="scripts/web-mobile-functions.js"></script>
    <script>

var locationTmp;


function isChoiceAlreadyDone() {

  var answer = confirm("Do you want to use Mobile Application?");

  if (answer) {
    setCookie('ForceStandardNavigationEnabled', 'false', 1, '/');
  } else {
    setCookie('ForceStandardNavigationEnabled', 'true', 1, '/');
  }

  locationTmp = getURLParam("initialURLRequested");
  alert('Navigation: ' + locationTmp + ' \n Cookie: ' + getCookie('ForceStandardNavigationEnabled'));
  setTimeout('location.href="' + locationTmp + '"', 500);
}



    </script>
  </head>
  <body onload="isChoiceAlreadyDone()">
  </body>
</html>
