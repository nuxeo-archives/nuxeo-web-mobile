<@extends src="base.ftl">

<@block name="content">
<div data-fullscreen="true" data-role="page">

    <div data-position="fixed" data-role="header">
        <h1>${Adapter.previewURL}</h1>
    </div>

    <div data-role="content">
       <#if This.hasPreview()>
         <iframe src="${Adapter.previewURL}" class="preview"/>
       <#else>
         <br/>
         <br/>
         <br/>
         <br/>
         The preview is not available for this type of document.
       </#if>
    </div>

</div>

</@block>
</@extends>
