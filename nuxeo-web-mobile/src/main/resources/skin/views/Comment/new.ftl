<@extends src="base.ftl">

<@block name="content">
<div data-role="dialog">
    <div data-role="content" class="comments">
      <form method="post">
        <textarea name="newComment" id="newComment" placeholder="Add your own comment"></textarea>
        <input type="hidden" name="parentId" value="${parentId}"></input>
        <button data-rel="back" data-inline="true" data-icon="check">Post your comment</button>
      </form>
    </div>
</div>
</@block>
</@extends>
