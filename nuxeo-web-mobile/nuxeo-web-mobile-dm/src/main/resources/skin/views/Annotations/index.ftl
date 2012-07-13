<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Annotations')}</h1>
    </div>

    <div data-role="content" class="annotations">
        <#if Adapter.annotations?size == 0>
          <p class="feedback">
            ${Context.getMessage('label.message.NoAnnotation')}
          </p>
        <#else>
        <ul>
        <#list Adapter.annotations as annotation>
          <li class="white">
            <div class="details">
              <span class="author">
                <a href="${Root.path}/profile/${annotation.creator}">${annotation.creator}</a>
              </span>
            </div>
            <div class="annotation">${annotation.bodyAsText}</div>
          </li>
        </#list>
        </ul>
      </#if>
    </div>
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
