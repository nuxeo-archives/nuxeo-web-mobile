<@extends src="base.ftl">

<@block name="content">
<div data-role="page">
    <#assign username = userMainInfo.user.username>
    <div data-role="header">
        <h1>${username}'s Profile</h1>
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
	            No Mail information
	          </#if>
          </div>
          <div class="groups">
              <#if userMainInfo.user.groups?size =0>
                <span class="tag">No Group</span>
              <#else>
                <#list userMainInfo.user.groups as group>
	              <span class="tag">${group}</span>
	            </#list>
	          </#if>
	      </div>
        </div>

      </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
