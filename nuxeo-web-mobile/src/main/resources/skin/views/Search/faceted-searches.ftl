<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>Search</h1>
    </div>

    <div data-role="content" class="search">

      <form method="get" action="${Root.path}/search">
        <input type="search" name="q" id="q" value="" placeholder="Fulltext Search"/>
        <input type="hidden" name="order" id="order" value="dc:modified DESC" />
        <input type="hidden" name="max" id="max" value="20" />
      </form>

      <h2>My Searches</h2>
      <ul  class="ui-listview ui-listview-inset ui-corner-all ui-shadow" data-inset="true" data-role="listview">
      <#if mySearches?size == 0>
        <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        You have no faceted search stored in your preference. You can easily create ones from a desktop browser.
            Click on the second navigation tab, create your query, and save. This query will be available here.
        </li>
      <#else>
          <#list mySearches as doc>
            <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc.id}/@faceted">
                <#if doc.common.icon != null && doc.common.icon != "">
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}${doc.common.icon}" />
                <#else>
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/file.gif" />
                </#if>
                <h3>${doc.title}</h3>
                <#if doc.dublincore.description = null || doc.dublincore.description = "" >
                  <p class="ui-li-desc">&nbsp;</p>
                <#else>
                  <p class="ui-li-desc">${doc.dublincore.description}</p>
                </#if>
              </a>
            </li>
          </#list>
        </ul>
      </#if>

      <h2>Shared Searches</h2>
      <ul  class="ui-listview ui-listview-inset ui-corner-all ui-shadow" data-inset="true" data-role="listview">
      <#if sharedSearches?size == 0>
        <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
          No Shared Search
        </li>
      <#else>
          <#list mySearches as doc>
            <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc.id}/@faceted">
                <#if doc.common.icon != null && doc.common.icon != "">
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}${doc.common.icon}" />
                <#else>
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/file.gif" />
                </#if>
                <h3>${doc.title}</h3>
                <#if doc.dublincore.description = null || doc.dublincore.description = "" >
                  <p class="ui-li-desc">&nbsp;</p>
                <#else>
                  <p class="ui-li-desc">${doc.dublincore.description}</p>
                </#if>
              </a>
            </li>
          </#list>
        </ul>
      </#if>
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
  </div>

</@block>
</@extends>
