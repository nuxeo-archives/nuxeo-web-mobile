<@extends src="base.ftl">

<@block name="content">
  <div data-role="dialog">
    <div data-role="header">
        <h1>Change your password</h1>
    </div>
    <div data-role="content" data-theme="c">
      
        <form action="?isPasswordModification=true" method="post">
          <input type="password" name="user:password_old" placeholder="Old password">
          <input type="password" name="user:password" placeholder="New password">
          <input type="password" name="user:password_bis" placeholder="Type it again">
          <button onclick="history.back();" data-direction="reverse" data-theme="b" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-b">
            Save
          </button>
        </form>       
    </div>
    
</@block>
</@extends>
