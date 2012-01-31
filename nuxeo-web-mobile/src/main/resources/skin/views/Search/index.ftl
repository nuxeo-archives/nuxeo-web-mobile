<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
        <ul data-role="listview" data-inset="true">
        <#list docs as doc>
          <li>
            <a href="${basePath}/mobile/doc/${doc.id}">
              <#if doc.common.icon != null && doc.common.icon != "">
                <img src="${skinPath}${doc.common.icon}" alt="icon1" class="ui-li-icon"/>
              </#if>
              <span class="ui-li-count">${doc.title}</span>
            </a>
          </li>
        </#list>
        </ul>        
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
