<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
        <#list Adapter.children as child>
          <#if child.isFolder>
            <li><a href="${basePath}/mobile/doc/${child.id}/@folderish">${child.title}</a></li>
          <#else>
            <li><a href="${basePath}/mobile/doc/${child.id}">${child.title}</a></li>
          </#if>
        </#list>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
