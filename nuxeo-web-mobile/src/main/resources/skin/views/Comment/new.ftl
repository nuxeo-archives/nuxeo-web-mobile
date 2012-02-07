<@extends src="base.ftl">

<@block name="content">
  <div data-role="dialog">
    <div data-role="header">
        <h1>Comment</h1>
    </div>
    <div data-role="content" data-theme="d" class="comments">
      <form method="post">
        <textarea name="newComment" id="newComment" placeholder="Add your own comment"></textarea>
        <input type="hidden" name="parentId" value="${parentId}"></input>
        <fieldset class="ui-grid-a">
          <div class="ui-block-a">
            <button data-rel="back" data-theme="b">Post</button>
          </div>
          <div class="ui-block-b">
            <a href="#" data-rel="back" data-role="button" data-theme="c">Cancel</a>
          </div>
        </fieldset>
      </form>
    </div>
  </div>
</@block>
</@extends>
