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
package org.nuxeo.ecm.mobile.handler;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.platform.ui.web.auth.CachableUserIdentificationInfo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.USERIDENT_KEY;

/**
 * @author bjalon
 *
 */
public class AnonymousRequestHandlerTestCase {

    private AnonymousRequestHandler handler;

    @Before
    public void init() {
        handler = new AnonymousRequestHandler() {
            @Override
            protected String getAnonymousUsername() throws Exception {
                return "Anonymous";
            }
        };

    }

    @Test
    public void nonAuthenticatedRequestShouldNotBeSelected() {
        HttpServletRequest request = initializeNonAuthenticatedRequest();
        assertFalse(handler.isRequestRedirectedToApplication(request));
    }

    @Test
    public void nonAnonymousUsersShouldNotBeSelected() {
        HttpServletRequest request = initializeRequestCachableUserIdentification("test");
        assertFalse(handler.isRequestRedirectedToApplication(request));

        request = initializeRequestUserPrincipal("test");
        assertFalse(handler.isRequestRedirectedToApplication(request));
    }

    @Test
    public void anonymousUsersShouldBeSelected() {
        HttpServletRequest request = initializeRequestCachableUserIdentification("Anonymous");
        assertTrue(handler.isRequestRedirectedToApplication(request));

        request = initializeRequestUserPrincipal("Anonymous");
        assertTrue(handler.isRequestRedirectedToApplication(request));
    }

    private HttpServletRequest initializeNonAuthenticatedRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(USERIDENT_KEY)).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(request.getUserPrincipal()).thenReturn(null);
        return request;
    }

    private HttpServletRequest initializeRequestUserPrincipal(String username) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        when(request.getUserPrincipal()).thenReturn(principal);
        return request;
    }

    private HttpServletRequest initializeRequestCachableUserIdentification(String username) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        CachableUserIdentificationInfo uii = mock(CachableUserIdentificationInfo.class);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        when(uii.getPrincipal()).thenReturn(principal);
        when(session.getAttribute(USERIDENT_KEY)).thenReturn(uii);
        when(request.getSession()).thenReturn(session);
        when(request.getUserPrincipal()).thenReturn(null);
        return request;
    }

}
