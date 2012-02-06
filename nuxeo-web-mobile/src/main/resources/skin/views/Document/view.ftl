<#macro "emailSubject">New%20Document%20will%20send</#macro>

<#macro "emailBody" document>
  Hi,%0D
  %0D
  ${Context.principal.name} found a funny document named '${document.title}' to share it with you. As this document stored
  in your Nuxeo, it can't be a spam. So click quickly on this following link and enjoy.%0D
  %0D
  ${This.getJSFURLPath(document)}%0D
  %0D
  regards,%0D
  Your sincerely Nuxeo Server.
</#macro>

<@extends src="base.ftl">
<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>View</h1>
    </div>

    <div data-role="content" class="documentView" id="main">
      <#assign doc = This.document>
      <div id="${doc.id}" title="Details"  class="panel">
        <div class="white nospace shadow documentInfo">
          <div class="title">
            <img alt="Document icon" src="${skinPath}${doc.common.icon}" />${doc.dublincore.title}
          </div>
          <div class="description">${doc.dublincore.description}</div>
          <div class="modificationDate">
            <#if doc.dublincore.modified != null>
              <span>modified</span>
              <span>${doc.dublincore.modified.time?datetime}</span>
            </#if>
          </div>
          <div class="participants">
            <#list doc.dublincore.contributors as contributor>
              <#if contributor != "system">
                <span class="tag">
                  <a href="${Root.path}/profile/${contributor}">${contributor}</a>
                </span>
              </#if>
            </#list>
          </div>
        </div><!-- documentInfo -->

        <div data-role="navbar" class="noSidespace">
          <ul>
            <#if This.hasPreview()>
              <li><a data-ajax="false" href="${This.previewURL}"><img alt="Preview" src="${skinPath}/icons/preview.png" /></a></li>
            </#if>
            <#if doc.schemas?seq_contains("note")>
              <li><a href="${Root.path}/doc/${doc.id}/@preview"><img alt="Preview" src="${skinPath}/icons/preview.png" /></a></li>
            </#if>
            <#if doc.isFolder>
              <li>
                <a href="${Root.path}/doc/${doc.id}/@folderish"><img alt="Content" src="${skinPath}/icons/preview.png" /></a>
              </li>
            </#if>
            <li><a href="${Root.path}/doc/${doc.id}/@comment"><img alt="Comments" src="${skinPath}/icons/comments.png" /></a></li>
            <li><a href="${Root.path}/doc/${doc.id}/@annotations"><img alt="Annotations" src="${skinPath}/icons/annotations.png" /></a></li>
            <li><a href="${Root.path}/doc/${doc.id}/@relation"><img alt="Relations" src="${skinPath}/icons/relations.png" /></a></li>
          </ul>
        </div>

        <ul data-role="listview" data-inset="true">
          <li class="nxDocumentItem">
            <a href="mailto:?cc=${This.principal.email}&amp;subject=<@emailSubject/>t&amp;body=<@emailBody document=doc/>">
              Mail it
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="#" 
               onclick="var jqxhr = $.get('${Root.path}/doc/${doc.id}/mailIt', function() { displayNotification('Request sent to server please '); })
                .success(function() { displayNotification('Mail sent to your personnal account'); })
                .error(function() { displayNotification('Problem occured during, please contact your administrator'); })">
              Mail me
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${doc.id}?mode=edit">
              Edit
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="#" onclick="${Root.path}/doc/${doc.id}?mode=delete-confirmation" data-rel="dialog">
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
