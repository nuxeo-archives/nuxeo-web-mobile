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
    <script src="scripts/web-mobile-functions.js"></script>
  </head>
  <body>
    <form id="login" class="panel" title="<%=productName%>"
      method="get" action="/site/mobile/home?initialURLRequested=<%= request.getParameter("requestedUrl") %>"
      target="_self" selected="true">
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
          <input class="login_input" type="password" name="user_password" id="userpassword">
        </div>
      </fieldset>

      <input class="login_button" type="submit" name="Submit"
                    value="<fmt:message bundle="${messages}" key="label.login.logIn" />">
    </form>
  </body>
</html>
