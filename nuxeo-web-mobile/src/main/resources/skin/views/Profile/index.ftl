<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>${userProfile.user.username}'s Profile</h1>
    </div>

    <div data-role="content">

    </div>
    

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
