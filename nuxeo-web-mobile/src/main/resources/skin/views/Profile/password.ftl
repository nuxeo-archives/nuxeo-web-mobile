<@extends src="base.ftl">

<@block name="content">
  <div data-role="dialog">
    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Password')}</h1>
    </div>
    <div data-role="content" data-theme="d">
      <form action="?isPasswordModification=true" method="post">
        <div class="marginBottom">
          <input type="password" name="user:password_old" placeholder="${Context.getMessage('label.form.OldPassword')}">
        </div>
        <div class="marginBottom">
          <input type="password" name="user:password" placeholder="${Context.getMessage('label.form.NewPassword')}">
        </div>
        <div class="marginBottom">
          <input type="password" name="user:password_bis" placeholder="${Context.getMessage('label.form.TypeItAgain')}">
        </div>
        <fieldset class="ui-grid-a">
          <div class="ui-block-a">
            <button onclick="history.back();" data-direction="reverse" data-theme="b" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-b">${Context.getMessage('label.action.Save')}</button>
          </div>
          <div class="ui-block-b">
            <a href="#" data-rel="back" data-role="button" data-theme="c">${Context.getMessage('label.action.Cancel')}</a>
          </div>
        </fieldset>
      </form>
    </div>
    
</@block>
</@extends>
