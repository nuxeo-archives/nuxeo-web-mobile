<@extends src="base.ftl">

<@block name="content">
  <script type="application/x-javascript">
    <#noparse>
      function validateForm() {
        if($('#username').val() != '') {
          $('input[type="submit"]').removeAttr('disabled');
        } else {
          $('input[type="submit"]').attr('disabled','disabled');
        }
      }

    $(document).ready(function(){
      if ($('#username').val() == '') {
        $('input[type="submit"]').attr('disabled','disabled');
      }
      
      $('#username').keyup(validateForm);
      
      self.setInterval(validateForm,200);
    });
    </#noparse>
  </script>

  <form method="post" data-ajax="false"
    action="${Context.request.requestURI}?${Context.request.queryString}">
    <div data-role="fieldcontain" class="ui-hide-label">
      <label for="username">Username:</label>
      <input type="text" name="user_name" id="username" value="" placeholder="Username required"/>
    </div>
    <div data-role="fieldcontain" class="ui-hide-label">
      <label for="password">password:</label>
      <input type="password" name="user_password" id="password" value="" placeholder="Password"/>
    </div>
    <input class="login_button" type="submit" name="Submit" value="Login"/>
  </form>
</@block>
</@extends>
