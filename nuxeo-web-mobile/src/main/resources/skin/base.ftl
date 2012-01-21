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

    <!-- Icon for Application into the springboard -->
    <link rel="apple-touch-icon" href="${skinPath}/img/nuxeo_logo.png" />
    <!-- spash screen for this "application" -->
    <link rel="apple-touch-startup-image" href="${skinPath}/img/nuxeo_splash_screen.png" />

    <link rel="stylesheet" href="${skinPath}/css/jquery.mobile-1.0.css" />
    <script src="${skinPath}/script/jquery-1.7.js"></script>
    <script src="${skinPath}/script/jquery.mobile-1.0.js"></script>


    <@block name="stylesheets" />
    <@block name="header_scripts" />
  </head>

<body>
 <@block name="content">The Content</@block>
</body>
</html>
