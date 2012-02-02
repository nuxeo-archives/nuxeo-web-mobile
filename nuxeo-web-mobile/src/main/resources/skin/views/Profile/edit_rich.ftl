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
            <div class="moreInfo">
              <ul data-inset="true" data-role="listview" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
                <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
                  <label for="name" class="ui-input-text">Phone number</label>
                  <input type="text" name="" value="${userProfile.userprofile.phonenumber}" placeholder="Phone Number"></input>
                </li>
                <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
                  <label for="name" class="ui-input-text">Birth Date</label>
                  <input type="text" name="" value="${userProfile.userprofile.birthdate?date}" placeholder="Birth Date">
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
        </form>
      </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>