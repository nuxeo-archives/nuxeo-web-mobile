<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>Request Result ${pageIndex}/${pageNumber}</h1>
    </div>

    <div data-role="content">
        <#if page?size = 0>
          No Document returned
        </#if>
        <ul data-role="listview" class="ui-listview search">
        <#list page as doc>
          <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
            <#if doc.isFolder>
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc.id}/@folderish">
            <#else>
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc.id}">
            </#if>
            <#if doc.common.icon != null && doc.common.icon != "">
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}${doc.common.icon}" />
            <#else>
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/file.gif" />
            </#if>
                <h3>${doc.title}</h3>
                <p class="ui-li-desc">Description of the document</p>
              </a>
          </li>
        </#list>
        </ul>        
    </div>

</div>

</@block>
</@extends>
