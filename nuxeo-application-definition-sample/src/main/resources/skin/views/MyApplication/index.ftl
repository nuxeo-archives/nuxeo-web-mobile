<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">

<div style="margin: 10px 10px 10px 10px">
<p>
This is an example of webengine application that all users to go to this application. Handler select all requests.
</p>

<p>
If you want to change the type of request that must be redirected to this application modify the ApplicationHandler.java class and make it return true for the condition you want.
</p>

<p>
Override the login.ftl page with the specific style you want for your application, but the form action must be the same, must be post type, login value must be stored into user_login and password into user_password.
</p>

<p>
For more information please look the nuxeo-application-definition documentation.
</p>

<p>
Here is the <a href="/nuxeo/site/myApplication/logout">Logout</a> link.
</p>

</div>

</@block>
</@extends>
