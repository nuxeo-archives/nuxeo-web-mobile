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
            <a href="${Root.path}/doc/${This.document.id}/@relation">
              Relations
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${doc.id}/@comment">
              Comments
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${doc.id}/@annotations">
              Annotations
            </a>
          </li>
        </ul>
      </div><!-- Panel detail -->
      
      <fieldset class="ui-grid-b">
        <div class="ui-block-a">
          <a href="mailto:?cc=${This.principal.email}&amp;subject=<@emailSubject/>t&amp;body=<@emailBody document=doc/>">
            Mail
          </a>
        </div>
        <div class="ui-block-b">
          <a href="${Root.path}/doc/${doc.id}?mode=edit" data-role="button">Edit</a>
        </div>
        <div class="ui-block-c">
          <a href="${Root.path}/doc/${doc.id}?mode=delete-confirmation" data-rel="dialog" data-role="button">Delete</a>
        </div>
      </fieldset>
    </div><!-- content -->

    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic false/>
  </div>

</@block>
</@extends>
