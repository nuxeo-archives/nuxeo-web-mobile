<!DOCTYPE html>
<html>
<head>
  <title>
     <@block name="title">
       Nuxeo
     </@block>
  </title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <!-- Force IE to use his real rendering engine -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <!-- Control layout on mobile browsers -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link rel="apple-touch-icon" href="${skinPath}/icons/nuxeo_logo.png" />
    <link rel="apple-touch-startup-image" href="${skinPath}/img/nuxeo_splash_screen.png" />

    <link rel="stylesheet" href="${skinPath}/css/jquery.mobile-1.0.css" />
    <link rel="stylesheet" href="${skinPath}/css/nuxeo-web-mobile.css" />
    <script src="${skinPath}/script/jquery-1.7.js"></script>
    <script src="${skinPath}/script/jquery.mobile-1.0.js"></script>


    <!-- rewrite the URL after a redirect from JSF to have mobile URL, 
         initial request accessible in the history -->
    <script>
        var mobileHomePath = "${Root.path}";
        if ('${mobileURL}' != null) {
          window.history.pushState(null, 'Mobile URL', '${mobileURL}');
        } 
    </script>
    <script src="${skinPath}/script/nuxeo-navigation-jquerymobile.js"></script>


    <@block name="stylesheets" />
    <@block name="header_scripts" />
  </head>

<body>
  <@block name="content">
  The Content
  </@block>

</body>
</html>
