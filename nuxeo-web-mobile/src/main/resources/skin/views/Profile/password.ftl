<@extends src="base.ftl">

<@block name="content">
  <div data-role="dialog">
    <div data-role="header">
        <h1>Password</h1>
    </div>
    <div data-role="content" data-theme="d">
      <form action="?isPasswordModification=true" method="post">
        <div class="marginBottom">
          <input type="password" name="user:password_old" placeholder="Old password">
        </div>
        <div class="marginBottom">
          <input type="password" name="user:password" placeholder="New password">
        </div>
        <div class="marginBottom">
          <input type="password" name="user:password_bis" placeholder="Type it again">
        </div>
        <fieldset class="ui-grid-a">
          <div class="ui-block-a">
            <button onclick="history.back();" data-direction="reverse" data-theme="b" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-b">Save</button>
          </div>
          <div class="ui-block-b">
            <a href="#" data-rel="back" data-role="button" data-theme="c">Cancel</a>
          </div>
        </fieldset>
      </form>
    </div>
    
</@block>
</@extends>
