<@extends src="base.ftl">
<@block name="content">
<div data-role="page" data-add-back-btn="true">

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
              <#if contributor != "system">
                <span class="tag">
                  <a href="${Root.path}/profile/${contributor}">${contributor}</a>
                </span>
              </#if>
            </#list>
          </div>
        </div>

        <div data-role="navbar" class="noSidespace">
          <ul>
            <#if This.hasPreview()>
              <li><a data-ajax="false" href="${This.previewURL}"><img alt="Preview" src="${skinPath}/icons/preview.png" /></a></li>
            </#if>
            <#if This.document.schemas?seq_contains("note")>
              <li><a href="${Root.path}/doc/${This.document.id}/@preview"><img alt="Preview" src="${skinPath}/icons/preview.png" /></a></li>
            </#if>
            <#if This.isFolder>
              <li><a href="${This.previewURL}"><img alt="Content" src="${Root.path}/doc/${This.id}/@folderish" /></a></li>
            </#if>
            <li><a href="${Root.path}/doc/${This.document.id}/@comment"><img alt="Comments" src="${skinPath}/icons/comments.png" /></a></li>
            <li><a href="${Root.path}/doc/${This.document.id}/@annotions"><img alt="Annotations" src="${skinPath}/icons/annotations.png" /></a></li>
            <li><a href="${Root.path}/doc/${This.document.id}/@relation"><img alt="Relations" src="${skinPath}/icons/relations.png" /></a></li>
          </ul>
        </div>

        <ul data-role="listview" data-inset="true">
          <li class="nxDocumentItem">
            <a data-ajax="false" href="mailto:?cc=${This.principal.email}&amp;subject=New%20Document%20will%20sent&amp;body=Mettre%20l'URL">
              Mail it
            </a>
          </li>
          <li class="nxDocumentItem">
            <a data-ajax="false" href="mailto:?cc=${This.principal.email}&amp;subject=New%20Document%20will%20sent&amp;body=Mettre%20l'URL">
              Mail me
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${This.document.id}?mode=edit">
              Edit
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${This.document.id}?mode=delete-confirmation" data-rel="dialog">
              Delete
            </a>
          </li>
        </ul>
        
      </div>
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic false/>
  </div>

</@block>
</@extends>
