<!DOCTYPE html>
<html>
<head>
  <title>
  <@block name="title">
    Nuxeo title
  </@block>
  </title>
  <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
  <!-- Force IE to use his real rendering engine -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

  <!-- Control layout on mobile browsers -->
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-capable" content="yes"/>
  <link rel="apple-touch-icon" href="${skinPath}/img/home_icon_57_57.jpg" media="screen and (resolution: 163dpi)"/>
  <link rel="apple-touch-icon" href="${skinPath}/img/home_icon_72_72.jpg" media="screen and (resolution: 132dpi)"/>
  <link rel="apple-touch-icon" href="${skinPath}/img/home_icon_114_114.jpg" media="screen and (resolution: 326dpi)"/>
  <link rel="apple-touch-startup-image" href="${skinPath}/img/splash_screen_1024_748.jpg"
        media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)"/>
  <link rel="apple-touch-startup-image" href="${skinPath}/img/splash_screen_768_1004.jpg"
        media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)"/>
  <link rel="apple-touch-startup-image" href="${skinPath}/img/splash_screen_320_460.jpg"
        media="screen and (max-device-width: 480px)"/>

  <link rel="stylesheet" href="${skinPath}/css/jquery.mobile-1.4.4.min.css"/>
  <link rel="stylesheet" href="${skinPath}/css/nuxeo-web-mobile.css"/>
<#if Context.getProperty('Cordova')??>
  <script
      src="${skinPath}/script/cordova-${Context.getProperty('Cordova').version}.0-${Context.getProperty('Cordova').device}.js"></script>
</#if>
  <script src="${skinPath}/script/jquery-1.11.1.min.js"></script>
  <script src="${skinPath}/script/jquery.mobile-1.4.4.min.js"></script>

  <!-- rewrite the URL after a redirect from JSF to have mobile URL,
       initial request accessible in the history -->
  <script type="text/javascript">
    var mobileHomePath = "${Root.path}";
    if ('${mobileURL}') {
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
