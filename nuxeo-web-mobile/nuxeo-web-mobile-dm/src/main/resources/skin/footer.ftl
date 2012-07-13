<#macro "basic">
    <div data-id="footer" data-position="fixed" data-role="footer">
      <div data-role="navbar">
        <ul>
          <li><a href="${Root.path}"><img alt="Home" src="${skinPath}/icons/footer_home.png" /></a></li>
          <li><a href="${Root.path}/root"><img alt="Personal Workspace" src="${skinPath}/icons/footer_browse.png" /></a></li>
          <li><a href="${Root.path}/profile/${Context.principal.name}"><img alt="Profile" src="${skinPath}/icons/footer_profile.png" /></a></li>
          <li><a href="${Root.path}/search/faceted"><img alt="Search" src="${skinPath}/icons/footer_search.png" /></a></li>
          <!-- Disabled for now ...
          <li><a data-ajax="false" href="#" onclick="goToStandardNavigation('${Root.nuxeoContextPath}');">
            <img alt="Standard_Navigation" src="${skinPath}/icons/footer_standard_navigation.png" />
          </a></li>
          -->
          <li>
          <#if Context.getProperty('Cordova')??>
            <a data-ajax="false" href="javascript:NXCordova.logout();" class="menuItem">
              <img src="${skinPath}/icons/footer_logout.png">
            </a>
          <#else>
            <a data-ajax="false" href="${Root.path}/auth/logout" class="menuItem">
              <img src="${skinPath}/icons/footer_logout.png">
            </a>
          </#if>
          </li>
        </ul>
      </div>
    </div>
</#macro>
