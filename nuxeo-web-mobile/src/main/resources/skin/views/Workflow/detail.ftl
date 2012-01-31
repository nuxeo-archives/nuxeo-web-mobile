<@extends src="base.ftl">

<@block name="content">
<div data-role="page">

    <div data-role="header">
      <h1>Page Title</h1>
    </div>

    <div data-role="content">
      <ul data-role="listview" data-inset="true">
        <li>
          <label>Action type</label>
          <span>${task.getDescription()}</span>
        </li>
        <li>
          <label>Due Date</label>
          <span>${task.getDueDate()}</span>
        </li>
      </ul>      
    </div>

    <div data-role="footer">
      <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
