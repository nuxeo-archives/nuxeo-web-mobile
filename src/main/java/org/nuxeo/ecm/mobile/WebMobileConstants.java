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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains all constants of the Mobile Application and some useful tools.
 * 
 * @author bjalon
 * 
 */
public class WebMobileConstants {

    public static final String FORCE_STANDARD_NAVIGATION_COOKIE_NAME = "ForceStandardNavigationEnabled";

    public static String MOBILE_HOME_URL_SUFFIX = "/site/mobile/home";

    public static String NAVIGATION_SELECTION_URL_SUFFIX = "/mobile/ask-mobile-or-standard-navigation.jsp";

    private static List<String> MOBILE_USER_AGENT_REGEXP = Arrays.asList(
            "(.*)Mobile(.*)Safari(.*)", "(.*)AppleWebKit(.*)Mobile(.*)");

    /**
     * Return true is the userAgent string value corresponding to a Mobile
     */
    public static boolean isMobileUserAgent(String userAgent) {
        boolean isMobileDevice = false;
        for (String pattern : MOBILE_USER_AGENT_REGEXP) {
            if (userAgent.matches(pattern)) {
                isMobileDevice = true;
            }
        }
        return isMobileDevice;
    }

    /**
     * Return true is the client that sends the request is a mobile browser.
     * 
     */
    public static boolean isMobileUserAgent(HttpServletRequest req) {
        String userAgent = req.getHeader("User-Agent");

        return isMobileUserAgent(userAgent);
    }

}
