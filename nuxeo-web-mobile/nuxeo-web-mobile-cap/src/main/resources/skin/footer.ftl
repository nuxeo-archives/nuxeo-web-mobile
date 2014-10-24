<#macro "basic">
  <#assign currentPage = Context.getProperty('currentPage')>
<div data-id="footer" data-position="fixed" data-role="footer">
  <div data-role="navbar">
    <ul>
      <li>
        <a href="${Root.path}">
          <span class="icon-footer icons-footer-home<#if currentPage == 'HOME'> selected</#if>">&nbsp;</span>
        </a>
      </li>
      <li>
        <a href="${Root.path}/root">
          <span class="icon-footer icons-footer-browse<#if currentPage == 'BROWSE'> selected</#if>">&nbsp;</span>
        </a>
      </li>
      <li>
        <a href="${Root.path}/profile/${Context.principal.name}">
          <span class="icon-footer icons-footer-profile<#if currentPage == 'PROFILE'> selected</#if>">&nbsp;</span>
        </a>
      </li>
      <li>
        <a href="${Root.path}/search/faceted">
          <span class="icon-footer icons-footer-search<#if currentPage == 'SEARCH'> selected</#if>">&nbsp;</span>
        </a>
      </li>
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
