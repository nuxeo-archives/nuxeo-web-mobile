<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-role="header">
        <h1>Annotations</h1>
    </div>

    <div data-role="content" class="annotations">
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
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
