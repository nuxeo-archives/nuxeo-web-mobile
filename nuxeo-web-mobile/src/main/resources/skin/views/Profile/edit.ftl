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
	            <#if userMainInfo.user.firstName != "" && userMainInfo.user.firstName != "">
	              <div class="marginBottom">
	                <input type="text" value="" placeholder="${userMainInfo.user.firstName}">
	              </div>
	              <div class="email">
	                <input type="text" name="name" id="name" value="" placeholder="${userMainInfo.user.lastName}">
	              </div>
	            <#else>
	              <div class="marginBottom">
	                <input type="text" value="" placeholder="First name">
	              </div>
	              <div class="email">
	                <input type="text" name="name" id="name" value="" placeholder="Last name">
	              </div>
	            </#if>
            </div>
            <div class="email">
	            <#if userMainInfo.user.email != "">
	              <input type="text" value="" placeholder="${userMainInfo.user.email}">
	            <#else>
	              <input type="text" value="" placeholder="email address">
	            </#if>
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
          <#if This.isRichProfileDeployed()>
            <div class="moreInfo">
              <ul data-inset="true" data-role="listview" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
                <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
                  <label for="name" class="ui-input-text">Phone number</label>
                  <#if userProfile.userprofile.phonenumber != "">
                    <input type="text"  value="" placeholder="${userProfile.userprofile.phonenumber}">
                  <#else>
                    <input type="text" value="">
                  </#if>
                </li>
                <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
                  <label for="name" class="ui-input-text">Birth Date</label>
                  <#if userProfile.userprofile.birthdate != "">
                    <input type="text"  value="" placeholder="${userProfile.userprofile.birthdate?date}">
                  <#else>
                    <input type="text" value="">
                  </#if>
                </li>
                <li data-role="fieldcontain" class="clear ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
                  <label class="gray ui-input-text">Gender</label>
                  <fieldset data-role="controlgroup">
                    <input type="radio" name="male" id="male" value="male" checked="checked" />
                    <label class="radioLabel" for="male">Male</label>
                    <input type="radio" name="female" id="female" value="female"  />
                    <label class="radioLabel" for="female">Female</label>
                  </fieldset>
                </li>
	            </ul>
            </div>
          </#if>
        </form>
      </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>