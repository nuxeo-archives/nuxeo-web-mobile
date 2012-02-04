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
      <li class="details">
        <@childCommentView comments=Adapter.getComments(comment)/>
      </li>
    </#list>
</#macro>

<@block name="content">
<div data-add-back-btn="true" data-role="page">

    <div data-role="header">
        <h1>Comments</h1>
    </div>

    <div data-role="content" class="comments">
        <ul>
        <@childCommentView comments=Adapter.comments/>
        </ul>
        <#if Adapter.hasAddingCommentRight()>
          <form id="newComment" method="post">
            <textarea name="newComment" id="newComment" placeholder="Add your own comment"></textarea>
            <button data-inline="true" data-icon="check">Post your comment</button>
          </form>
        </#if>
    </div>
    
    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
