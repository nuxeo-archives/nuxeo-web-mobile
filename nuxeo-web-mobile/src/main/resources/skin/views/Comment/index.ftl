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
            <div class="details">
              <span class="author"><a href="#">${comment.comment.author}</a></span><span class="time">${comment.comment.creationDate.time?datetime}</span>
            </div>
            <div class="comment">${comment.comment.text}</div>
            <#if Adapter.hasAddingCommentRight()>
              <div class="actions">
                <#if Adapter.hasWriteRightOnComment(comment)><a href="#">Delete</a> | </#if><a href="#">Reply</a>
              </div>
            </#if>
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
