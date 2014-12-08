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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public class MobileWithCookieRequestHandler extends MobileRequestHandler {

    private static final String COOKIE_NAME = "skipMobileRedirection";

    protected String getCookieName() {
        return COOKIE_NAME;
    }

    @Override
    public boolean isRequestRedirectedToApplication(HttpServletRequest request) {
        Boolean skipMobileRedirection = getCookieValue(request);
        if (skipMobileRedirection != null && skipMobileRedirection) {
            return !skipMobileRedirection;
        }

        return super.isRequestRedirectedToApplication(request);
    }

    @Override
    public boolean isRequestRedirectedToApplicationLoginForm(HttpServletRequest request) {
        Boolean checkCookie = getCookieValue(request);
        if (checkCookie != null && checkCookie) {
            return checkCookie;
        }

        return super.isRequestRedirectedToApplicationLoginForm(request);
    }

    private Boolean getCookieValue(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return Boolean.parseBoolean(cookie.getValue());
                }
            }
        }
        return null;
    }

}
