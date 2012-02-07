<@extends src="base.ftl">
<#import "../../footer.ftl" as footer/>

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>Repository</h1>
    </div>

    <div data-role="content" class="repository">

      <h2>Domain</h2>
      <@docListing domain "No Document found in Domain"/> 

      <h2>User Workspace</h2>
      <@docListing userwokspace "No Document found in your Workspace"/> 

    </div>

    <@footer.basic />
  </div>

</@block>
</@extends>

<#macro "docListing" docs emptyMessage>
      <#if docs?size == 0>
        <p class="feedback">
         <h3>${emptyMessage}</h3>
        </p>
      <#else>
        <ul  class="ui-listview ui-listview-inset ui-corner-all ui-shadow" data-inset="true" data-role="listview">
          <#list docs as doc>
            <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <#if doc.isFolder>
                <#assign targetView = "/@folderish">
              <#else>
                <#assign targetView = "">
              </#if> 
              <a class="ui-link-inherit" data-add-back-btn="true" href="${Root.path}/doc/${doc.id}${targetView}">
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
</#macro>