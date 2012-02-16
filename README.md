# Nuxeo Web Mobile - when WebEngine meet JQuery Mobile

This add-on provides a WebApp that expose Nuxeo with a dedicate view for Mobile browser. All Mobile browser will automatically redirected to this application after its deployment.
This Application is in alpha version, Nuxeo will not garantee (at all) the API as we think to work on split the data and the UI restitution.
You are welcome to test it and if you install it you will be no problem to move to the next version, but if you develop on the top of it you will be alone to move your code :D

About the Application Nuxeo Web Mobile: we expose a subset of funconnalities of Nuxeo DM. Here is an overview of what you can do:

- Redirection automatically to the Mobile Application
- Redirection to the document into the Mobile application when JSF request asked
- Dedicated Login page
- About Document you will have access to different view of this document:
  - Subset of Metadata
  - Preview
  - Comments
  - Annotation (just the text, not into the preview context)
  - Relation
  - Content
  - The list of contributor to each document. Click on the user will show you their information, especially phone number and mail. You will have the possibility to send email directly to him or call him just clicking on the item.
- You also have some default actions enabled:
  - Mail it (to send the link to a user into your calendar)
  - Download it (Download the file attached)
  - Mail me (to ask to the server to send to yourself - using the mail into the Nuxeo account - the File attached and the link to the document)
- Profile view
  - You have access to all user profile account
  - Phone number (clickable)
  - Email (clickable)
  - Avatar (can only be set from the desktop browser)
  - Edition of First Name, Last Name, Phone Number, mail.
- You have different kind of browsing. You have at anytime access to the 2 kind of browsing
  - Hierarchical: You have a root view that expose the default domain root children and your personnal workspace content. Click on the element will let show you his content or if this is a leaf the document view.
  - Search. You have 2 kind of search:
     - Full text search - from the Home page and the search view
     - faceted search: you have access to your personnal and shared faceted search recorded. You can't create new faceted search from the mobile application.

This application works on all main mobile browser (Webkits - android, iOS, BB6 -, Fennec Mobile and Windows Phone - we never try, but JQuery say it's ok :).
About iOS you have also the possibility to add the application into your springboard: Browser button will be hidden and you will have a "feeling" to be to a built-in application.

Here is the list of Web Mobile bundles you can find inside this addon:

* Nuxeo Application Definition: service where you describe an application (Base URL, Login relative Path URL, Request Handler) filters will automatically will do the redirection to your app
* Nuxeo Application Sample: This is a simple example of how to use the Nuxeo Application Definition
* Nuxeo Web Mobile: This is the webengine application that uses JQuery Mobile to provide a dedicate UI for Mobile Browser.

## How to build

You can build Nuxeo Web Mobile application with:

    $ mvn install

If you want to test it, take jars built (in nuxeo-web-mobile/target and nuxeo-application-definition/target directories) and put it into your tomcat server in $tomcat/nxserver/bundles or in your jboss server in $jboss/server/default/deploy/nuxeo.ear/bundles.

## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>
