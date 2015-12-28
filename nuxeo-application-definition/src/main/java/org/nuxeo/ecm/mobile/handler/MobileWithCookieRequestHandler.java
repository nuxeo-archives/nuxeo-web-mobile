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
