<@extends src="base.ftl">

<@block name="stylesheets">
  <link rel="stylesheet" href="${skinPath}/css/nuxeo-dform.css" />
</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
      <h1>Edit</h1>
    </div>

    <div data-role="content">
      <form method="post" action="${Root.path}/doc/${This.document.id}/@put">
        <div data-role="fieldcontain" class="ui-field-contain ui-body ui-br">
	         <label for="name" class="ui-input-text">Title</label>
	         <input type="text" value="${This.document.title}" name="dc:title" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset">
			  </div>
			  <div data-role="fieldcontain" class="ui-field-contain ui-body ui-br">
			    <label for="textarea" class="ui-input-text">Description</label>
			    <textarea id="textarea" value="${This.document.description}" name="dc:description"  rows="8" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset"></textarea>
			  </div>
			  <fieldset class="ui-grid-a">
			    <div class="ui-block-a">
			      <button class="ui-btn-text">Cancel</button>
			    </div>
			    <div class="ui-block-b">
			      <button class="ui-btn-text" data-theme="b">Submit</button>
			    </div>
			  </fieldset>
      </form>
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>

