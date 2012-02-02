<@extends src="base.ftl">

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Search</h1>
    </div>

    <div data-role="content">
        <ul data-role="listview" class="ui-listview search">
        <#if size = 0>
        No document found
        </#if>

        <#assign index = 0>
        <#list docs as doc>
        <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <#assign index = index + 1>
              <a class="ui-link-inherit" href="${Root.path}/doc/${doc["ecm:uuid"]}">
              <#if doc["common:icon"] != null && doc["common:icon"] != "">
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}${doc["common:icon"]}" />
              <#else>
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}icons/file.gif" />
              </#if>
                <h3>${doc["dc:title"]}</h3>
                <p class="ui-li-desc">Description of the document</p>
              </a>
        </li>
        <#if (index > max)><#break></#if>
        </#list>
        <#if (size > max)>
          <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c" data-theme="e">
            Too much result too return please affine your request.
          </li>
          <li>
            <#if fulltext != null>
            <form method="get" action="${Root.path}/search">
              <input type="search" name="q" id="q" value="${fulltext}" placeholder="Fulltext Search"/>
              <input type="hidden" name="order" id="order" value="dc:modified DESC" />
              <input type="hidden" name="max" id="max" value="20" />
            </form>
            <#else>
            <form method="get" action="${Root.path}/search/nxql">
              <input type="search" name="q" id="q" value="${q}" placeholder="NXQL Request"/>
              <input type="hidden" name="max" id="max" value="20" />
            </form>
            </#if>
          </li>
        </#if>
        </ul>
    </div>
    

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
