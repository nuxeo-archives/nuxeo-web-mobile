<@extends src="base.ftl">

<@block name="content">
<div data-role="page">
    <#assign username = userMainInfo.user.username>
    <div data-role="header">
        <h1>${username}'s Profile</h1>
    </div>

    <div data-role="content" class="profile">
            <div class="white nospace profileDetail">
        <form>
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
        </form>
      </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>