<@extends src="base.ftl">

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
        <h4>My Tasks</h4>
        <ul data-role="listview" data-inset="true">
        <#if This.myTasks?size == 0>
          <li>No Task found</li>
        <#else>
           <#list This.myTasks as task>
             <li>
               <a href="${Root.path}/task/byId/${task.id?string("0.#")}">${task.name}</a>
             </li>
           </#list>
        </#if>
        </ul>        
        <h4>My Workflows</h4>
        <ul data-role="listview" data-inset="true">
          <li>No Workflow found</li>
        </ul>        
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
