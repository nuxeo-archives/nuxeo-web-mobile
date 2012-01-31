<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
        <ul data-role="listview" data-inset="true">
        <#list Adapter.children as child>
          <li>
            <#if child.isFolder>
              <a href="${Root.path}/doc/${child.id}/@folderish">
            <#else>
              <a href="${Root.path}/doc/${child.id}">
            </#if>
            <#if child.common.icon != null && child.common.icon != "">
                <img src="${skinPath}${child.common.icon}" />
            </#if>
                <span>${child.title}</span>
              </a>
        </#list>
        </ul>        
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
