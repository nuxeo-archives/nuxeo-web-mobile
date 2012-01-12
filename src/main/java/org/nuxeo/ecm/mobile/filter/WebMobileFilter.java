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
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.mobile.WebMobileConstants;
import org.nuxeo.ecm.platform.ui.web.auth.service.OpenUrlDescriptor;
import org.nuxeo.ecm.platform.ui.web.auth.service.PluggableAuthenticationService;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.WebMobileConstants.getWebMobileURL;
import static org.nuxeo.ecm.mobile.WebMobileConstants.TARGET_URL_PARAMETER;
import static org.nuxeo.ecm.mobile.WebMobileConstants.getWebengineResourcesUrlPrefix;

/**
 * Filter that redirects to the mobile navigation URL, if mobile navigation
 * chosen. See {@code WebMobileNavigationSelectionFilter} to understand how the
 * navigation choice is fetched. If the navigation is not set into this filter,
 * the default choice will be gotten. Means for a mobile browser the Mobile
 * navigation will be chosen and Standard navigation for other.
 *
 * @author bjalon
 *
 */
public class WebMobileFilter implements Filter {

    protected static final Log log = LogFactory.getLog(WebMobileFilter.class);

    private String mobileHomeURL;

    private PluggableAuthenticationService service;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mobileHomeURL = getWebMobileURL();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            log.debug("Not an Http request, no redirection");
            doNoRedirect(request, response, chain);
        }

        RequestAdapter req = new RequestAdapterImpl(
                (HttpServletRequest) request);

        if (req.isStandardNavigationChosen()) {
            if (req.isMobileBrowser()) {
                log.debug("Mobile browser/Standard Navigation "
                        + "=> no redirect: target URL: " + req.getUrl());
            } else {
                log.debug("Desktop browser/Standard Navigation "
                        + "=> no redirect: target URL: " + req.getUrl());
            }
            doNoRedirect(request, response, chain);
            return;
        }

        if (req.isMobileNavigationChosen() && isMobileURL(req)) {
            log.debug("Mobile browser/Mobile Navigation/Web Mobile URL "
                    + "=> no redirect: target URL: " + req.getUrl());
            doNoRedirect(request, response, chain);
            return;
        }

        // not redirected urls
        if (isNotRedirectedURL((HttpServletRequest) request)) {
            doNoRedirect(request, response, chain);
        }

        doMobileRedirect((HttpServletRequest) request,
                (HttpServletResponse) response, chain);
    }

    /**
     * Return if the url is not redirected. We check open url declared into
     * {@link PluggableAuthenticationService} and also webengine resources.
     *
     * @return
     */
    protected boolean isNotRedirectedURL(HttpServletRequest request) {

        RequestAdapter req = new RequestAdapterImpl(
                (HttpServletRequest) request);

        if (getOpenURLsService() != null) {
            List<OpenUrlDescriptor> openUrls = service.getOpenUrls();
            for (OpenUrlDescriptor openUrl : openUrls) {
                if (openUrl.allowByPassAuth(request)) {
                    log.debug("Mobile browser/Mobile Navigation/Open URL "
                            + "(into PluggableAuthenticationService) "
                            + "=> no redirect: target URL: " + req.getUrl());
                    return true;
                }
            }
        }

        if (req.getUri().startsWith(getWebengineResourcesUrlPrefix())) {
            log.debug("Mobile browser/Mobile Navigation/Webengine resources "
                    + "=> no redirect: target URL: " + req.getFullURL());
            return true;
        }

        return false;
    }

    /**
     * Redirect the browser navigation to the mobile application with the
     * target URL as parameter into the
     * {@value WebMobileConstants#TARGET_URL_PARAMETER} url parameter.
     *
     */
    protected void doMobileRedirect(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        RequestAdapter req = new RequestAdapterImpl(
                (HttpServletRequest) request);

        Map<String, String> param = new HashMap<String, String>();
        param.put(TARGET_URL_PARAMETER, req.getUri());
        String redirect = URIUtils.addParametersToURIQuery(mobileHomeURL, param);
        log.debug("Mobile Browser/Mobile navigation => web mobile redirect: target URL: "
                + redirect);

        response.sendRedirect(redirect);
        response.flushBuffer();
        return;
    }

    /**
     * Do no redirection and let filters application to be done.
     */
    protected void doNoRedirect(ServletRequest request,
            ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        chain.doFilter(request, response);
        return;
    }

    /**
     * Return true is the request url is a mobile url.
     */
    protected boolean isMobileURL(RequestAdapter req) {
        return (req.getUri().startsWith(mobileHomeURL));
    }

    /**
     * Return context path
     */
    protected String getContextPath() {
        String contextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        log.debug("Nuxeo Context Path detected " + contextPath);
        if (contextPath == null) {
            contextPath = "/nuxeo";
            log.debug("Nuxeo Context Path default value set " + contextPath);
        }
        return contextPath;
    }

    protected PluggableAuthenticationService getOpenURLsService() {
        if (service == null && Framework.getRuntime() != null) {
            service = (PluggableAuthenticationService) Framework.getRuntime().getComponent(
                    PluggableAuthenticationService.NAME);
            if (service == null) {
                log.error("Unable to get Service "
                        + PluggableAuthenticationService.NAME);
                return null;
            }
        }
        return service;
    }

    @Override
    public void destroy() {
        log.debug("Filter detroyed");
    }

}
