<@extends src="base.ftl">

<@block name="content">
<div data-role="page">
  <div data-role="content">
      <h1>Do you really want to delete ${This.document.title} ?</h1>
        <#if This.document.allowedStateTransitions?seq_contains("delete")>
          This document will not be permanently deleted. You can restore from the Trash of the Super Space.
        </#if> 
        <form action="${Context.request.requestURI}" method="delete">
          <button data-direction="reverse" data-theme="b" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-b">
            YES !
          </button>
        </form>       
        <a href="#main" data-role="button" data-theme="c" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c">
          Nope
        </a>    
  </div>
</div>    
    
</@block>
</@extends>
