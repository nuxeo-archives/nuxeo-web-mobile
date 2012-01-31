<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

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
         THIS DOCUMENT HAS NO PREVIEW
       </#if>
    </div>

    <div data-position="fixed" data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
