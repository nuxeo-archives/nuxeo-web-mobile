<@extends src="base.ftl">

<@block name="content">
  <div data-role="page" data-dom-cache="true">

    <div data-role="header">
      <h1>Page Title</h1>
    </div>
    <div data-role="content" class="home">
      <form method="get" action="${Root.path}/search">
        <input type="search" name="q" id="q" value="" placeholder="Search"/>
        <input type="hidden" name="order" id="order" value="dc:modified DESC" />
        <input type="hidden" name="max" id="max" value="20" />
      </form>
      <div class="ui-grid-a">
        <div class="ui-block-a">
          <a href="${Root.path}/root" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c">
            <img src="${skinPath}/icons/browse.png" class="">
            <p class="">Browse</p>
          </a>
        </div>
        <div class="ui-block-b">
          <a href="${Root.path}/search/faceted" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c">
            <img src="${skinPath}/icons/annuaire.png" class="">
            <p class="">Search</p>
          </a>
        </div>
        <div class="ui-block-a">
          <a href="${Root.path}/profile/${Context.principal.name}" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c">
            <img src="${skinPath}/icons/profile.png" class="">
            <p class="">Profile</p>
          </a>
        </div>
        <div class="ui-block-b">
          <a data-ajax="false" href="/nuxeo/site/mobile/auth/logout" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c">
            <img src="${skinPath}/icons/deconnexion.png" class="">
            <p class="">Deconnexion</p>
          </a>
        </div>
      </div>
    </div>

  </div>
</@block>
</@extends>
