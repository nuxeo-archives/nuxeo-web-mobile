<html>
  <head>
    <title>Nuxeo DM</title>
    <meta name="viewport" content="user-scalable=no,width=device-width"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  </head>
  <body>
test
    <form id="login" class="panel" title="Nuxeo DM"
      method="get" action="${Root.path}/auth/login?initialURLRequested=${Context.request.requestURI}?${Context.request.queryString}"
      target="_self" selected="true">
      <fieldset>
        <div class="row">
          <label for="username"/>
          <input class="login_input" type="text" name="user_name" id="username"/>
        </div>
        <div class="row">
          <label for="password"/>
          <input class="login_input" type="password" name="user_password" id="userpassword"/>
        </div>
      </fieldset>

      <input class="login_button" type="submit" name="Submit"
                    value="Login"/>
    </form>
  </body>
</html>
