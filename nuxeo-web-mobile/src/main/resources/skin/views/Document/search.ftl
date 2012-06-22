<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Search')}</h1>
    </div>

    <div data-role="content">
        <ul data-role="listview" data-inset="true">
        <#if result?size == 0>
          <p class="feedback">
            ${Context.getMessage('label.message.NoDocumentMatches')}
          </p>
        </#if>
        <#list result as doc>
          <li>
            <a href="${Root.path}/doc/${doc.id}">
              <#if doc.common.icon != null && doc.common.icon != "">
                <img src="${skinPath}${doc.common.icon}" alt="icon1" class="ui-li-icon"/>
              </#if>
              <span class="ui-li-count">${doc.title}</span>
            </a>
          </li>
        </#list>
        </ul>
    </div>
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
