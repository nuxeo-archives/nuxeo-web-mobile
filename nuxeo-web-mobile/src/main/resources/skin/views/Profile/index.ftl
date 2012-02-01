<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>${userProfile.user.username}'s Profile</h1>
    </div>

    <div data-role="content" class="profile">
      <div class="white nospace profileDetail">
        <div class="avatar">
          <img src="/nuxeo/site/skin/nuxeo/icons/default_avatar.png" alt="Avatar">
        </div>
        <div class="profilInfo clear">
          <div class="name">Benjamin Jalon</div>
          <div class="email"><a href="mailto:bjalon@nuxeo.com">bjalon@nuxeo.com</a></div>
          <div class="groups">
            <span class="tag">Nuxeo</span>
            <span class="tag">Starship</span>
            <span class="tag">Atlantis</span>
          </div>
        </div>
        <div class="moreInfo">
          <ul data-inset="true" data-role="listview" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
              <label for="name" class="ui-input-text">Phone number</label>
              <span><a href="tel:0666996699">0666996699</a></span>
            </li>
            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
              <label for="name" class="ui-input-text">Birth Date</label>
              <span>2012/12/12</span>
            </li>
            <li data-role="fieldcontain" class="ui-field-contain ui-body ui-br ui-li ui-li-static ui-body-c ui-corner-top ui-btn-up-c">
              <label for="name" class="ui-input-text">Gender</label>
              <span>Male</span>
            </li>
	        <ul>
        </div>
      </div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
