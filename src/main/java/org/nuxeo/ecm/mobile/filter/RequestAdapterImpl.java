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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.nuxeo.ecm.mobile.WebMobileConstants.FORCE_STANDARD_NAVIGATION_COOKIE_NAME;

/**
 * Implementation of {@code RequestAdapter}.
 *
 * @author bjalon
 *
 */
public class RequestAdapterImpl implements RequestAdapter {

    private static final Log log = LogFactory.getLog(RequestAdapterImpl.class);

    private static List<String> MOBILE_USER_AGENT_REGEXP = Arrays.asList(
            "(.*)Mobile(.*)Safari(.*)", "(.*)AppleWebKit(.*)Mobile(.*)");

    private String uri;

    private StringBuffer url;

    private boolean isMobileBrowser;

    private Cookie[] cookies;

    private boolean isStandardNavigationChosen;

    private boolean isNavigationChosen = false;

    private boolean navigationChoiceFetched;

    private boolean isMobileNavigationChosen;

    private String urlParameters;

    public RequestAdapterImpl() {
    }

    public RequestAdapterImpl(HttpServletRequest request) {
        uri = request.getRequestURI();
        url = request.getRequestURL();
        urlParameters = request.getQueryString();

        isMobileBrowser = isMobileBrowser(request.getHeader("User-Agent"));

        // Sometimes send Exception ?? get cookie throw an error
        try {
            cookies = request.getCookies();
        } catch (Exception e) {
            log.error(e, e);
        }

        if (cookies == null) {
            cookies = new Cookie[0];
        }

    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public StringBuffer getUrl() {
        return url;
    }

    @Override
    public Cookie[] getCookies() {
        return cookies;
    }

    @Override
    public boolean isMobileBrowser() {
        return isMobileBrowser;
    }

    @Override
    public boolean isNavigationChosen() {
        if (!isMobileBrowser()) {
            return true;
        }
        fetchNavigationChoice();
        return isNavigationChosen;
    }

    @Override
    public boolean isStandardNavigationChosen() {
        if (!isMobileBrowser()) {
            return true;
        }
        fetchNavigationChoice();
        if ((!isMobileNavigationChosen) && (!isMobileBrowser())) {
            return true;
        }
        return isStandardNavigationChosen;
    }

    @Override
    public boolean isMobileNavigationChosen() {
        if (!isMobileBrowser()) {
            return false;
        }
        fetchNavigationChoice();
        if ((!isMobileNavigationChosen) && isMobileBrowser()) {
            return true;
        }
        return isMobileNavigationChosen;
    }

    @Override
    public String getFullURL() {
        return getUri() + "?" + (urlParameters != null ? urlParameters : "");
    }

    protected void fetchNavigationChoice() {
        if (!navigationChoiceFetched) {
            navigationChoiceFetched = true;
            for (Cookie cookie : getCookies()) {
                if (FORCE_STANDARD_NAVIGATION_COOKIE_NAME.equals(cookie.getName())) {
                    isNavigationChosen = true;
                    isStandardNavigationChosen = Boolean.parseBoolean(cookie.getValue());
                    isMobileNavigationChosen = (!isStandardNavigationChosen);
                }
            }
        }
    }

    protected boolean isMobileBrowser(String userAgent) {
        for (String pattern : MOBILE_USER_AGENT_REGEXP) {
            if (userAgent.matches(pattern)) {
                return true;
            }
        }
        return false;
    }

}
