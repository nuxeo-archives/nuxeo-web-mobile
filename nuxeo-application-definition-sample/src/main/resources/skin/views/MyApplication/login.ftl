<@extends src="base.ftl">
<@block name="content">
  <form method="post" data-ajax="false"
    action="${Context.request.requestURI}?${Context.request.queryString}">
    <div data-role="fieldcontain" class="ui-hide-label">
      <label for="username">Username:</label>
      <input type="text" name="user_name" id="username" value="" placeholder="Username"/>
    </div>
    <div data-role="fieldcontain" class="ui-hide-label">
      <label for="password">password:</label>
      <input type="password" name="user_password" id="password" value="" placeholder="Password"/>
    </div>
    <input class="login_button" type="submit" name="Submit" value="Login"/>
  </form>
</@block>
</@extends>
