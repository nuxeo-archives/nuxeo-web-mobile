# Nuxeo Web Mobile - when WebEngine meet JQuery Mobile

This add-on provides a WebApp exposing Nuxeo with dedicated views for Mobile browsers. All Mobile browsers will be automatically redirected to this application.
Note for developers: there's no guarantee the current API will be maintained since we envisage to split the data and the UI restitution.
Test and feedback are very welcome.

About the Nuxeo Web Mobile application: we expose a subset of functionalities of Nuxeo DM. Here is an overview of what you can do:

- Automatic redirection to the Web Mobile application.
- Redirection to the document into the Mobile application when JSF request asked.
- Dedicated login page.
- Multiple views are available for a document:
  - Subset of Metadata
  - Preview
  - Comments
  - Annotation (just the text, no display over the preview)
  - Relation
  - Content
  - The list of contributors to each document. Clicking on a user will show his data, especially his phone number and mail. It is then possible to send him an email or start a phone call.
- You also have some default actions enabled:
  - Mail it (send a link to the current document to a contact from your address book)
  - Download it (download the attached file)
  - Mail me (ask the server to send the current document to yourself, including the attached file)
- Profile view
  - Access to all users' profiles
  - Phone number (clickable)
  - Email (clickable)
  - Avatar (can only be set from the desktop browser)
  - Edition of First Name, Last Name, Phone Number, mail.
- There are two different kinds of browsing, always available:
  - Hierarchical: You have a root view that expose the default domain root children and your personal workspace content. Clicking on an element shows its content or the document view (if it's a leaf).
  - There are two kinds of search:
     - Full text search: from the Home page and the search view
     - faceted search: you have access to your personal and shared faceted search recorded. You can't create new faceted search from the mobile application.

This application works on all main mobile browser (WebKits - Android, iOS, BB6 -, Fennec Mobile and Windows Phone - not tested but JQuery is supposed to).
About iOS you have also the possibility to add the application into your springboard: the browser button will be hidden and you will feel like using a built-in application.

Here is the list of Web Mobile bundles you can find inside this addon:

* Nuxeo Application Definition: service where you describe an application (Base URL, Login relative Path URL, Request Handler) filters will automatically will do the redirection to your application.
* Nuxeo Application Sample: simple example of how to use the Nuxeo Application Definition.
* Nuxeo Web Mobile: This is the WebEngine application that uses JQuery Mobile to provide a dedicate UI for Mobile Browser.
* Nuxeo Web Mobile Cordova: This is the iOS / Android native project that embedded a Nuxeo server management and enhance your nuxeo-web-mobile experience with some natives behaviors. Send file store into your server to other application, file / camera / gallery direct upload, ...

## How to build nuxeo-web-mobile bundle

You can build Nuxeo Web Mobile application with:

    $ mvn clean install

## Deploying

Install [the Nuxeo Web Mobile Marketplace Package](https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-web-mobile).
Or manually copy the built artifacts (in nuxeo-web-mobile/target and nuxeo-application-definition/target directories) into `$NUXEO_HOME/templates/custom/bundles/` and activate the "custom" template.

## QA results

[![Build Status](https://qa.nuxeo.org/jenkins/buildStatus/icon?job=addons_nuxeo-web-mobile-master)](https://qa.nuxeo.org/jenkins/job/addons_nuxeo-web-mobile-master/)

## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>
