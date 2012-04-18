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

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.mobile.handler.RequestHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bjalon
 * 
 */
public class ApplicationDefinitionTestCase {

    private static final String REQUEST_HANDLER_NAME_PREFIX = "requestHandlerTest";

    private ApplicationRedirectServiceImpl service;

    private HttpServletRequest request;

    @Before
    public void init() {
        service = new ApplicationRedirectServiceImpl() {
            @Override
            protected String getBaseURL(HttpServletRequest req) {
                return "https://localhost/nuxeo/";
            }
        };

    }

    @Test
    public void shouldReturnApplicationTargetAccordingHandlersSet() {

        // handler win as cookie empty
        request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[0]);

        // descriptor disabled
        String appName = "app1";
        boolean requestHandlerReturn = true;
        boolean applicationDisabled = true;
        int order = 20;
        ApplicationDefinitionDescriptor app = initApplicationDescriptor(
                appName, requestHandlerReturn, applicationDisabled, order);

        service.registerApplication(app, "myComponentName1");
        assertNull(service.getApplicationBaseURL(request));

        // handler doen't match and is ordered after
        appName = "app2";
        requestHandlerReturn = false;
        applicationDisabled = false;
        order = 30;
        app = initApplicationDescriptor(appName, requestHandlerReturn,
                applicationDisabled, order);
        service.registerApplication(app, "myComponentName2");
        assertNull(service.getApplicationBaseURL(request));

        // handler doen't match and is ordered before
        appName = "app3";
        requestHandlerReturn = false;
        applicationDisabled = false;
        order = 40;
        app = initApplicationDescriptor(appName, requestHandlerReturn,
                applicationDisabled, order);
        service.registerApplication(app, "myComponentName3");
        assertNull(service.getApplicationBaseURL(request));

        // handler match and application enabled
        appName = "app4";
        requestHandlerReturn = true;
        applicationDisabled = false;
        order = 50;
        app = initApplicationDescriptor(appName, requestHandlerReturn,
                applicationDisabled, order);
        service.registerApplication(app, "myComponentName4");
        assertNotNull(service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app4",
                service.getApplicationBaseURL(request));

        // disable previous application
        appName = "app4";
        applicationDisabled = true;
        requestHandlerReturn = true;
        order = 50;
        app = initApplicationDescriptor(appName, requestHandlerReturn,
                applicationDisabled, order);
        service.registerApplication(app, "myComponentName5");
        assertNull(service.getApplicationBaseURL(request));

        // handler match and application enabled (in the first position)
        appName = "app5";
        requestHandlerReturn = true;
        applicationDisabled = false;
        order = 0;
        app = initApplicationDescriptor(appName, requestHandlerReturn,
                applicationDisabled, order);
        service.registerApplication(app, "myComponentName6");
        assertNotNull(service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app5",
                service.getApplicationBaseURL(request));

        // Idem but ordered after
        appName = "app6";
        requestHandlerReturn = true;
        applicationDisabled = false;
        order = 1;
        app = initApplicationDescriptor(appName, requestHandlerReturn,
                applicationDisabled, order);
        service.registerApplication(app, "myComponentName7");
        assertNotNull(service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app5",
                service.getApplicationBaseURL(request));
    }

    @Test
    public void shouldReturnApplicationBaseURL() {

        // Here we test values returned by application definition
        request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[0]);

        ApplicationDefinitionDescriptor app = initApplicationDescriptor("app1",
                true, false, 10);
        service.registerApplication(app, "myComponentName1");

        assertEquals("https://localhost/nuxeo/site/app1",
                service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app1/login",
                service.getLoginURL(request));
        assertEquals("https://localhost/nuxeo/site/app1/logout",
                service.getLogoutURL(request));

    }

    /**
     * Return a {@code ApplicationDescriptor} with an handler return always
     * {@value handleReturn}
     * 
     * @param handlerReturn value return by the handler
     */
    private ApplicationDefinitionDescriptor initApplicationDescriptor(
            String appName, final boolean handlerReturn, boolean disabled,
            int order) {

        // Request Handler Descriptor definition
        RequestHandlerDescriptor rhd = new RequestHandlerDescriptorMock(
                handlerReturn, appName);

        // Add request handler descriptor into service
        service.registerRequestHandler(rhd, "mycomponent.name");

        return new ApplicationDefinitionDescriptorMock(appName, disabled, order);
    }

    class RequestHandlerDescriptorMock extends RequestHandlerDescriptor {

        private boolean handlerReturn;

        public RequestHandlerDescriptorMock(boolean handlerReturn,
                String appName) {
            this.handlerReturn = handlerReturn;
            this.requestHandlerName = REQUEST_HANDLER_NAME_PREFIX + appName;
        }

        @Override
        public RequestHandler getRequestHandlerInstance() {
            return new RequestHandler() {

                @Override
                public RequestHandler init(Map<String, String> prop) {
                    return this;
                }

                @Override
                public boolean isRequestRedirectedToApplication(
                        HttpServletRequest req) {
                    return handlerReturn;
                }

                @Override
                public boolean isRequestRedirectedToApplicationLoginForm(
                        HttpServletRequest req) {
                    return isRequestRedirectedToApplication(req);
                }

            };
        }
    }

    class ApplicationDefinitionDescriptorMock extends
            ApplicationDefinitionDescriptor {
        public ApplicationDefinitionDescriptorMock(String appName,
                boolean disabled, int order) {
            this.name = appName;
            this.disabled = disabled;
            this.order = order;
            this.applicationRelativePath = "/site/" + appName;
            this.loginPage = "/login";
            this.logoutPage = "/logout";
            this.requestHandlerName = REQUEST_HANDLER_NAME_PREFIX + appName;
        }
    }
}
