<@extends src="base.ftl">

<@block name="stylesheets">
  <link rel="stylesheet" href="${skinPath}/css/nuxeo-dform.css" />
</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
      <h1>${Context.getMessage('label.header.title.Edit')}</h1>
    </div>

    <div data-role="content">
      <form method="post" action="${Root.path}/doc/${This.document.id}/@put">
        <div data-role="fieldcontain" class="ui-field-contain ui-body ui-br">
	         <label for="name" class="ui-input-text">${Context.getMessage('label.form.Title')}</label>
	         <input type="text" value="${This.document.title}" name="dc:title" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset">
			  </div>
			  <div data-role="fieldcontain" class="ui-field-contain ui-body ui-br">
			    <label for="textarea" class="ui-input-text">${Context.getMessage('label.form.Description')}</label>
			    <textarea id="textarea" value="${This.document.description}" name="dc:description"  rows="8" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset"></textarea>
			  </div>
			  <fieldset class="ui-grid-a">
			    <div class="ui-block-a">
			      <button class="ui-btn-text">${Context.getMessage('label.action.Cancel')}</button>
			    </div>
			    <div class="ui-block-b">
			      <button class="ui-btn-text" data-theme="b">${Context.getMessage('label.action.Submit')}</button>
			    </div>
			  </fieldset>
      </form>
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic false/>
</div>

</@block>
</@extends>

