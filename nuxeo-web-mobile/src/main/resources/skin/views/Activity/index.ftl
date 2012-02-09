<@extends src="base.ftl">


<#macro "activityView" comments>
    <#list activities as activity>
      <li class="white">
        <div class="details">
          <span class="author">
            <a href="${Root.path}/profile/${activity.actor}">${activity.actor}</a> 
          </span>
            ${activity.verb}
          <span class="target">
            <a href="${Root.path}/doc/${activity.target}">${activity.target}</a>
          </span>
        </div>
        
        <div class="comment">${comment.comment.text}</div>
        <#if Adapter.hasAddingCommentRight()>
          <div class="actions">
            <#if Adapter.hasWriteRightOnComment(comment)>
              <a href="#" onclick="$.get('@comment/${comment.id}/@delete', function(data) {alert('success');});">${Context.getMessage('label.action.Delete')}</a> | 
            </#if>
            <a href="@comment/${comment.id}" data-rel="dialog">${Context.getMessage('label.action.Reply')}</a>
          </div>
        </#if>
      </li>
      <li class="details">
        <@childCommentView comments=Adapter.getComments(comment)/>
      </li>
    </#list>
</#macro>

<@block name="content">
<div data-add-back-btn="true" data-role="page">

    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.ActivityStream')}</h1>
    </div>

    <div data-role="content" class="comments">
        <ul>
        <@activityView activities=Adapter.activities/>
        </ul>
    </div>
    
    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
