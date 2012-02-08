<@extends src="base.ftl">


<#macro "childCommentView" comments>
    <#list comments as comment>
      <li class="white">
        <div class="details">
          <span class="author">
            <a href="${Root.path}/profile/${comment.comment.author}">${comment.comment.author}</a>
            </span><span class="time">${comment.comment.creationDate.time?datetime}</span>
          </span>
        </div>
        <div class="comment">${comment.comment.text}</div>
        <#if Adapter.hasAddingCommentRight()>
          <div class="actions">
            <#if Adapter.hasWriteRightOnComment(comment)>
              <a href="#" onclick="$.get('@comment/${comment.id}/@delete', function(data) {alert('success');});">Delete</a> | 
            </#if>
            <a href="@comment/${comment.id}" data-rel="dialog">Reply</a>
          </div>
        </#if>
      </li>
      <@childCommentView comments=Adapter.getComments(comment)/>
    </#list>
</#macro>

<@block name="content">
<div data-add-back-btn="true" data-role="page">

    <div data-role="header">
        <h1>Comments</h1>
        <#if Adapter.hasWriteRightOnComment(This.document)>
          <a href="@comment/null" class="ui-btn-right squared">
            <img src="${skinPath}/icons/add.png"/>
          </a>
        </#if>
    </div>

    <div data-role="content" class="comments">
        <#if Adapter.comments?size == 0>
          <p class="feedback">
            There is no comment on this document.
          </p>
        </#if>
        <ul>
          <@childCommentView comments=Adapter.comments/>
        </ul>
    </div>
    
    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
