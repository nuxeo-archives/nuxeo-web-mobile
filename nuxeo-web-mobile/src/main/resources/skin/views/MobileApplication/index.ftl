<@extends src="base.ftl">

<@block name="content">
  <div data-role="page" data-dom-cache="true" class="home">

    <div data-role="header">
      <h1>Home</h1>
    </div>
    <div data-role="content">
      <form method="get" action="${Root.path}/search">
        <input type="search" name="q" id="q" value="" placeholder="Search"/>
        <input type="hidden" name="order" id="order" value="dc:modified DESC" />
        <input type="hidden" name="max" id="max" value="20" />
      </form>
      <div class="ui-grid-a">
        <div class="ui-block-a">
          <a href="${Root.path}/root" data-add-back-btn="true" class="menuItem">
            <img src="${skinPath}/icons/browse.png">
            <p class="">Browse</p>
          </a>
        </div>
        <div class="ui-block-b">
          <a href="${Root.path}/search/faceted" class="menuItem">
            <img src="${skinPath}/icons/searches.png">
            <p class="">Searches</p>
          </a>
        </div>
        <div class="ui-block-a">
          <a href="${Root.path}/profile/${Context.principal.name}" class="menuItem">
            <img src="${skinPath}/icons/profile.png">
            <p class="">Profile</p>
          </a>
        </div>
        <div class="ui-block-b">
          <a data-ajax="false" href="/nuxeo/site/mobile/auth/logout" class="menuItem">
            <img src="${skinPath}/icons/deconnexion.png">
            <p class="">Deconnexion</p>
          </a>
        </div>
      </div>
    </div>

  </div>
</@block>
</@extends>
