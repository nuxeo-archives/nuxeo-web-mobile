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
package org.nuxeo.ecm.mobile.filter;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test User agent Mobile detector
 *
 * @author bjalon
 *
 */
public class RequestAdapterImplTestCase {

    @Test
    public void shouldDetectMobileUserAgent() {
        RequestAdapterImpl adapter = new RequestAdapterImpl();

        assertTrue(adapter.isMobileBrowser(CHROME_ANDROID_USER_AGENT));
        assertTrue(adapter.isMobileBrowser(SAFARI_IOS_USER_AGENT));
    }


    @Test
    public void shouldDetectNonMobileUserAgent() {
        RequestAdapterImpl adapter = new RequestAdapterImpl();

        assertFalse(adapter.isMobileBrowser(CHROME_DESKTOP_USER_AGENT));
        assertFalse(adapter.isMobileBrowser(SAFARI_DESKTOP_USER_AGENT));
        assertFalse(adapter.isMobileBrowser(FIREFOX_DESKTOP_USER_AGENT));
    }

    private static final String SAFARI_DESKTOP_USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; en-us) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27";

    private static final String SAFARI_IOS_USER_AGENT = "Mozilla/5.0 (iPod; U; CPU iPhone OS 2_2_1 like Mac OS X; en-us) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5H11a Safari/525.20";

    private static final String CHROME_DESKTOP_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.0 Safari/535.11";

    private static final String CHROME_ANDROID_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.3.4; fr-fr; Nexus S Build/GRJ22) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

    private static final String FIREFOX_DESKTOP_USER_AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:8.0) Gecko/20100101 Firefox/8.0";


}
