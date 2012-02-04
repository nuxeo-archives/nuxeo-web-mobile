<#macro "basic" isFixed=true>
    <div <#if isFixed>data-position="fixed"</#if> data-role="footer">
      <div data-role="navbar">
        <ul>
          <li><a href="${Root.path}"><img alt="Home" src="${skinPath}/icons/footer_home.png" /></a></li>
          <li><a href="${Root.path}/docPath/@folderish"><img alt="Personal Workspace" src="${skinPath}/icons/footer_personalworkspace.png" /></a></li>
          <li><a href="${Root.path}/profile"><img alt="Profile" src="${skinPath}/icons/footer_profile.png" /></a></li>
          <li><a href="${Root.path}/search/faceted"><img alt="Search" src="${skinPath}/icons/footer_search.png" /></a></li>
        </ul>
      </div>
    </div>
</#macro>
