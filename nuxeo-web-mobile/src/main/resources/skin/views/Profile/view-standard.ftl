<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-theme="d" data-add-back-btn="true">
    <#assign username = userMainInfo.user.username>
    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Profile')}</h1>
    </div>

    <div data-role="content" class="profile">
      <div class="white nospace profileDetail">
        <div class="avatar">
          <img src="${This.getAvatarURI(username)}" alt="Avatar">
        </div>
        <div class="profileInfo clear">
          <#if userMainInfo.user.firstName != "" && userMainInfo.user.firstName != "">
            <div class="name">
              ${userMainInfo.user.firstName} ${userMainInfo.user.lastName}
            </div>
              <#else>
            <div class="name gray">
                John Doe
            </div>
              </#if>
          </div>
          <div class="email">
              <#if userMainInfo.user.email != "">
                <a href="mailto:${userMainInfo.user.email}">${userMainInfo.user.email}</a>
              <#else>
                ${Context.getMessage('label.message.NoInfo')}
              </#if>
          </div>
          <div class="groups">
              <#if userMainInfo.user.groups?size =0>
                <span class="tag">${Context.getMessage('label.message.NoInfo')}</span>
              <#else>
                <#list userMainInfo.user.groups as group>
                  <span class="tag">${group}</span>
                </#list>
              </#if>
          </div>
        </div>
      <fieldset class="ui-grid-a">
        <div class="ui-block-a">
          <a href="?mode=edit" data-role="button" data-theme="c">${Context.getMessage('label.action.Edit')}</a>
        </div>
        <div class="ui-block-b">
          <a href="?mode=password" data-role="button" data-theme="c">${Context.getMessage('label.action.Password')}</a>
        </div>
      </fieldset>

      </div>
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
