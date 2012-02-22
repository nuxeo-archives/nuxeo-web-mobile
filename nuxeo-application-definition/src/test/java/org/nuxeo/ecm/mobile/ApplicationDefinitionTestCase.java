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
        ApplicationDefinitionDescriptor app = initDescriptorWithoutCookie(
                "app1", true, true, 20);
        service.registerApplication(app, "myComponentName1");
        assertNull(service.getApplicationBaseURL(request));

        // handler doen't match and is ordered after
        app = initDescriptorWithoutCookie("app2", false, false, 30);
        service.registerApplication(app, "myComponentName2");
        assertNull(service.getApplicationBaseURL(request));

        // handler doen't match and is ordered before
        app = initDescriptorWithoutCookie("app3", false, false, 40);
        service.registerApplication(app, "myComponentName3");
        assertNull(service.getApplicationBaseURL(request));

        // handler match and application enabled
        app = initDescriptorWithoutCookie("app4", true, false, 50);
        service.registerApplication(app, "myComponentName4");
        assertNotNull(service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app4", service.getApplicationBaseURL(request));

        // disable previous application
        app = initDescriptorWithoutCookie("app4", true, true, 50);
        service.registerApplication(app, "myComponentName5");
        assertNull(service.getApplicationBaseURL(request));

        // handler match and application enabled (in the first position)
        app = initDescriptorWithoutCookie("app5", true, false, 0);
        service.registerApplication(app, "myComponentName6");
        assertNotNull(service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app5", service.getApplicationBaseURL(request));

        // Idem but ordered after
        app = initDescriptorWithoutCookie("app6", true, false, 1);
        service.registerApplication(app, "myComponentName7");
        assertNotNull(service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app5", service.getApplicationBaseURL(request));
    }


    @Test
    public void shouldReturnApplicationBaseURL() {

        // Here we test values returned by application definition
        request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[0]);

        ApplicationDefinitionDescriptor app = initDescriptorWithoutCookie(
                "app1", true, false, 10);
        service.registerApplication(app, "myComponentName1");
        
        assertEquals("https://localhost/nuxeo/site/app1", service.getApplicationBaseURL(request));
        assertEquals("https://localhost/nuxeo/site/app1/login", service.getLoginURL(request));
        assertEquals("https://localhost/nuxeo/site/app1/logout", service.getLogoutURL(request));

    }
    
    /**
     * Return a {@code ApplicationDescriptor} with an handler return always
     * {@value handleReturn}
     *
     * @param handlerReturn value return by the handler
     */
    private ApplicationDefinitionDescriptor initDescriptorWithoutCookie(
            String name, final boolean handlerReturn, boolean disabled,
            int order) {
        ApplicationDefinitionDescriptor result = new ApplicationDefinitionDescriptor() {
            @Override
            public RequestHandler getRequestHandlerInstance() {
                RequestHandler handler = mock(RequestHandler.class);
                when(handler.isRequestRedirectedToApplication(request)).thenReturn(
                        Boolean.valueOf(handlerReturn));
                return handler;
            }
        };

        result.name = name;
        result.disabled = disabled;
        result.order = order;
        result.applicationRelativePath = "/site/" + name;
        result.loginPage = "/login";
        result.logoutPage = "/logout";
        return result;
    }
}
