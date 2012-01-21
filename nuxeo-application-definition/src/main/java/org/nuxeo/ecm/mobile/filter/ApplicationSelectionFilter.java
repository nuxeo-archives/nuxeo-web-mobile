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
import org.nuxeo.ecm.mobile.ApplicationDefinitionService;
import org.nuxeo.ecm.mobile.ApplicationSelectionViewService;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.filter.RequestAdapter.generateURIWithTargetURL;

/**
 * Filter that redirects request to the application selection view if enabled.
 * You can find more information in {@code ApplicationSelectionViewService}. The
 * redirection is executed if no application selected and no application handler
 * match
 *
 * @author bjalon
 *
 */
public class ApplicationSelectionFilter implements Filter {

    protected static final Log log = LogFactory.getLog(ApplicationSelectionFilter.class);

    private ApplicationSelectionViewService selectionViewService;

    private ApplicationDefinitionService applicationDefinitionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (selectionViewService == null) {
            selectionViewService = Framework.getLocalService(ApplicationSelectionViewService.class);
        }
        if (applicationDefinitionService == null) {
            applicationDefinitionService = Framework.getLocalService(ApplicationDefinitionService.class);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (!selectionViewService.isApplicationSelectionViewEnabled()) {
            log.debug("Application Selection View disabled");
            chain.doFilter(request, response);
            return;
        }

        if (applicationDefinitionService.getTargetApplication((HttpServletRequest) request) != null) {
            log.debug("Navigation already chosen, no redirection needed.");
            chain.doFilter(request, response);
            return;
        }

        RequestAdapter reqAdapter = new RequestAdapter();

        if (reqAdapter.getNavigationSelectionCookieValue() != null) {
            log.debug("User has chosen the default navigation as cookie "
                    + "value doesn't match with any Application name");
            chain.doFilter(request, response);
            return;
        }

        if (reqAdapter.isOpenURL()) {
            log.debug("Open url target, no redirection.");
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse res = (HttpServletResponse) response;
        String applicationSelectionURL = selectionViewService.getApplicationSelectionURL();
        String redirect = generateURIWithTargetURL(applicationSelectionURL,
                reqAdapter.getUri());

        log.debug("Kind of navigation not chosen, redirect to the application selection: "
                + "Final URL target: " + redirect);
        res.sendRedirect(redirect);
        res.flushBuffer();

    }

    @Override
    public void destroy() {
        log.debug("Filter detroyed");
    }

}
