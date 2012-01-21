<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- Nuxeo Enterprise Platform, svn $Revision: 22925 $ -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="org.nuxeo.ecm.mobile.ApplicationDefinitionService"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    Framework.getProperty(ApplicationDefinitionService.class);
%>
<html>
  <head>
    <title>Nuxeo DM</title>
    <meta name="viewport" content="user-scalable=no,width=device-width"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <link rel="apple-touch-icon" href="icons/nuxeo_logo.png" />
    <link rel="apple-touch-startup-image" href="image/nuxeo_splash_screen.png" />
    <script src="script/web-mobile-functions.js"></script>
    <script>

var locationTmp;


function isChoiceAlreadyDone() {

  var answer = confirm("Do you want to use Mobile Application?");

  if (answer) {
    setCookie('applicationSelected', 'mobile', 1, '/');
  } else {
    setCookie('applicationSelected', '', 1, '/');
  }

  locationTmp = getURLParam("initialURLRequested");
  alert('Navigation: ' + locationTmp + ' \n Cookie: ' + getCookie('ForceStandardNavigationEnabled'));
  setTimeout('location.href="' + locationTmp + '"', 500);
}



    </script>
  </head>
  <body onload="isChoiceAlreadyDone()">
  <ul class="list">
    <li>
        <a href="#Course1" class="launch" onclick="alert('event 1')">event 1</a>
    </li>
    <li class="alt">
        <a href="#Course2" class="launch" onclick="alert('event 2')">event 2</a>
    </li>
    <li>
        <a href="#Course3" class="launch" onclick="alert('event 3')">event 3</a>
    </li>
    <li class="alt">
        <a href="#Course4" class="launch" onclick="alert('event 4')">event 4</a>
    </li>
</ul>
  </body>
</html>
