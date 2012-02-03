<!DOCTYPE html>
<html>
<head>
  <title>
     <@block name="title">
       WebEngine Project
     </@block>
  </title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <!-- Force IE to use his real rendering engine -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <!-- Control layout on mobile browsers -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- Icon for Application into the springboard -->
    <link rel="apple-touch-icon" href="${skinPath}/img/nuxeo.png" />
    <!-- spash screen for this "application" -->
    <link rel="apple-touch-startup-image" href="${skinPath}/img/nuxeo_splash_screen.png" />

    <link rel="stylesheet" href="${skinPath}/css/jquery.mobile-1.0.css" />
    <link rel="stylesheet" href="${skinPath}/css/nuxeo-web-mobile.css" />
    <script src="${skinPath}/script/jquery-1.7.js"></script>
    <script src="${skinPath}/script/jquery.mobile-1.0.js"></script>


    <!-- rewrite the URL after a redirect from JSF to have mobile URL, 
         initial request accessible in the history -->
    <script>
        if ('${mobileURL}' != null) {
          window.history.pushState(null, 'Mobile URL', '${mobileURL}');
        } 
    </script>


    <@block name="stylesheets" />
    <@block name="header_scripts" />
  </head>

<body>
  <@block name="content">The Content</@block>

  <@block name="footer">
    <div data-role="footer">
      <div data-role="navbar">
        <ul>
          <li><a data-ajax="false" href="${This.previewURL}"><img alt="Home" src="${skinPath}/icons/footer_home.png" /></a></li>
          <li><a href="#"><img alt="Personal Workspace" src="${skinPath}/icons/footer_personalworkspace.png" /></a></li>
          <li><a href="#"><img alt="Profile" src="${skinPath}/icons/footer_profile.png" /></a></li>
          <li><a href="#"><img alt="Search" src="${skinPath}/icons/footer_search.png" /></a></li>
        </ul>
      </div>
    </div>
  </@block>

</body>
</html>
