<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
        <ul data-role="listview" data-inset="true">
        <#list Adapter.comments as comment>
          <li>
            <div class="commentHeader">
              ${comment.comment.creationDate.time?datetime} - ${comment.comment.author} wrote:
            </div> 
            <div class="commentText">${comment.comment.text}</div>
          </li>
        </#list>
        </ul>
        <#if Adapter.hasAddingCommentRight()>
          <form id="newComment" method="post">
            <textarea name="textarea" id="textarea-a" placeholder="Add your own comment"></textarea>
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
