<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-add-back-btn="true">
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
          <div class="name">
	          <#if userMainInfo.user.firstName != "" && userMainInfo.user.firstName != "">
	            ${userMainInfo.user.firstName} ${userMainInfo.user.lastName}
	          <#else>
	            No information set for First Name/Last Name
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
        <div class="moreInfo">
          <ul data-inset="true" data-role="listview" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
              <label for="name" class="ui-input-text">Phone number</label>
                <span><a href="tel:${userProfile.userprofile.phonenumber}">${userProfile.userprofile.phonenumber}</a></span>
            </li>
            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
              <label for="name" class="ui-input-text">Birth Date</label>
              <#if userProfile.userprofile.birthdate != "">
                <span>${userProfile.userprofile.birthdate?date}</span>
              </#if>
            </li>
            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
              <label for="name" class="ui-input-text">Gender</label>
              <#if userProfile.userprofile.gender = true>
                <span>Male</span>
              <#else>
                <span>Female</span>
              </#if>
            </li>
	        <ul>
        </div>
      <fieldset class="ui-grid-b">
        <div class="ui-block-a">
          <a href="?mode=edit" data-role="button">Edit</a>
        </div>
        <div class="ui-block-c">
          <a href="?mode=password" data-role="button">Password</a>
        </div>
      </fieldset>

      </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
