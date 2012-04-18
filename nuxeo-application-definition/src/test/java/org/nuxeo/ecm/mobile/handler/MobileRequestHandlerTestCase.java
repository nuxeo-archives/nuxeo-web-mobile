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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bjalon
 * 
 */
public class MobileRequestHandlerTestCase {

    private static final String SAFARI_USER_AGENT = "Mozilla/5.0 "
            + "(Macintosh; U; Intel Mac OS X 10_6_6; en-us) "
            + "AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27";

    private static final String SAFARI_MOBILE_USER_AGENT = "Mozilla/5.0 "
            + "(iPod; U; CPU iPhone OS 2_2_1 like Mac OS X; en-us) "
            + "AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5H11a Safari/525.20";

    private static final String FENNEC_MOBILE_USER_AGENT = "Mozilla/5.0 (Android; Linux armv7l;"
            + " rv:10.0) Gecko/20120129 Firefox/10.0 Fennec/10.0";

    @Test
    public void SafariMobileUserAgentShouldBeSelected() {
        RequestHandler handler = new MobileRequestHandler();

        HttpServletRequest request = initRequest(SAFARI_MOBILE_USER_AGENT,
                "http://localhost:8080/nuxeo/nxstartup.faces");
        assertTrue(handler.isRequestRedirectedToApplication(request));
    }

    @Test
    public void SafariUserAgentShouldNotBeSelected() {
        RequestHandler handler = new MobileRequestHandler();

        HttpServletRequest request = initRequest(SAFARI_USER_AGENT,
                "http://localhost:8080/nuxeo/nxstartup.faces");
        assertFalse(handler.isRequestRedirectedToApplication(request));
    }

    @Test
    public void FennecMobileUserAgentShouldBeSelected() {
        RequestHandler handler = new MobileRequestHandler();

        HttpServletRequest request = initRequest(FENNEC_MOBILE_USER_AGENT,
                "http://localhost:8080/nuxeo/nxstartup.faces");
        assertTrue(handler.isRequestRedirectedToApplication(request));
    }

    @Test
    public void URLSkippedShouldNotBeSelectedEvenForSafariMobileUserAgent() {
        RequestHandler handler = new MobileRequestHandler();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(MobileRequestHandler.URL_SKIPPED_PATTERNS_PROP,
                "(.*)/nxfile/(.*)|(.*)/nxbigfile/(.*)");
        handler.init(properties);

        HttpServletRequest request = initRequest(SAFARI_MOBILE_USER_AGENT,
                "http://localhost:8080/nuxeo/nxstartup.faces");
        assertTrue(handler.isRequestRedirectedToApplication(request));
        assertTrue(handler.isRequestRedirectedToApplicationLoginForm(request));

        request = initRequest(SAFARI_MOBILE_USER_AGENT,
                "http://localhost:8080/nuxeo/nxfile/etc");
        assertFalse(handler.isRequestRedirectedToApplication(request));
        assertTrue(handler.isRequestRedirectedToApplicationLoginForm(request));

        request = initRequest(SAFARI_MOBILE_USER_AGENT,
                "http://localhost:8080/nuxeo/nxbigfile/etc");
        assertFalse(handler.isRequestRedirectedToApplication(request));
        assertTrue(handler.isRequestRedirectedToApplicationLoginForm(request));
    }

    private HttpServletRequest initRequest(String userAgent, String url) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent")).thenReturn(userAgent);
        when(request.getRequestURL()).thenReturn(new StringBuffer(url));
        return request;
    }

}
