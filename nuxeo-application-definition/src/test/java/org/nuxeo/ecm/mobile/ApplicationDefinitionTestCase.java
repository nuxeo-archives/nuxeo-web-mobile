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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.nuxeo.ecm.mobile.ApplicationConstants.APPLICATION_SELECTED_COOKIE_NAME;

/**
 * @author bjalon
 *
 */
public class ApplicationDefinitionTestCase {

    private ApplicationRedirectServiceImpl service;

    private HttpServletRequest request;

    private Cookie[] cookies;

    @Before
    public void init() {
        service = new ApplicationRedirectServiceImpl();

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
        assertNull(service.getTargetApplication(request));

        // handler doen't match and is ordered after
        app = initDescriptorWithoutCookie("app2", false, false, 30);
        service.registerApplication(app, "myComponentName2");
        assertNull(service.getTargetApplication(request));

        // handler doen't match and is ordered before
        app = initDescriptorWithoutCookie("app3", false, false, 40);
        service.registerApplication(app, "myComponentName3");
        assertNull(service.getTargetApplication(request));

        // handler match and application enabled
        app = initDescriptorWithoutCookie("app4", true, false, 50);
        service.registerApplication(app, "myComponentName4");
        assertEquals("app4", service.getTargetApplication(request).getName());

        // disable previous application
        app = initDescriptorWithoutCookie("app4", true, true, 50);
        service.registerApplication(app, "myComponentName5");
        assertNull(service.getTargetApplication(request));

        // handler match and application enabled (in the first position)
        app = initDescriptorWithoutCookie("app5", true, false, 0);
        service.registerApplication(app, "myComponentName6");
        assertEquals("app5", service.getTargetApplication(request).getName());

        // Idem but ordered after
        app = initDescriptorWithoutCookie("app6", true, false, 1);
        service.registerApplication(app, "myComponentName7");
        assertEquals("app5", service.getTargetApplication(request).getName());
    }

    @Test
    public void shouldReturnApplicationTargetAccordingCookieIfNoHandlerMatch() {

        // Create a request with the app1 application selected => user has
        // selected app1
        cookies = new Cookie[1];
        cookies[0] = mock(Cookie.class);
        when(cookies[0].getName()).thenReturn(APPLICATION_SELECTED_COOKIE_NAME);
        when(cookies[0].getValue()).thenReturn("app1");
        request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);

        // Even cookie = app1, null returned as no app1 defined
        assertNull(service.getTargetApplication(request));

        // app1 selected as no handler match and cookie = app1 and app 1 defined
        ApplicationDefinitionDescriptor app = initDescriptorWithoutCookie(
                "app1", false, false, 10);
        service.registerApplication(app, "myComponentName1");
        assertEquals("app1", service.getTargetApplication(request).getName());

        // Cookie value ignored as app2 handler match
        app = initDescriptorWithoutCookie("app2", true, false, 20);
        service.registerApplication(app, "myComponentName2");
        assertEquals("app2", service.getTargetApplication(request).getName());
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
        result.baseURL = "/site/" + name;
        result.loginPage = "/login";
        result.logoutPage = "/logout";
        return result;
    }
}
