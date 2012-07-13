<@extends src="base.ftl">

<@block name="content">
<div data-role="page">

    <div data-role="header">
      <h1>${Context.getMessage('label.header.title.Details')}</h1>
    </div>

    <div data-role="content">
      <ul data-role="listview" data-inset="true">
        <li>
          <label>${Context.getMessage('label.form.ActionType')}</label>
          <span>${task.getDescription()}</span>
        </li>
        <li>
          <label>${Context.getMessage('label.form.DueDate')}</label>
          <span>${task.getDueDate()}</span>
        </li>
      </ul>      
    </div>

    <div data-role="footer">
      <h4>Page Footer</h4>
    </div>
    <#import "/footer.ftl" as footer/>
    <@footer.basic />
</div>

</@block>
</@extends>
