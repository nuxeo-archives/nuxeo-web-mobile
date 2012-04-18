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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bjalon
 * 
 */
public class MobileRequestHandler implements RequestHandler {

    public static final String URL_SKIPPED_PATTERNS_PROP = "urlSkippedPatterns";

    private static final Log log = LogFactory.getLog(MobileRequestHandler.class);

    private static List<String> MOBILE_USER_AGENT_REGEXP = Arrays.asList(
            "(.*)Mobile(.*)Safari(.*)", "(.*)AppleWebKit(.*)Mobile(.*)",
            "(.*)Android(.*)");

    private String[] urlPatterns;

    @Override
    public boolean isRequestRedirectedToApplication(HttpServletRequest request) {

        // Some resources are skipped (nxfile pattern for instance, ...) but we
        // want login page displayed for mobile
        String queryString = request.getQueryString() != null ? "?"
                + request.getQueryString() : "";

        String urlRequest = request.getRequestURL() + queryString;

        if (urlPatterns != null) {
            for (String urlPattern : urlPatterns) {
                if (urlRequest.matches(urlPattern)) {
                    String msg = "Mobile Handler: URL redirection (%s) is skipped by configuration: %s";
                    log.info(String.format(msg, urlRequest, urlPattern));
                    return false;
                }
            }
        }

        return isRequestRedirectedToApplicationLoginForm(request);

    }

    @Override
    public boolean isRequestRedirectedToApplicationLoginForm(
            HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null) {
            return false;
        }

        for (String pattern : MOBILE_USER_AGENT_REGEXP) {
            if (userAgent.matches(pattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public RequestHandler init(Map<String, String> properties) {
        String urlSkippedPatterns = properties.get(URL_SKIPPED_PATTERNS_PROP);

        if (urlSkippedPatterns != null && !"".equals(urlSkippedPatterns)) {
            urlPatterns = urlSkippedPatterns.split("[|]");
        }

        return this;
    }

}
