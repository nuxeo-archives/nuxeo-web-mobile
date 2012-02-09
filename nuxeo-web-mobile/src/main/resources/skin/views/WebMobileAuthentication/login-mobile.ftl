<@extends src="base.ftl">

<@block name="content" >
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

  <div data-role="page" class="login">
    <div data-role="content">

      <div class="logo nospace">
        <img src="${skinPath}/img/nuxeo_logo.png" alt="Nuxeo" />
      </div>

      <div class="authentification">
        <form method="post" data-ajax="false" action="${Context.request.requestURI}?${Context.request.queryString}">
          <div data-role="fieldcontain" class="ui-hide-label">
            <label for="username">${Context.getMessage('label.login.auth.Username')}</label>
            <input type="text" name="user_name" id="username" value="" placeholder="${Context.getMessage('label.login.auth.Username')}" data-theme="d"/>
          </div>
          <div data-role="fieldcontain" class="ui-hide-label">
            <label for="password">${Context.getMessage('label.login.auth.Password')}</label>
            <input type="password" name="user_password" id="password" value="" placeholder="${Context.getMessage('label.login.auth.Password')}" data-theme="d"/>
          </div>
          <input class="login_button" type="submit" name="Submit" value="${Context.getMessage('label.login.auth.LogIn')}"/>
        </form>
      </div>

    </div>
  </div>

</@block>
</@extends>
