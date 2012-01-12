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
import org.nuxeo.ecm.platform.ui.web.auth.service.OpenUrlDescriptor;
import org.nuxeo.ecm.platform.ui.web.auth.service.PluggableAuthenticationService;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.WebMobileConstants.getNavigationSelectionURL;
import static org.nuxeo.ecm.mobile.WebMobileConstants.isUnitTestExecution;

/**
 * Filter that redirects request to navigation selection (Mobile or standard) if
 * mobile web browser make the request. By default this Filter is disabled, if
 * you want enable it, you just have to set true for the parameter
 * {@code NAVIGATION_FILTER_ENABLE_PARAMETER_NAME} (into config/nuxeo.conf for
 * instance)
 *
 * @author bjalon
 *
 */
public class WebMobileNavigationSelectionFilter implements Filter {

    public static final String NAVIGATION_FILTER_ENABLE_PARAMETER_NAME = "org.nuxeo.mobile.navigation.filter.enable";

    protected static final Log log = LogFactory.getLog(WebMobileNavigationSelectionFilter.class);

    private String navigationSelectionURL;

    private PluggableAuthenticationService service;

    private boolean enable;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (isUnitTestExecution) {
            enable = true;
        } else {
            enable = Boolean.parseBoolean(Framework.getProperty(NAVIGATION_FILTER_ENABLE_PARAMETER_NAME));
        }

        String navSelectionURLSuffix = filterConfig.getInitParameter("navigation.selection.url");
        if (navSelectionURLSuffix != null && !"".equals(navSelectionURLSuffix)) {
            navigationSelectionURL = getNavigationSelectionURL();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (!enable) {
            log.debug("Filter disabled");
            chain.doFilter(request, response);
            return;
        }

        RequestAdapter req = new RequestAdapterImpl(
                (HttpServletRequest) request);
        if (req.isNavigationChosen()) {
            log.debug("Navigation already chosen, no redirection needed.");
            chain.doFilter(request, response);
            return;
        }

        if (getOpenURLsService() != null) {
            List<OpenUrlDescriptor> openUrls = service.getOpenUrls();
            for (OpenUrlDescriptor openUrl : openUrls) {
                if (openUrl.allowByPassAuth((HttpServletRequest) request)) {
                    log.debug("Open url target, no redirection: "
                            + navigationSelectionURL);
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        HttpServletResponse res = (HttpServletResponse) response;

        log.debug("Kind of navigation not chosen, redirect to the question about mobile navigation: "
                + req.getUrl());
        Map<String, String> param = new HashMap<String, String>();
        param.put("initialURLRequested", req.getUri());
        String redirect = URIUtils.addParametersToURIQuery(
                navigationSelectionURL, param);
        log.debug("URL target: " + redirect);
        res.sendRedirect(redirect);
        res.flushBuffer();

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
