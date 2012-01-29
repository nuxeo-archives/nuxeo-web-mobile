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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.mobile.handler.RequestHandler;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import static org.nuxeo.ecm.mobile.ApplicationConstants.APPLICATION_SELECTED_COOKIE_NAME;

/**
 * @author bjalon
 * 
 */
public class ApplicationRedirectServiceImpl extends DefaultComponent implements
        ApplicationDefinitionService {

    private static final Log log = LogFactory.getLog(ApplicationRedirectServiceImpl.class);

    private String nuxeoContextPath;

    private final Map<String, ApplicationDefinitionDescriptor> applications = new HashMap<String, ApplicationDefinitionDescriptor>();

    private final List<ApplicationDefinitionDescriptor> applicationsOrdered = new ArrayList<ApplicationDefinitionDescriptor>();

    private List<String> unAuthenticatedURLPrefix;

    private String getNuxeoContextPath() {
        if (nuxeoContextPath == null) {
            nuxeoContextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        }
        return nuxeoContextPath;
    }

    public enum ExtensionPoint {
        applicationDefinition, applicationSelector
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
        default:
            throw new RuntimeException(
                    "error in exception handling configuration");
        }

    }

    protected void registerApplication(
            ApplicationDefinitionDescriptor appDescriptor, String componentName) {
        String name = appDescriptor.getName();

        validateApplicationDescriptor(appDescriptor, componentName);

        if (applications.containsKey(name)) {
            if (!appDescriptor.isDisable()) {
                String messageTemplate = "Application definition '%s' will be overridden, "
                        + "replaced by ones declared into %s";
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

    @Override
    public ApplicationDefinitionDescriptor getTargetApplication(
            HttpServletRequest request) {

        for (ApplicationDefinitionDescriptor application : applicationsOrdered) {
            RequestHandler handler = application.getRequestHandlerInstance();
            if (handler.isRequestRedirectedToApplication(request)) {
                String messageTemplate = "Request '%s' match the application '%s' request handler";
                String message = String.format(messageTemplate,
                        request.getRequestURI(), application.getName());
                log.debug(message);
                return application;

            }
        }

        // Then check if the target application is not stored into the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (APPLICATION_SELECTED_COOKIE_NAME.equals(cookie.getName())) {
                    ApplicationDefinitionDescriptor app = applications.get(cookie.getValue());
                    if (app != null) {
                        return app;
                    }
                    String messageTemplate = "Application name '%s' given in cookie '%s' "
                            + "not found, can't redirect to it. Try to fetch information "
                            + "in the Request context.";
                    String message = String.format(messageTemplate,
                            cookie.getValue(), APPLICATION_SELECTED_COOKIE_NAME);
                    log.error(message);
                    break;
                }
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
        return getNuxeoContextPath() + app.getBaseUrl();
    }

    @Override
    public List<String> getResourcesApplicationBaseURL(HttpServletRequest request) {
        List<String> result = new ArrayList<String>();
        ApplicationDefinitionDescriptor app = getTargetApplication(request);
        if (app == null) {
            log.debug(String.format("No application matched for this request,"
                    + " no Application base url found"));
            return null;
        }
        for (String resourcesBaseURL : app.getResourcesBaseUrl()) {
            result.add(getNuxeoContextPath() + resourcesBaseURL);
        }
        return result;

    }

    @Override
    public String getLoginURL(HttpServletRequest request) {
        ApplicationDefinitionDescriptor app = getTargetApplication(request);
        if (app == null) {
            log.debug(String.format("No application matched for this request,"
                    + " no Login page found"));
            return null;
        }
        return getNuxeoContextPath() + app.getBaseUrl() + app.getLoginPage();
    }

    @Override
    public String getLogoutURL(HttpServletRequest request) {
        ApplicationDefinitionDescriptor app = getTargetApplication(request);
        if (app == null) {
            log.debug(String.format("No application matched for this request,"
                    + ", no Logout page found"));
            return null;
        }
        return getNuxeoContextPath() + app.getBaseUrl() + app.getLogoutPage();
    }

    private void validateApplicationDescriptor(
            ApplicationDefinitionDescriptor app, String componentName) {
        if (app.getName() == null) {
            String messageTemplate = "Application given in '%s' component is null, "
                    + "can't register it";
            String message = String.format(messageTemplate, componentName);
            throw new RuntimeException(message);
        }
        if (app.getBaseUrl() == null) {
            String messageTemplate = "Application name %s given in '%s' component as "
                    + "an empty base URL, can't register it";
            String message = String.format(messageTemplate, app.getName(),
                    componentName);
            throw new RuntimeException(message);
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
                String loginPage = app.getBaseUrl() + app.getLoginPage();
                // Remove the first slash
                unAuthenticatedURLPrefix.add(loginPage.substring(1));
                if (app.getResourcesBaseUrl() != null) {
                    for (String url : app.getResourcesBaseUrl()) {
                        unAuthenticatedURLPrefix.add(url.substring(1));
                    }
                }
            }
        }
        return unAuthenticatedURLPrefix;
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
