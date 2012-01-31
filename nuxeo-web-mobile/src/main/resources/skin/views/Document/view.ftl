<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>View</h1>
    </div>

    <div data-role="content">
      <div id="${This.document.id}" title="Details"  class="panel">
        <div class="white nospace shadow documentInfo">
          <div class="title"><img alt="Document icon" src="${skinPath}/icons/doc.png" />${This.document.dublincore.title}</div>
          <div class="description">${This.document.dublincore.description}</div>
          <div class="modificationDate">
            <span>last modification</span>
            <span>${This.document.dublincore.issued}</span>
          </div>
          <div class="participants">
            <!-- TODO: Add contributors correct value -->
            <span class="tag"><a href="#">Lise Kemen</a></span><span class="tag"><a href="#">Benjamin Jalon</a></span><span class="tag"><a href="#">Delphine Renevey</a></span>
            <span>${This.document.dublincore.issued}</span>
          </div>
        </div>
          

        <ul data-role="listview" data-inset="true">
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
      <div data-role="controlgroup" data-type="horizontal" class="ui-corner-all" >
          <a href="mailto:?cc=${This.principal.email}&amp;subject=New%20Document%20will%20sent&amp;body=Mettre%20l'URL" class="ui-btn-icon-left" data-role="button">Mail</a>
          <a href="${Document.id}?mode=edit" class="ui-btn-icon-left" data-role="button">Edit</a>
          <a href="index.html" class="ui-btn-icon-left" data-role="button">Delete</a>
      </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
