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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.RequestFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.WebMobileConstants.FORCE_STANDARD_NAVIGATION_COOKIE_NAME;
import static org.nuxeo.ecm.mobile.WebMobileConstants.MOBILE_HOME_URL_SUFFIX;
import static org.nuxeo.ecm.mobile.WebMobileConstants.NAVIGATION_SELECTION_URL_SUFFIX;
import static org.nuxeo.ecm.mobile.WebMobileConstants.isMobileUserAgent;

public class WebMobileFilter implements Filter {

    protected static final Log log = LogFactory.getLog(WebMobileFilter.class);

    private static String mobileHomeURL;

    public static String navigationSelectionURL;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (mobileHomeURL == null) {
            log.debug("Initialize Filter parameters");
            String contextPath = getContextPath();

            mobileHomeURL = contextPath + MOBILE_HOME_URL_SUFFIX;
            navigationSelectionURL = contextPath
                    + NAVIGATION_SELECTION_URL_SUFFIX;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            log.debug("Not a http request, so move to the next Filter");
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!isMobileBrowserRequester(req)) {
            log.debug("Not Mobile device, so move to the next filter");
            chain.doFilter(request, response);
            return;
        }
        
        String initialURL = req.getRequestURI();
        if (initialURL == null || initialURL.startsWith(mobileHomeURL) || initialURL.startsWith(navigationSelectionURL) || initialURL.startsWith("/nuxeo/mobile/")) {
            log.debug("Mobile device, and mobile URL so move to the next filter");
            chain.doFilter(request, response);
            return;
            
        }

        // If request is RequestFacade get cookie throw an error
        if (! (request instanceof RequestFacade)) {
            for (Cookie cookie : req.getCookies()) {
                if (FORCE_STANDARD_NAVIGATION_COOKIE_NAME.equals(cookie.getName())) {
                    if ("true".equals(cookie.getValue())) {
                        log.debug("User don't want mobile navigation");
                        chain.doFilter(request, response);
                        return;
                    } else {
                        log.debug("User want mobile navigation");
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("initialURLRequested", initialURL);
                        String redirect = URIUtils.addParametersToURIQuery(
                                mobileHomeURL, param);
                        res.sendRedirect(redirect);
                        res.flushBuffer();
                        return;

                    }
                }
            }
        } else {
            log.debug("RequestFacade so no cookie exposed: throw Exception");
        }

        log.debug("Redirect to the question about mobile navigation");
        Map<String, String> param = new HashMap<String, String>();
        param.put("initialURLRequested", initialURL);
        String redirect = URIUtils.addParametersToURIQuery(
                navigationSelectionURL, param);
        res.sendRedirect(redirect);
        res.flushBuffer();

    }

    protected String getContextPath() {
        String contextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        log.debug("Nuxeo Context Path detected " + contextPath);
        if (contextPath == null) {
            contextPath = "/nuxeo";
            log.debug("Nuxeo Context Path default value set " + contextPath);
        }
        return contextPath;
    }

    protected boolean isMobileBrowserRequester(HttpServletRequest request) {
        return isMobileUserAgent(request);
    }

    @Override
    public void destroy() {
        log.debug("Filter detroyed");
    }

}
