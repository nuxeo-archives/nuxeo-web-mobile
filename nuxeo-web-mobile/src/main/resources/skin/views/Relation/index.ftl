<@extends src="base.ftl">

<@block name="content">
<div data-role="page">

  <div data-role="header">
    <h1>Document relations</h1>
  </div>
  <div data-role="content">
    <ul class="ui-listview" data-role="listview">
      <li data-role="list-divider" role="heading" class="ui-li ui-li-divider ui-btn ui-bar-b ui-btn-up-undefined">Conforms to</li>
      <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        <a class="ui-link-inherit" href="#">
          <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/pdf.png" />
          <span>Mon document number one</span>
        </a>
      </li>
      <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        <a class="ui-link-inherit" href="#">
          <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/pdf.png" />
          <span>Mon document number two</span>
        </a>
      </li>
      <li data-role="list-divider" role="heading" class="ui-li ui-li-divider ui-btn ui-bar-b ui-btn-up-undefined">Based on</li>
      <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        <a class="ui-link-inherit" href="#">
          <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/pdf.png" />
          <span>Mon document number three</span>
        </a>
      </li>
      <li data-role="list-divider" role="heading" class="ui-li ui-li-divider ui-btn ui-bar-b ui-btn-up-undefined">References</li>
      <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        <a class="ui-link-inherit" href="#">
          <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/pdf.png" />
          <span>Mon document number four</span>
        </a>
      </li>
      <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        <a class="ui-link-inherit" href="#">
          <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/doc.png" />
          <span>A text to explain the reference</span>
        </a>
      </li>
      <li data-role="list-divider" role="heading" class="ui-li ui-li-divider ui-btn ui-bar-b ui-btn-up-undefined">Replaces</li>
      <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        <a class="ui-link-inherit" href="#">
          <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/doc.png" />
          <span>My old cat</span>
        </a>
      </li>
      <li data-role="list-divider" role="heading" class="ui-li ui-li-divider ui-btn ui-bar-b ui-btn-up-undefined">Requires</li>
      <li class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-li-has-count ui-li-has-icon ui-btn-up-c">
        <a class="ui-link-inherit" href="#">
          <img class="ui-li-icon ui-li-thumb" src="${skinPath}/icons/html.png" />
          <span>http://www.google.com</span>
        </a>
      </li>
    </ul>
  </div>

</div>

</@block>
</@extends>
