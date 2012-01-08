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
    <link rel="stylesheet" type="text/css" href="iui/iui.css"/>
    <link rel="stylesheet" type="text/css" href="iui/t/default/default-theme.css"/>
    <link rel="stylesheet" type="text/css" href="iui/ext-sandbox/masabi/t/default/iui_ext.css"/>
    <link rel="apple-touch-icon" href="icons/nuxeo_logo.png" />
    <link rel="apple-touch-startup-image" href="image/nuxeo_splash_screen.png" />
    <script src="iui/iui.js"></script>
    <script src="iui/ext-sandbox/masabi/iui_ext.js"></script>
    <!-- for option button improvement -->
    <script src="iui/ext-sandbox/TbBMod/TbBMod.js"></script>
    <script src="script/login-mobile.js"></script>
  </head>
  <body>
    <div id="toolBar" class="toolbar">
      <h1 id="pageTitle"></h1>
      <a id="backButton" class="button" href="#"></a>
      <a id="help" class="button" href="#about">?</a>
    </div>
    <div id="about" title="About" class="panel" class="toolbar">
      <h2><%=productName%> - <%=productVersion%></h2>
    </div>
    <form id="login" class="panel" title="<%=productName%>" method="post" action="site/mobile/home?initialURLRequested=<%= request.getParameter("initialURLRequested") %>" target="_self" selected="true">
      <fieldset>
        <div class="row">
          <label for="username">
            <fmt:message bundle="${messages}" key="label.login.username" />
          </label>
          <input class="login_input" type="text" name="user_name" id="username">
        </div>
        <div class="row">
          <label for="password">
            <fmt:message bundle="${messages}" key="label.login.password" />
          </label>
          <input class="login_input" type="password" name="user_password" id="password">
        </div>
      </fieldset>

      <input class="login_button" type="submit" name="Submit"
                    value="<fmt:message bundle="${messages}" key="label.login.logIn" />">
    </form>
  </body>
</html>
