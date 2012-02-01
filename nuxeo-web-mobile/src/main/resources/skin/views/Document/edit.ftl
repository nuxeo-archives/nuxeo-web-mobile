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
      <form>
        <div data-role="fieldcontain" class="ui-field-contain ui-body ui-br">
	         <label for="name" class="ui-input-text">Title</label>
	         <input type="text" value="" id="name" name="name" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset">
			  </div>
			  <div data-role="fieldcontain" class="ui-field-contain ui-body ui-br">
			    <label for="textarea" class="ui-input-text">Description</label>
			    <textarea id="textarea" name="textarea" rows="8" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset"></textarea>
			  </div>
			  <div data-role="fieldcontain" class="ui-field-contain ui-body ui-br">
			    <label for="textarea" class="ui-input-text">Comment</label>
			    <textarea id="textarea" name="textarea" rows="8" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset"></textarea>
			  </div>
			  <fieldset class="ui-grid-a">
			    <div class="ui-block-a">
			      <div data-theme="d" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-d" aria-disabled="false">
			        <span class="ui-btn-inner ui-btn-corner-all">
			          <span class="ui-btn-text">Cancel</span>
			        </span>
			      </div>
			    </div>
			    <div class="ui-block-b">
			      <div data-theme="a" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-b" aria-disabled="false">
			        <span class="ui-btn-inner ui-btn-corner-all">
			          <span class="ui-btn-text">Submit</span>
			        </span>
			      </div>
			    </div>
			  </fieldset>
      </form>
    </div>

</div>

</@block>
</@extends>
