<@extends src="base.ftl">

<@block name="stylesheets">
  <link rel="stylesheet" href="${skinPath}/css/nuxeo-document-note.css" />
</@block>

<@block name="content">
<div data-role="page" data-add-back-btn="true" class="preview">

    <div data-role="header">
        <h1>Preview</h1>
    </div>

    <div data-role="content">
       ${Adapter.previewContent}
    </div>

</div>

</@block>
</@extends>
