/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public class TestAnonymousRequestHandler {

    private AnonymousRequestHandler handler;

    @Before
    public void init() {
        handler = new AnonymousRequestHandler() {
            @Override
            protected String getAnonymousUsername() {
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
