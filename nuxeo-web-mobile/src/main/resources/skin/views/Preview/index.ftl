<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>${Adapter.previewURL}</h1>
    </div>

    <div data-role="content">
       
       <iframe src="${Adapter.previewURL}" frameborder="1" width="100%" scrolling="auto" class="preview">
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
