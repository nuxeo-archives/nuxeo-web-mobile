<@extends src="base.ftl">

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>${Context.getMessage('label.header.title.Tasks')}</h1>
    </div>

    <div data-role="content">
        <h4>${Context.getMessage('label.task.title.tasks')}</h4>
        <ul data-role="listview" data-inset="true">
        <#if This.myTasks?size == 0>
          <li>${Context.getMessage('label.message.NoTask')}</li>
        <#else>
           <#list This.myTasks as task>
             <li>
               <a href="${Root.path}/task/byId/${task.id?string("0.#")}">${task.name}</a>
             </li>
           </#list>
        </#if>
        </ul>        
        <h4>${Context.getMessage('label.task.title.workflows')}</h4>
        <ul data-role="listview" data-inset="true">
          <li>${Context.getMessage('label.message.NoWorkflow')}</li>
        </ul>        
    </div>

    <#import "/footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
