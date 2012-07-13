<@extends src="base.ftl">

<@block name="content">
  <div data-role="page">
    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Confirmation')}</h1>
    </div>
    <div data-role="content" data-theme="c">
      <h1>${Context.getMessage('label.message.ConfirmDelete')} ${This.document.title} ?</h1>
        <#if This.document.allowedStateTransitions?seq_contains("delete")>
          ${Context.getMessage('label.message.DeleteRestoreDocument')}
        </#if> 
        <form action="${Context.request.requestURI}" method="delete">
          <button data-direction="reverse" data-theme="b" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-b">
            ${Context.getMessage('label.action.Yes')}
          </button>
        </form>       
        <a href="#main" data-role="button" data-theme="c" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c">
          <span>${Context.getMessage('label.action.No')}</span>
        </a>    
    </div>
    <div data-role="footer"></div>

</@block>
</@extends>
