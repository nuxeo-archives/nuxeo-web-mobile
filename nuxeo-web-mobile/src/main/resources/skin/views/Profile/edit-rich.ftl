<@extends src="base.ftl">

<@block name="content">
<div data-role="dialog">
    <#assign username = userMainInfo.user.username>
    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Edit')}</h1>
    </div>

    <div data-role="content" class="profile">
            <div class="white nospace profileDetail">
        <form method="post">
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
            <div class="moreInfo">
              <ul data-inset="true" data-role="listview" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
                <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
                  <label for="name" class="ui-input-text">${Context.getMessage('label.form.Phone')}</label>
                  <input type="text" name="" value="${userProfile.userprofile.phonenumber}" placeholder="${Context.getMessage('label.form.Phone')}"></input>
                </li>
	            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
	              <label for="name" class="ui-input-text">${Context.getMessage('label.form.Birth')}</label>
	              <#if userProfile.userprofile.birthdate != "">
	                <span>${userProfile.userprofile.birthdate?date}</span>
	              </#if>
	            </li>
	            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
	              <label for="name" class="ui-input-text">${Context.getMessage('label.form.Gender')}</label>
	              <#if userProfile.userprofile.gender = true>
	                <span>${Context.getMessage('label.form.GenderMale')}</span>
	              <#else>
	                <span>${Context.getMessage('label.form.GenderFemale')}</span>
	              </#if>
	            </li>
                <!--li data-role="fieldcontain" class="clear ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
                  <label class="gray ui-input-text">${Context.getMessage('label.form.Gender')}</label>
                  <fieldset data-role="controlgroup">
                    <input type="radio" name="true" id="true" value="true" checked="checked" />
                    <label class="radioLabel" for="true">${Context.getMessage('label.form.GenderMale')}</label>
                    <input type="radio" name="false" id="false" value="false"  />
                    <label class="radioLabel" for="false">${Context.getMessage('label.form.GenderFemale')}</label>
                  </fieldset>
                </li-->
                </ul>
            </div>
            <fieldset class="ui-grid-a">
              <div class="ui-block-a">
                <button data-rel="back" class="ui-btn-text" data-theme="b">${Context.getMessage('label.action.Save')}</button>
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