<@extends src="base.ftl">

<@block name="content">
<div data-role="page"
   <#if page?size = 0>
   data-add-back-btn="true"
   </#if>
   class="searchResult">


    <div data-role="header" data-position="inline">
      <#if (pageIndex > 0)>
        <a data-direction="reverse" href="@faceted?pageIndex=${pageIndex - 1}" class="ui-btn-left smallButton">
          <img src="${skinPath}/icons/arrow_left.png"/>
        </a>
      </#if>
      <h1>Result ${pageIndex + 1}/${pageNumber}</h1>
      <#if (pageIndex < pageNumber - 1)>
        <a href="@faceted?pageIndex=${pageIndex + 1}" class="ui-btn-right smallButton">
          <img src="${skinPath}/icons/arrow_right.png"/>
        </a>
      </#if>
    </div>

    <div data-role="content">
        <#if page?size = 0>
          <p class="feedback">
            No document matches your query.
          </p>
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

    <#import "../../footer.ftl" as footer/>
    <@footer.basic false/>
    
</div>

</@block>
</@extends>
