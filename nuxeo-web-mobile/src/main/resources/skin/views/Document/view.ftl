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
              <#if contributor != "system">
                <span class="tag">
                  <a href="${Root.path}/profile/${contributor}">${contributor}</a>
                </span>
              </#if>
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
            <a href="${Root.path}/doc/${This.document.id}/@relation">
              Relations
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${This.document.id}/@comment">
              Comments
            </a>
          </li>
          <li class="nxDocumentItem">
            <a href="${Root.path}/doc/${This.document.id}/@annotations">
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
          <a href="${Root.path}/doc/${This.document.id}?mode=delete-confirmation" data-rel="dialog" data-role="button">Delete</a>
        </div>
      </fieldset>
    </div>

    <div data-role="footer">
      <div data-role="navbar">
        <ul>
          <li><a href="${Root.path}"><img alt="Home" src="${skinPath}/icons/footer_home.png" /></a></li>
          <li><a href="${Root.path}/docPath/@folderish"><img alt="Personal Workspace" src="${skinPath}/icons/footer_personalworkspace.png" /></a></li>
          <li><a href="${Root.path}/profile"><img alt="Profile" src="${skinPath}/icons/footer_profile.png" /></a></li>
          <li><a href="${Root.path}/search/faceted"><img alt="Search" src="${skinPath}/icons/footer_search.png" /></a></li>
        </ul>
      </div>
    </div>
    </div>
  </div>

</@block>
</@extends>
