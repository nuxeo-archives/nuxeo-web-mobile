<#macro "basic" isFixed=true>
    <div <#if isFixed>data-position="fixed"</#if> data-role="footer">
      <div data-role="navbar">
        <ul>
          <li><a href="${Root.path}"><img alt="Home" src="${skinPath}/icons/footer_home.png" /></a></li>
          <li><a href="${Root.path}/root"><img alt="Personal Workspace" src="${skinPath}/icons/footer_browse.png" /></a></li>
          <li><a href="${Root.path}/profile/${Context.principal.name}"><img alt="Profile" src="${skinPath}/icons/footer_profile.png" /></a></li>
          <li><a href="${Root.path}/search/faceted"><img alt="Search" src="${skinPath}/icons/footer_search.png" /></a></li>
          <li><a data-ajax="false" href="#" onclick="goToStandardNavigation('${Root.nuxeoContextPath}');">
            <img alt="Standard_Navigation" src="${skinPath}/icons/footer_standard_navigation.png" />
          </a></li>
          <#if Context.getProperty('Cordova')??>
          <li><a href="javascript:Cordova.openFileChooser();"><img alt="Cordova" src="${skinPath}/icons/footer_search.png" /></a></li>
          </#if>
        </ul>
      </div>
    </div>
</#macro>
