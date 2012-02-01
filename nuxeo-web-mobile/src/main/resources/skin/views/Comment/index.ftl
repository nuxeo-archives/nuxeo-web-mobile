<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Comments</h1>
    </div>

    <div data-role="content" class="comments">
        <ul>
        <#list Adapter.comments as comment>
          <li class="white">
            <div class="header clear">
              <div class="details">
                <span class="author"><a href="#">${comment.comment.author}</a></span><span class="time">${comment.comment.creationDate.time?datetime}</span>
              </div>
              <#if Adapter.hasAddingCommentRight()>
                <div class="actions">
                  <a href="#">reply</a> | <#if Adapter.hasWriteRightOnComment(comment)><a href="#">remove</a></#if>
                </div>
              </#if>
            </div>
            <div class="comment">${comment.comment.text}</div>
          </li>
        </#list>
        </ul>
        <#if Adapter.hasAddingCommentRight()>
          <form id="newComment" method="post">
            <textarea name="newComment" id="newComment" placeholder="Add your own comment"></textarea>
            <button data-inline="true" data-icon="check">Post your comment</button>
          </form>
        </#if>
        
    </div>
    

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
