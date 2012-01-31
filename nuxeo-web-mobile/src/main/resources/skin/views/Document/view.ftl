<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">

        <div id="${This.document.id}" title="Details"  class="panel">
          <h2>Dublincore</h2>
          <fieldset>
            <div class="row">
              <label>Title</label>
              <span>${This.document.dublincore.title}</span>
            </div>
            <div class="row">
              <label>Description</label>
              <span>${This.document.dublincore.description}</span>
            </div>
            <div class="row">
              <label>Issued date</label>
              <span>${This.document.dublincore.issued}</span>
            </div>
          </fieldset>
          
          <h2>Main File attached</h2>

          <ul>
            <#if This.hasPreview()>
              <li class="nxDocumentItem">
               <a href="${Document.id}/@preview">Preview</a>
              </li>
            </#if>
            <li class="nxDocumentItem">
              <a href="${basePath}/mobile/relations/${This.document.id}">
                Relations
              </a>
            </li>
            <li class="nxDocumentItem">
              <a href="${basePath}/mobile/comments/${This.document.id}">
                Comments
              </a>
            </li>
            <li class="nxDocumentItem">
              <a href="${basePath}/mobile/annotations/${This.document.id}">
                Annotations
              </a>
            </li>
          </ul>
        </div>
        <div data-role="controlgroup" data-type="horizontal" >
            <a href="index.html" data-role="button" data-icon="arrow-u">Up</a>
            <a href="index.html" data-role="button" data-icon="arrow-d">Down</a>
            <a href="index.html" data-role="button" data-icon="delete">Delete</a>
        </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
