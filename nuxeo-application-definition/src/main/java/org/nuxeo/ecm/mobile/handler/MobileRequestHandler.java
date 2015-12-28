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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public class MobileRequestHandler implements RequestHandler {

    public static final String URL_SKIPPED_PATTERNS_PROP = "urlSkippedPatterns";

    private static final Log log = LogFactory.getLog(MobileRequestHandler.class);

    private static List<String> MOBILE_USER_AGENT_REGEXP = Arrays.asList("(.*)Mobile(.*)Safari(.*)",
            "(.*)AppleWebKit(.*)Mobile(.*)", "(.*)Android(.*)");

    private String[] urlPatterns;

    @Override
    public boolean isRequestRedirectedToApplication(HttpServletRequest request) {

        // Some resources are skipped (nxfile pattern for instance, ...) but we
        // want login page displayed for mobile
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";

        String urlRequest = request.getRequestURL() + queryString;

        if (urlPatterns != null) {
            for (String urlPattern : urlPatterns) {
                if (urlRequest.matches(urlPattern)) {
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("Mobile Handler: URL redirection (%s) is skipped by configuration: %s",
                                urlRequest, urlPattern));
                    }
                    return false;
                }
            }
        }

        return isRequestRedirectedToApplicationLoginForm(request);

    }

    @Override
    public boolean isRequestRedirectedToApplicationLoginForm(HttpServletRequest request) {
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
