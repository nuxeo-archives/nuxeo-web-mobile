<#macro "basic">
    <div data-id="footer" data-position="fixed" data-role="footer">
      <div data-role="navbar">
        <ul>
          <li>
            <a href="${Root.path}">
              <span class="icon-footer icons-footer-home">&nbsp;</span>
            </a>
          </li>
          <li>
            <a href="${Root.path}/root">
              <span class="icon-footer icons-footer-browse">&nbsp;</span>
            </a>
          </li>
          <li>
            <a href="${Root.path}/profile/${Context.principal.name}">
              <span class="icon-footer icons-footer-profile">&nbsp;</span>
            </a>
          </li>
          <li>
            <a href="${Root.path}/search/faceted">
              <span class="icon-footer icons-footer-search">&nbsp;</span>
            </a>
          </li>
          <!-- Disabled for now ...
          <li><a data-ajax="false" href="#" onclick="goToStandardNavigation('${Root.nuxeoContextPath}');">
            <img alt="Standard_Navigation" src="${skinPath}/icons/footer_standard_navigation.png" />
          </a></li>
          -->
          <li>
          <#if Context.getProperty('Cordova')??>
            <a data-ajax="false" href="javascript:NXCordova.logout();">
              <span class="icon-footer icons-footer-logout">&nbsp;</span>
            </a>
          <#else>
            <a data-ajax="false" href="${Root.path}/auth/logout">
              <span class="icon-footer icons-footer-logout">&nbsp;</span>
            </a>
          </#if>
          </li>
        </ul>
      </div>
    </div>
</#macro>
