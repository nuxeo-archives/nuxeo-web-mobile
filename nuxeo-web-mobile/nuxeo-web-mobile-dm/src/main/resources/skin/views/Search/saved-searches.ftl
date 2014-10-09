<@extends src="base.ftl">

  <@block name="content">
  <div data-role="page" data-add-back-btn="true">

    <div data-role="header">
      <h1>${Context.getMessage('label.header.title.Searches')}</h1>
    </div>

    <div data-role="content" class="search">

      <form method="get" action="${Root.path}/search">
        <input type="search" name="q" id="q" value="" placeholder="${Context.getMessage('label.header.title.Search')}"/>
        <input type="hidden" name="order" id="order" value="dc:modified DESC"/>
        <input type="hidden" name="max" id="max" value="20"/>
      </form>

      <h2>${Context.getMessage('label.search.title.SavedSearches')}</h2>
      <#if mySearches?size == 0>
        <p class="feedback">
        ${Context.getMessage('label.message.NoSavedSearch')}
        </p>
      <#else>
        <ul class="ui-listview" data-role="listview">
          <#list mySearches as doc>
            <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc.id}/@faceted">
                <#if doc.common.icon != null && doc.common.icon != "">
                  <img class="ui-li-icon ui-li-thumb" src="${basePath}/..${doc.common.icon}"/>
                <#else>
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/file.gif"/>
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

      <h2>${Context.getMessage('label.search.title.SharedSearches')}</h2>
      <#if sharedSearches?size == 0>
        <p class="feedback">
        ${Context.getMessage('label.message.NoSharedSearch')}
        </p>
      <#else>
        <ul class="ui-listview" data-role="listview">
          <#list sharedSearches as doc>
            <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc.id}/@faceted">
                <#if doc.common.icon != null && doc.common.icon != "">
                  <img class="ui-li-icon ui-li-thumb" src="${basePath}/..${doc.common.icon}"/>
                <#else>
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/file.gif"/>
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
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
  </div>

  </@block>
</@extends>
