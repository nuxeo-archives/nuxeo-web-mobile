<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
        <#list docs as doc>
          <li>
            <#if doc.common.icon != null && doc.common.icon != "">
              <img src="${skinPath}${doc.common.icon}" />
            </#if>
          <a href="${basePath}/mobile/doc/${doc.id}">${doc.title}</a></li>
        </#list>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
