<@extends src="base.ftl">

<@block name="content">
<div data-role="dialog">
    <#assign username = userMainInfo.user.username>
    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Edit')}</h1>
    </div>

    <div data-role="content" class="profile">
            <div class="white nospace profileDetail">
        <form action="${username}?mode=edit" method="post">
          <div class="avatar">
            <img src="${This.getAvatarURI(username)}" alt="Avatar">
          </div>
          <div class="profileInfo clear">
            <div class="name">
              <div class="marginBottom">
                <input type="text" name="user:firstName" value="${userMainInfo.user.firstName}" placeholder="${Context.getMessage('label.form.FirstName')}">
              </div>
              <div class="email">
                <input type="text" name="user:lastName" value="${userMainInfo.user.lastName}" placeholder="${Context.getMessage('label.form.LastName')}">
              </div>
            </div>
            <div class="email">
              <input type="text" name="user:email" value="${userMainInfo.user.email}" placeholder="${Context.getMessage('label.form.Email')}">
            </div>
            <div class="groups">
              <label class="gray">${Context.getMessage('label.form.Groups')}</label>
              <#if userMainInfo.user.groups?size =0>
                <span class="tag">${Context.getMessage('label.message.NoGroup')}</span>
              <#else>
                <#list userMainInfo.user.groups as group>
                    <span class="tag">${group}</span>
                  </#list>
                </#if>
              </div>
          </div>
            <fieldset class="ui-grid-a">
              <div class="ui-block-a">
                <button data-rel="back" type="submit" class="ui-btn-text" data-theme="b">${Context.getMessage('label.action.Save')}</button>
              </div>
              <div class="ui-block-b">
                <a href="#" data-rel="back" data-role="button" data-theme="c">${Context.getMessage('label.action.Cancel')}</a>
              </div>
            </fieldset>
        </form>
      </div>
    </div>

</div>

</@block>
</@extends>