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
          <div class="email">
              <#if userMainInfo.user.email != "">
                <a href="mailto:${userMainInfo.user.email}">${userMainInfo.user.email}</a>
              <#else>
                ${Context.getMessage('label.message.NoInfo')}
              </#if>
          </div>
        </div>
        <div class="moreInfo">
          <ul data-inset="true" data-role="listview" data-theme="c" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
              <label for="name" class="ui-input-text">${Context.getMessage('label.form.Phone')}</label>
                <span><a href="tel:${userProfile.userprofile.phonenumber}">${userProfile.userprofile.phonenumber}</a></span>
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
            <ul>
        </div>

      </div>
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic false/>
</div>

</@block>
</@extends>
