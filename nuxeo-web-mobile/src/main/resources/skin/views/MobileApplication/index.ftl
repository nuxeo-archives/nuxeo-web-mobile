<@extends src="base.ftl">

<@block name="content">
  <div data-role="page" data-dom-cache="true">

    <div data-role="header">
      <h1>Page Title</h1>
    </div>
    <div data-role="content">
      <ul data-role="listview" class="ui-listview">
        <li>
          <form method="get" action="${Root.path}/search">
            <input type="search" name="q" id="q" value="" placeholder="Fulltext Search"/>
            <input type="hidden" name="order" id="order" value="dc:modified DESC" />
            <input type="hidden" name="max" id="max" value="20" />
          </form>
        </li>
        <li>
          <a href="${Root.path}/docPath/@folderish" class="ui-link-inherit">
            <img src="${skinPath}/icons/browse.png" class="ui-li-thumb">
            <h3 class="ui-li-heading">Browse</h3>
            <p class="ui-li-desc">Dive into your tree</p>
          </a>
        </li>
        <li>
          <a href="${Root.path}/search/faceted" class="ui-link-inherit">
            <img src="${skinPath}/icons/annuaire.png" class="ui-li-thumb">
            <h3 class="ui-li-heading">Search</h3>
            <p class="ui-li-desc">Find your document quickly</p>
          </a>
        </li>
        <li>
          <a href="${Root.path}/profile/${Context.principal.name}" class="ui-link-inherit">
            <img src="${skinPath}/icons/profile.png" class="ui-li-thumb">
            <h3 class="ui-li-heading">Profile</h3>
            <p class="ui-li-desc">Enrich your profile</p>
          </a>
        </li>
        <li>
          <a data-ajax="false" href="/nuxeo/site/mobile/auth/logout">
            <img src="${skinPath}/icons/deconnexion.png" class="ui-li-thumb">
            <h3 class="ui-li-heading">Deconnexion</h3>
            <p class="ui-li-desc">Log out your application</p>
          </a>
        </li>
      </ul>
    </div>

  </div>
</@block>
</@extends>
