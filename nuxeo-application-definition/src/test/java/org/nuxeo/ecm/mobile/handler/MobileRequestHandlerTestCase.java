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

    @Test
    public void SafariMobileUserAgentShouldBeSelected() {
        RequestHandler handler = new MobileRequestHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent")).thenReturn(
                SAFARI_MOBILE_USER_AGENT);

        assertTrue(handler.isRequestRedirectedToApplication(request));
    }

    @Test
    public void SafariUserAgentShouldNotBeSelected() {
        RequestHandler handler = new MobileRequestHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent")).thenReturn(SAFARI_USER_AGENT);

        assertFalse(handler.isRequestRedirectedToApplication(request));
    }

}
