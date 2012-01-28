<@extends src="base.ftl">

<@block name="header_scripts">
  <script type="text/javascript" src="${skinPath}/script/jquery.dform-0.1.3.min.js"></script>
  <script type="text/javascript" src="${skinPath}/script/jquery-ui.min.js"></script>
  <script>
    var formdata = ${doc};

    ${r"$(document)"}.ready(function() {
      ${r"$('#myform')"}.buildForm(formdata);
    });
  </script>
  
</@block>

<@block name="stylesheets">
  <link rel="stylesheet" href="${skinPath}/css/nuxeo-dform.css" />
</@block>

<@block name="content">
<div data-role="page">

    <div data-role="header">
        <h1>Page Title</h1>
    </div>

    <div data-role="content">
      <h1>${Document.dublincore.title}</h1>
      
      <div id="myform"></div>
    </div>

    <div data-role="footer">
        <h4>Page Footer</h4>
    </div>
</div>

</@block>
</@extends>
