<@extends src="base.ftl">

<@block name="content">
<div data-role="page" data-add-back-btn="true">

    <div data-position="fixed" data-role="header">
        <h1>${Adapter.previewURL}</h1>
    </div>

    <div data-role="content">
       <#if This.document.schemas?seq_contains("note")>
         ${This.document.note.note}
       </#if>
    </div>

</div>

</@block>
</@extends>
