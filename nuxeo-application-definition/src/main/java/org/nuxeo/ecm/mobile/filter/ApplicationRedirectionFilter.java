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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.mobile.ApplicationDefinitionService;
import org.nuxeo.ecm.platform.ui.web.auth.CachableUserIdentificationInfo;
import org.nuxeo.ecm.platform.ui.web.auth.NuxeoAuthenticationFilter;
import org.nuxeo.runtime.api.Framework;

/**
 * Filter that redirects to the chosen application URL or application dedicated for the context request.
 * To understand the application selection engine look {@code ApplicationDefinitionService#getTargetApplication(HttpServletRequest).
 * If no application match, no redirection will be executed.
 *
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 *
 */
public class ApplicationRedirectionFilter implements Filter {

    protected static final Log log = LogFactory.getLog(ApplicationRedirectionFilter.class);

    public static final String INITIAL_TARGET_URL_PARAM_NAME = "targetURL";

    private ApplicationDefinitionService service;

    protected ApplicationDefinitionService getService() {
        if (service == null) {
            service = Framework.getLocalService(ApplicationDefinitionService.class);
        }
        return service;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (!(request instanceof HttpServletRequest)) {
            log.debug("Not an Http request, no redirection");
            doNoRedirect(request, response, chain);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        log.debug("do filter - URL :" + req.getRequestURL() + "?" + req.getQueryString());

        if (!isAuthenticated(req)) {
            log.debug("User not authenticated so no application redirection");
            doNoRedirect(request, response, chain);
            return;
        }

        String targetApplicationBaseURL = getService().getApplicationBaseURI(req);

        if (targetApplicationBaseURL == null) {
            log.debug("No application match this request context " + "=> no redirect: final URL: "
                    + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        if (isRequestIntoApplication(req, targetApplicationBaseURL)) {
            log.debug("Request URI is a child of target application so no redirect:" + " final URL: "
                    + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        if (getService().isResourceURL(req)) {
            log.debug("Request URI is a resource of the target application so no redirect:" + " final URL: "
                    + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        RequestAdapter requestAdapter = new RequestAdapter(req);
        if (requestAdapter.isOpenURL()) {
            log.debug("Request URI is opened so no redirect:" + " final URL: " + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        doApplicationRedirection((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    /**
     * Return principal stored into session
     */
    private boolean isAuthenticated(HttpServletRequest req) {
        if (req.getSession(false) == null) {
            return false;
        }

        CachableUserIdentificationInfo idInfo = (CachableUserIdentificationInfo) req.getSession().getAttribute(
                "org.nuxeo.ecm.login.identity");

        if (idInfo == null || idInfo.getPrincipal() == null) {
            return false;
        }

        return true;
    }

    /**
     * Redirect the browser to the target application with the initial URL as parameter into the
     * {@value WebMobileConstants#TARGET_URL_PARAMETER} url parameter.
     */
    private void doApplicationRedirection(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Map<String, String> parameters = new HashMap<String, String>();

        String requestedPage = NuxeoAuthenticationFilter.getRequestedPage(request);
        if (!StringUtils.isBlank(requestedPage)) {
            parameters.put(INITIAL_TARGET_URL_PARAM_NAME, requestedPage);
        }

        String redirectURI = URIUtils.addParametersToURIQuery(getService().getApplicationBaseURL(request), parameters);
        log.debug("Handler match/Non target application URI " + "=> Application redirected: target URL: " + redirectURI);

        response.sendRedirect(redirectURI);
    }

    /**
     * Do no redirection and let filters application to be done.
     */
    private void doNoRedirect(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        chain.doFilter(request, response);
    }

    private boolean isRequestIntoApplication(HttpServletRequest req, String targetApplicationBaseURL) {
        String uri = req.getRequestURI();

        log.debug("Request url: " + uri + " and targetApplicationURI: ");
        if (!uri.startsWith(targetApplicationBaseURL)) {
            log.debug("Request uri is not a child of application base url");
            return false;
        }
        if (uri.equals(targetApplicationBaseURL)) {
            log.debug("Request uri is the root of the application");
            return true;
        }
        char character = uri.charAt(targetApplicationBaseURL.length());
        if (character != '/' && character != '?' && character != '#' && character != '@') {
            log.debug("Request uri is not a child of application base url");
            return false;
        }
        log.debug("Request uri is a child of application base url");
        return true;
    }

    @Override
    public void destroy() {
        log.debug("Filter detroyed");
    }

}
