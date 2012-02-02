<@extends src="base.ftl">

<@block name="content">
  <div data-role="page">
    <!--p>
    This is the view corresponding to your root object: ${This.class.simpleName}.
    You can find the code of this view in: src/main/resources/skin/views/${This.class.simpleName}
    To render a view from an WebEngine object you should create @GET annotated method which is returning the view: getView("viewname") where <i>viewname</i> is the file name (without the ftl extension) in the views/ObjectName folder.
    In a view you can access the object instance owning the view using ${r"${This}"} variable or the request context using the ${r"${Context}"} variable.
    Also, you can use @block statements to create reusable layouts.
    </p-->

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
          <a href="/nuxeo/site/mobile/auth/logout" class="ui-link-inherit">
            <img src="${skinPath}/icons/annuaire.png" class="ui-li-thumb">
            <h3 class="ui-li-heading">Search</h3>
            <p class="ui-li-desc">Find your document quickly</p>
          </a>
        </li>
        <li>
          <a href="#" class="ui-link-inherit">
            <img src="${skinPath}/icons/tasks.png" class="ui-li-thumb">
            <h3 class="ui-li-heading">Tasks</h3>
            <p class="ui-li-desc">Check if someone is waiting a action from you</p>
          </a>
        </li>
        <li>
          <a href="#" class="ui-link-inherit">
            <img src="${skinPath}/icons/feed.png" class="ui-li-thumb">
            <h3 class="ui-li-heading">Activity feed</h3>
            <p class="ui-li-desc">See what is happening since the last time you work</p>
          </a>
        </li>
        <li>
          <a href="${Root.path}/profile" class="ui-link-inherit">
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

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>

  </div>
</@block>
</@extends>
