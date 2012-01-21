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
import org.nuxeo.ecm.mobile.ApplicationDefinitionService;
import org.nuxeo.runtime.api.Framework;

/**
 * Filter that redirects to the chosen application URL or application dedicated for the context request.
 * To understand the application selection engine look {@code ApplicationDefinitionService#getTargetApplication(HttpServletRequest).
 * If no application match, no redirection will be executed.
 *
 * @author bjalon
 *
 */
public class ApplicationRedirectionFilter implements Filter {

    protected static final Log log = LogFactory.getLog(ApplicationRedirectionFilter.class);

    private ApplicationDefinitionService service;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (service == null) {
            service = Framework.getLocalService(ApplicationDefinitionService.class);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            log.debug("Not an Http request, no redirection");
            doNoRedirect(request, response, chain);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        String baseURL = service.getApplicationBaseURL(req);
        String resourcesBaseURL = service.getResourcesApplicationBaseURL(req);

        if (baseURL == null) {
            log.debug("No application match this request context "
                    + "=> no redirect: final URL: " + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        if (req.getRequestURI().startsWith(baseURL)) {
            log.debug("Request URI is the target application so no redirect:"
                    + " final URL: " + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        if (resourcesBaseURL != null && req.getRequestURI().startsWith(resourcesBaseURL)) {
            log.debug("Request URI is a resource of the target application so no redirect:"
                    + " final URL: " + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        RequestAdapter requestAdapter = new RequestAdapter(req);
        if (requestAdapter.isOpenURL()) {
            log.debug("Request URI is opened so no redirect:" + " final URL: "
                    + req.getRequestURI());
            doNoRedirect(request, response, chain);
            return;
        }

        doApplicationRedirection((HttpServletRequest) request,
                (HttpServletResponse) response, chain);
    }

    /**
     * Redirect the browser to the target application with the initial URL as
     * parameter into the {@value WebMobileConstants#TARGET_URL_PARAMETER} url
     * parameter.
     *
     */
    private void doApplicationRedirection(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        RequestAdapter adapter = new RequestAdapter(request);
        String redirectURI = URIUtils.addParametersToURIQuery(
                service.getApplicationBaseURL(request),
                adapter.getParametersAndAddTargetURL());
        log.debug("Handler or cookie match/Non target application URI "
                + "=> Application redirected: target URL: " + redirectURI);

        response.sendRedirect(redirectURI);
        response.flushBuffer();
        return;
    }

    /**
     * Do no redirection and let filters application to be done.
     */
    private void doNoRedirect(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.debug("Filter detroyed");
    }

}
