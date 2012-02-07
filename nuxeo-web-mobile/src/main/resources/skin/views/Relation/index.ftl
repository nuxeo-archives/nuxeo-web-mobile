<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-add-back-btn="true" class="relations">

  <div data-role="header">
    <h1>Document relations</h1>
  </div>
  <div data-role="content">
    <ul class="ui-listview" data-role="listview">
      <#assign relations = Adapter.relations/>
      <#if Adapter.hasRelation()>
        <p class="feedback">
          There is no relation between your document and another item.
        </p>
      </#if>
      <#list relations?keys as label>
        <li data-role="list-divider" role="heading" class="ui-bar-b">
          ${Context.getMessage(label)}
        </li>
        <#list relations[label] as statement>
          <#assign node = statement.objectInfo />
          <li class="ui-btn ui-btn-icon-right ui-li-has-icon ui-li-has-arrow ui-btn-up-c">
            <#if node.documentVisible>
              <a class="ui-link-inherit" href="${Root.path}/doc/${node.documentModel.id}">
            <#elseif node.resource && !node.QNameResource>
              <a class="ui-link-inherit" href="${node.href}">
            <#else>
              <span>
            </#if>

              <#if node.documentModel>
                <#if node.documentModel.common.icon != null && node.documentModel.common.icon != "">
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}/${node.documentModel.common.icon}" />
                </#if>
              </#if>
              <#if node.resource && !node.QNameResource>
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/html.png" />
              </#if>
              <#if node.QNameResource && !node.documentVisible>
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/relation_not_visible" />
              </#if>
              <#if node.literal>
                <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/page_text.gif" />
              </#if>
              <span>
                <#if node.QNameResource>
                  <#if node.documentVisible>
                    ${node.title}
                  </#if>
                  <#if !node.documentVisible>
                    Referenced document not visible
                  </#if>
                </#if>
                <#if node.resource && !node.QNameResource>
                  ${node.title}
                </#if>
                <#if node.literal>
                  ${node.title}
                </#if>
              </span>

            <#if node.documentVisible>
              </a>
            <#elseif node.resource && !node.QNameResource>
              </a>
            <#else>
              </span>
            </#if>
          </li>
        </#list>
      </#list>
    </ul>
  </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
