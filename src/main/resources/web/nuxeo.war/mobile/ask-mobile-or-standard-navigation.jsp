<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- Nuxeo Enterprise Platform, svn $Revision: 22925 $ -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String productName = Framework.getProperty("org.nuxeo.ecm.product.name");
    String productVersion = Framework.getProperty("org.nuxeo.ecm.product.version");
%>
<html>
  <fmt:setBundle basename="messages" var="messages" />

  <head>
    <title>Nuxeo DM</title>
    <meta name="viewport" content="user-scalable=no,width=device-width"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <link rel="apple-touch-icon" href="icons/nuxeo_logo.png" />
    <link rel="apple-touch-startup-image" href="image/nuxeo_splash_screen.png" />
  </head>
  <body>
    <script type="text/javascript">
      function SetCookie(name, value, expDays, path, domain, secure) {
        // Set cookie with name, value etc provided
        // in function call and date from above
        // Number of days the cookie should persist NB expDays='' or undef. => non-persistent
        if (expDays != null ) {
          var expires = new Date();
          expires.setTime(expires.getTime() + (expDays*24*60*60*1000));
        }
        var curCookie = name + "=" + escape(value) +
          ((expires) ? "; expires=" + expires.toGMTString() : "") +
          ((path) ? "; path=" + path : "") +
          ((domain) ? "; domain=" + domain : "") +
          ((secure) ? "; secure" : "");
          document.cookie = curCookie;
        }

      var answer = confirm ("Do you want to use Mobile Application?");

      if (answer) {
        SetCookie('ForceStandardNavigationEnabled', 'false', 1, '/');
        window.location = '/nuxeo/site/mobile/home?initialURLRequested=<%= request.getParameter("initialURLRequested") %>';
        alert('1');
      } else {
        SetCookie('ForceStandardNavigationEnabled', 'true', 1, '/');
        window.location = '<%= request.getParameter("initialURLRequested") %>';
        alert('2');
        
      }

    </script>
  </body>
</html>
