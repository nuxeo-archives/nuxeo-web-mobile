<@extends src="base.ftl">

  <@block name="stylesheets">
  <link rel="stylesheet" href="${skinPath}/css/nuxeo-document-note.css"/>
  </@block>

  <@block name="content">
  <div data-role="page" data-add-back-btn="true" class="preview">

    <div data-role="header">
      <h1>${Context.getMessage('label.header.title.Preview')}</h1>
    </div>

    <div data-role="content">
    ${Adapter.previewContent}
    </div>
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
  </div>

  </@block>
</@extends>
