<@extends src="base.ftl">

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Creation')}</h1>
    </div>

    <div data-role="content">
      TODO creation Mode
    </div>

    <#import "../../footer.ftl" as footer/>
    <@footer.basic false/>
</div>

</@block>
</@extends>
