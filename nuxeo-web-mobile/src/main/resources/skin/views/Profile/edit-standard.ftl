<@extends src="base.ftl">

<@block name="content">
<div data-role="dialog">
    <#assign username = userMainInfo.user.username>
    <div data-role="header">
        <h1>${username}'s Profile</h1>
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
                <input type="text" name="user:firstName" value="${userMainInfo.user.firstName}" placeholder="First Name">
              </div>
              <div class="email">
                <input type="text" name="user:lastName" value="${userMainInfo.user.lastName}" placeholder="Last Name">
              </div>
            </div>
            <div class="email">
              <input type="text" name="user:email" value="${userMainInfo.user.email}" placeholder="User Mail">
            </div>
            <div class="groups">
              <label class="gray">Groups:</label>
              <#if userMainInfo.user.groups?size =0>
                <span class="tag">No Group</span>
              <#else>
                <#list userMainInfo.user.groups as group>
                    <span class="tag">${group}</span>
                  </#list>
                </#if>
              </div>
          </div>
            <fieldset class="ui-grid-b">
              <div class="ui-block-a">
                <button data-rel="back" type="submit" class="ui-btn-text" data-theme="b">Save</button>
              </div>
              <div class="ui-block-c">
                <a href="#" data-rel="back" data-role="button" data-theme="c">Cancel</a>
              </div>
            </fieldset>
        </form>
      </div>
    </div>

</div>

</@block>
</@extends>