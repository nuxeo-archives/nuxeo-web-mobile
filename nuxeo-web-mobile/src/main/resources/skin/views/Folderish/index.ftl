<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
        <ul data-role="listview" class="ui-listview">
        <#list Adapter.children as child>
          <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
            <#if child.isFolder>
              <a class="ui-link-inherit" href="${Root.path}/doc/${child.id}/@folderish">
            <#else>
              <a class="ui-link-inherit" href="${Root.path}/doc/${child.id}">
            </#if>
            <#if child.common.icon != null && child.common.icon != "">
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}${child.common.icon}" />
            </#if>
                <span>${child.title}</span>
              </a>
        </#list>
        </ul>        
    </div>

</div>

</@block>
</@extends>
