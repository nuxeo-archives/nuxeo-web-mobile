<@extends src="base.ftl">

<@block name="header_scripts">
  <script type="application/x-javascript">
    document.onload = function() { document.getElementById('submit').disabled=true; }

    function valideUsernameValue() {
      var value = document.getElementById('username').value;
      if (value == null || value.trim() == '') {
        document.getElementById('submit').disabled=true;
      } else {
        document.getElementById('submit').disabled=false;
      }
    }
  </script>
</@block>

<@block name="content">
  <form method="post" data-ajax="false"
    action="${Context.request.requestURI}?${Context.request.queryString}">
    <div data-role="fieldcontain" class="ui-hide-label">
      <label for="username">Username:</label>
      <input type="text" name="user_name" id="username" value=""
        placeholder="Username" onkeyup="valideUsernameValue();"/>
    </div>
    <div data-role="fieldcontain" class="ui-hide-label">
      <label for="password">password:</label>
      <input type="password" name="user_password" id="password" value="" placeholder="Password"/>
    </div>
    <input class="login_button" type="submit" name="Submit" id="submit" value="Login"/>
  </form>
</@block>
</@extends>
