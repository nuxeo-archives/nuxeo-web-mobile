<@extends src="base.ftl">

<@block name="content">
  <div data-role="page" class="homeNavigation" data-cache="never">
    <script type="text/javascript">
    $(function() {
      $('div').bind('pagehide', function(event) {
        var page = jQuery(event.target);

        if(page.data('cache') == 'never') {
          page.remove();
        };
      });
    });
    </script>

    <div data-role="header">
      <h1>${Context.getMessage('label.header.title.Home')}</h1>
      <a class="ui-btn-right" data-ajax="false" href="#" onclick="goToStandardNavigation('${Root.nuxeoContextPath}');"
        data-iconpos="right" data-icon="home">
          ${Context.getMessage("command.mobile.backDM")}
      </a>
    </div>
    
    <div class="ui-content" data-role="content">
    <h3>${Context.getMessage('label.root.title.UserWorkspace')}</h3>
    <#assign messageEmptyUW = Context.getMessage("label.message.NoDocumentInWorkspace")/>
    <@docListing userWorkspace "${messageEmptyUW}"/>
    </div>
    
    
    <#macro "docListing" docs emptyMessage>
      <#if docs?size == 0>
        <p class="feedback">
         ${emptyMessage}
        </p>
      <#else>
        <ul  class="ui-listview" data-role="listview">
          <#list docs as doc>
            <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
              <#if doc.isFolder>
                <#assign targetView = "/@folderish">
              <#else>
                <#assign targetView = "">
              </#if>
              <a class="ui-link-inherit" data-add-back-btn="true" href="${Root.path}/doc/${doc.id}${targetView}">
                <#if doc.common.icon != null && doc.common.icon != "">
                  <img class="ui-li-icon ui-li-thumb" src="${basePath}/..${doc.common.icon}" />
                <#else>
                  <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/file.gif" />
                </#if>
                <h3>${doc.title}</h3>
                <#if doc.dublincore.description = null || doc.dublincore.description = "" >
                  <p class="ui-li-desc">&nbsp;</p>
                <#else>
                  <p class="ui-li-desc">${doc.dublincore.description}</p>
                </#if>
              </a>
            </li>
          </#list>
        </ul>
      </#if>
    </#macro>
    
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
  </div>
</@block>
</@extends>
