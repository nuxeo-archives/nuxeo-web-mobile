/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bjalon
 */
package org.nuxeo.ecm.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.mobile.handler.RequestHandler;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.theme.jsf.URLUtils;

/**
 * @author bjalon
 * 
 */
public class ApplicationRedirectServiceImpl extends DefaultComponent implements
        ApplicationDefinitionService {

    private static final Log log = LogFactory.getLog(ApplicationRedirectServiceImpl.class);

    private final Map<String, ApplicationDefinitionDescriptor> applications = new HashMap<String, ApplicationDefinitionDescriptor>();

    private final Map<String, RequestHandlerDescriptor> requestHandlers = new HashMap<String, RequestHandlerDescriptor>();

    private final List<ApplicationDefinitionDescriptor> applicationsOrdered = new ArrayList<ApplicationDefinitionDescriptor>();

    private List<String> unAuthenticatedURLPrefix;

    private String nuxeoRelativeContextPath;

    public enum ExtensionPoint {
        applicationDefinition, requestHandlers
    }

    protected String getNuxeoRelativeContextPath() {
        if (nuxeoRelativeContextPath == null) {
            nuxeoRelativeContextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
            if (!nuxeoRelativeContextPath.endsWith("/")) {
                nuxeoRelativeContextPath = nuxeoRelativeContextPath + "/";
            }
            if (!nuxeoRelativeContextPath.startsWith("/")) {
                nuxeoRelativeContextPath = "/" + nuxeoRelativeContextPath;
            }
        }
        return nuxeoRelativeContextPath;
    }

    @Override
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        ExtensionPoint ep = Enum.valueOf(ExtensionPoint.class, extensionPoint);
        switch (ep) {
        case applicationDefinition:
            registerApplication((ApplicationDefinitionDescriptor) contribution,
                    contributor.getName().getName());
            break;
        case requestHandlers:
            registerRequestHandler((RequestHandlerDescriptor) contribution,
                    contributor.getName().getName());
            break;
        default:
            throw new RuntimeException(
                    "error in exception handling configuration");
        }

    }

    protected void registerRequestHandler(RequestHandlerDescriptor rhd,
            String componentName) {
        RequestHandlerDescriptor finalRH = null;

        String requestHandlerName = rhd.getRequestHandlerName();

        if (requestHandlers.containsKey(requestHandlerName)) {
            if (!rhd.disabled) {
                String messageTemplate = "Request Handler definition %s will be"
                        + " overriden by on declared into %s component";
                String message = String.format(messageTemplate,
                        requestHandlerName, componentName);
                log.info(message);
            } else {
                String messageTemplate = "Request Handler definition '%s' will be removed as defined into %s";
                String message = String.format(messageTemplate,
                        requestHandlerName, componentName);
                log.info(message);
                for (ApplicationDefinitionDescriptor app : applicationsOrdered) {
                    if (app.getRequestHandlerName().equals(requestHandlerName)) {
                        messageTemplate = "Request Handler definition '%s' used by %s Application Definition";
                        message = String.format(messageTemplate,
                                requestHandlerName, app.getName());
                        log.warn(message);
                    }
                }
            }
            finalRH = mergeRequestHandlerDescriptor(
                    requestHandlers.get(requestHandlerName), rhd);
        } else {
            finalRH = rhd;
        }
        requestHandlers.put(requestHandlerName, finalRH);

    }

    private RequestHandlerDescriptor mergeRequestHandlerDescriptor(
            RequestHandlerDescriptor initial, RequestHandlerDescriptor toMerge) {
        if (toMerge.klass == null) {
            toMerge.klass = initial.klass;
        }
        if (toMerge.properties == null) {
            toMerge.properties = initial.properties;
        }
        return toMerge;
    }

    protected void registerApplication(
            ApplicationDefinitionDescriptor appDescriptor, String componentName) {
        String name = appDescriptor.getName();

        validateApplicationDescriptor(appDescriptor, componentName);

        if (applications.containsKey(name)) {
            if (!appDescriptor.isDisable()) {
                String messageTemplate = "Application definition '%s' will be overridden, "
                        + "replaced by ones declared into %s component";
                String message = String.format(messageTemplate, name,
                        componentName);
                log.info(message);
            } else {
                String messageTemplate = "Application definition '%s' will be removed as defined into %s";
                String message = String.format(messageTemplate, name,
                        componentName);
                log.info(message);
                disableApplication(name);
                return;
            }
        }
        if (appDescriptor.isDisable()) {
            String messageTemplate = "Application definition '%s' already removed, definition into %s component ignored";
            String message = String.format(messageTemplate, name, componentName);
            log.info(message);
            disableApplication(name);
            return;
        }

        String messageTemplate = "New Application definition detected '%s' into %s component";
        String message = String.format(messageTemplate, name, componentName);
        log.info(message);
        applications.put(name, appDescriptor);
        applicationsOrdered.add(appDescriptor);
        Collections.sort(applicationsOrdered, new MobileApplicationComparator());
    }

    private void disableApplication(String applicationName) {
        applications.remove(applicationName);
        for (int i = 0; i < applicationsOrdered.size(); i++) {
            ApplicationDefinitionDescriptor application = applicationsOrdered.get(i);
            if (application.getName().equals(applicationName)) {
                applicationsOrdered.remove(i);
            }
        }
    }

    protected String getBaseURL(HttpServletRequest request) {
        return URLUtils.getBaseURL(request);
    }

    protected RequestHandlerDescriptor getRequestHandlerByName(String name) {
        return requestHandlers.get(name);
    }

    private ApplicationDefinitionDescriptor getTargetApplication(
            HttpServletRequest request) {

        for (ApplicationDefinitionDescriptor application : applicationsOrdered) {
            RequestHandlerDescriptor rhd = getRequestHandlerByName(application.getRequestHandlerName());
            if (rhd == null) {
                String message = "Can't find request handler %s for app definition %s, please check your configuration, skipping check";
                log.error(String.format(message,
                        application.getRequestHandlerName(),
                        application.getName()));
                continue;
            }
            RequestHandler handler = rhd.getRequestHandlerInstance();

            if (handler.isRequestRedirectedToApplication(request)) {
                String messageTemplate = "Request '%s' match the application '%s' request handler";
                String message = String.format(messageTemplate,
                        request.getRequestURI(), application.getName());
                log.debug(message);
                return application;

            }
        }

        log.debug("Request match no application request handler");
        return null;
    }

    @Override
    public String getApplicationBaseURL(HttpServletRequest request) {
        ApplicationDefinitionDescriptor app = getTargetApplication(request);
        if (app == null) {
            log.debug(String.format("No application matched for this request,"
                    + " no Application base url found"));
            return null;
        }
        return getBaseURL(request) + app.getApplicationRelativePath();
    }

    @Override
    public String getLoginURL(HttpServletRequest request) {
        ApplicationDefinitionDescriptor app = getTargetApplication(request);
        if (app == null) {
            log.debug(String.format("No application matched for this request,"
                    + " no Login page found"));
            return null;
        }
        return getBaseURL(request) + app.getApplicationRelativePath()
                + app.getLoginPage();
    }

    @Override
    public String getLogoutURL(HttpServletRequest request) {
        ApplicationDefinitionDescriptor app = getTargetApplication(request);
        if (app == null) {
            log.debug(String.format("No application matched for this request,"
                    + ", no Logout page found"));
            return null;
        }
        return getBaseURL(request) + app.getApplicationRelativePath()
                + app.getLogoutPage();
    }

    private void validateApplicationDescriptor(
            ApplicationDefinitionDescriptor app, String componentName) {
        if (app.getName() == null) {
            String messageTemplate = "Application given in '%s' component is null, "
                    + "can't register it";
            String message = String.format(messageTemplate, componentName);
            throw new RuntimeException(message);
        }
        if (app.getApplicationRelativePath() == null) {
            String messageTemplate = "Application name %s given in '%s' component as "
                    + "an empty base URL, can't register it";
            String message = String.format(messageTemplate, app.getName(),
                    componentName);
            throw new RuntimeException(message);
        }
        if (app.getApplicationRelativePath().startsWith("/")) {
            log.warn("Application relative path must not start by a slash, please think"
                    + " to change your contribution");
            app.applicationRelativePath = app.getApplicationRelativePath().substring(
                    1);
        }
        List<String> resourcesUriChanged = new ArrayList<String>();

        for (String resourceUri : app.getResourcesBaseUrl()) {
            if (resourceUri.startsWith("/")) {
                log.warn("Resource Uri relative path must not start by a slash, please"
                        + " think to change your contribution");
                resourceUri = resourceUri.substring(1);
            }
            resourcesUriChanged.add(resourceUri);
            app.resourcesBaseUrl = resourcesUriChanged;
        }
        
        if (app.getLoginPage() == null) {
            String messageTemplate = "Application name %s given in '%s' component as "
                    + "an empty login URL, can't register it";
            String message = String.format(messageTemplate, app.getName(),
                    componentName);
            throw new RuntimeException(message);
        }
        if (app.getLogoutPage() == null) {
            String messageTemplate = "Application name %s given in '%s' component as "
                    + "an empty logout URL, can't register it";
            String message = String.format(messageTemplate, app.getName(),
                    componentName);
            throw new RuntimeException(message);
        }
    }

    @Override
    public List<String> getUnAuthenticatedURLPrefix() {
        if (unAuthenticatedURLPrefix == null) {
            unAuthenticatedURLPrefix = new ArrayList<String>();
            for (ApplicationDefinitionDescriptor app : applicationsOrdered) {
                String loginPage = app.getApplicationRelativePath()
                        + app.getLoginPage();
                log.debug("Add login page as Unauthenticated resources"
                        + loginPage);
                unAuthenticatedURLPrefix.add(loginPage);
                if (app.getResourcesBaseUrl() != null) {
                    for (String uri : app.getResourcesBaseUrl()) {
                        log.debug("Add following declared resources as Unauthenticated resources"
                                + uri);
                        unAuthenticatedURLPrefix.add(uri);
                    }
                }
            }
        }
        return unAuthenticatedURLPrefix;
    }

    @Override
    public boolean isResourceURL(HttpServletRequest request) {
        ApplicationDefinitionDescriptor app = getTargetApplication(request);
        if (app == null) {
            return false;
        }
        List<String> resourcesBaseURL = app.getResourcesBaseUrl();

        if (resourcesBaseURL == null || resourcesBaseURL.size() == 0) {
            return false;
        }
        String uri = request.getRequestURI();
        for (String resourceBaseURL : resourcesBaseURL) {
            log.debug("Check if this is this Resources application : "
                    + getNuxeoRelativeContextPath() + resourceBaseURL
                    + " for uri : " + uri);
            if (uri.startsWith(getNuxeoRelativeContextPath() + resourceBaseURL)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRequestIntoApplication(HttpServletRequest req) {
        ApplicationDefinitionDescriptor app = getTargetApplication(req);
        if (app == null) {
            return false;
        }

        String uri = req.getRequestURI();

        log.debug("Request url: " + uri + " and targetApplicationURI: ");
        if (!uri.startsWith(getNuxeoRelativeContextPath()
                + app.getApplicationRelativePath())) {
            log.debug("Request uri is not a child of application base url");
            return false;
        }
        if (uri.equals(getNuxeoRelativeContextPath()
                + app.getApplicationRelativePath())) {
            log.debug("Request uri is the root of the application");
            return true;
        }
        char character = uri.charAt((getNuxeoRelativeContextPath() + app.getApplicationRelativePath()).length());
        if (character != '/' && character != '?' && character != '#'
                && character != '@') {
            log.debug("Request uri is not a child of application base url");
            return false;
        }
        log.debug("Request uri is a child of application base url");
        return true;
    }

}

class MobileApplicationComparator implements
        Comparator<ApplicationDefinitionDescriptor> {
    @Override
    public int compare(ApplicationDefinitionDescriptor app1,
            ApplicationDefinitionDescriptor app2) {
        if (app1.getOrder() == null) {
            return 1;
        }
        if (app2.getOrder() == null) {
            return -1;
        }
        return app1.getOrder().compareTo(app2.getOrder());

    }
}
