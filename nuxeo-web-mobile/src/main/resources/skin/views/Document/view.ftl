<@extends src="base.ftl">
<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>View</h1>
    </div>

    <div data-role="content" class="documentView" id="main">
      <div id="${This.document.id}" title="Details"  class="panel">
        <div class="white nospace shadow documentInfo">
          <div class="title"><img alt="Document icon" src="${skinPath}/icons/doc.png" />${This.document.dublincore.title}</div>
          <div class="description">${This.document.dublincore.description}</div>
          <div class="modificationDate">
            <#if This.document.dublincore.modified != null>
              <span>modified</span>
              <span>${This.document.dublincore.modified.time?datetime}</span>
            </#if>
          </div>
          <div class="participants">
            <#list This.document.dublincore.contributors as contributor>
              <span class="tag">
                <#if contributor != "system">
                <a href="${Root.path}/profile/${contributor}">${contributor}</a>
                </#if>
              </span>
            </#list>
          </div>
        </div>
          

        <ul data-role="listview" data-inset="true">
          <#if This.hasPreview()>
            <li class="nxDocumentItem">
              <!-- TODO: FIND A SOLUTION TO KEEP THE NAVIGATION WITH THE PREVIEW
              <a href="${Root.path}/doc/${Document.id}/@preview">
              -->
              <a data-ajax="false" href="${This.previewURL}">
                Preview
              </a>
            </li>
          </#if>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${This.document.id}">
              Relations
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${This.document.id}/@comment">
              Comments
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${This.document.id}">
              Annotations
            </a>
          </li>
        </ul>
      </div>
      <fieldset class="ui-grid-b">
        <div class="ui-block-a">
          <a href="mailto:?cc=${This.principal.email}&amp;subject=New%20Document%20will%20sent&amp;body=Mettre%20l'URL" data-role="button">Mail</a>
        </div>
        <div class="ui-block-b">
          <a href="${Root.path}/doc/${This.document.id}?mode=edit" data-role="button">Edit</a>
        </div>
        <div class="ui-block-c">
          <a href="${Root.path}/doc/${This.document.id}?mode=delete-confirmation" data-rel="dialog" data-role="button">Delete</a
        </div>
      </fieldset>
    </div>

    </div>
</div>

      <div data-role="footer">
          <h4>Page Footer</h4>
      </div>
</@block>
</@extends>
