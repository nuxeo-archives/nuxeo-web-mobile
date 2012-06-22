<@extends src="base.ftl">

<@block name="content">
  <div data-role="dialog">
    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Comments')}</h1>
    </div>
    <div data-role="content" data-theme="d" class="comments">
      <form method="post">
        <textarea name="newComment" id="newComment" placeholder="${Context.getMessage('label.comment.AddComment')}"></textarea>
        <input type="hidden" name="parentId" value="${parentId}"></input>
        <fieldset class="ui-grid-a">
          <div class="ui-block-a">
            <button data-rel="back" data-theme="b">${Context.getMessage('label.action.Post')}</button>
          </div>
          <div class="ui-block-b">
            <a href="#" data-rel="back" data-role="button" data-theme="c">${Context.getMessage('label.action.Cancel')}</a>
          </div>
        </fieldset>
      </form>
    </div>
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
  </div>
</@block>
</@extends>
