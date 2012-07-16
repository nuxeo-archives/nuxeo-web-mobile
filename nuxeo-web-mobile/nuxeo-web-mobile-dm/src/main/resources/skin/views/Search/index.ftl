<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Search')}</h1>
    </div>

    <div data-role="content" class="search">
      <#if size = 0>
        <p class="feedback">${Context.getMessage('label.message.NoDocumentMatches')}</p>
      </#if>
      <ul data-role="listview" class="ui-listview search">
        <#assign index = 0>
        <#list docs as doc>
        <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <#assign index = index + 1>
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc["ecm:uuid"]}">
              <#if doc["common:icon"] != null && doc["common:icon"] != "">
                <img class="ui-li-icon ui-li-thumb" src="${basePath}/..${doc["common:icon"]}" />
              <#else>
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}icons/file.gif" />
              </#if>
                <h3>${doc["dc:title"]}</h3>
                <#if doc["dublincore:description"] = null || doc["dublincore:description"] = "" >
                  <p class="ui-li-desc">&nbsp;</p>
                <#else>
                  <p class="ui-li-desc">${doc.dublincore.description}</p>
                </#if>
              </a>
        </li>
        <#if (index > max)><#break></#if>
        </#list>
        <#if (size > max)>
          <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c feedback" data-theme="e">
            ${Context.getMessage('label.message.TooManyResults')}
          </li>
          <li>
            <#if fulltext != null>
            <form method="get" action="${Root.path}/search">
              <input type="search" name="q" id="q" value="${fulltext}" placeholder="${Context.getMessage('label.header.title.Search')}"/>
              <input type="hidden" name="order" id="order" value="dc:modified DESC" />
              <input type="hidden" name="max" id="max" value="20" />
            </form>
            <#else>
            <form method="get" action="${Root.path}/search/nxql">
              <input type="search" name="q" id="q" value="${q}" placeholder="${Context.getMessage('label.search.NXQLQuery')}"/>
              <input type="hidden" name="max" id="max" value="20" />
            </form>
            </#if>
          </li>
        </#if>
        </ul>
    </div>
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
