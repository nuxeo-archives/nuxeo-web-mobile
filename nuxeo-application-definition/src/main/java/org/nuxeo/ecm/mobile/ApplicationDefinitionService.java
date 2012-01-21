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
package org.nuxeo.ecm.mobile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This service will store all the information needed to redirect the user to
 * the right url according his navigation choice or the request context. This is
 * needed for instance when we want to redirect the user to a dedicated
 * application according the user-agent browser or the application chosen into
 * the {@code ApplicationSelectionViewService#getApplicationSelectionURL()}.
 *
 * @author bjalon
 *
 */
public interface ApplicationDefinitionService {

    /**
     * Return the application targeted by the request. The target application is
     * the first application handler ({@code RequestHandler}) that matches the
     * request context or the one described into the
     * {@code ApplicationConstants#APPLICATION_SELECTED_COOKIE_NAME} cookie.
     * This handler is defined into the application definition contribution (
     * {@code ApplicationDescriptor}).
     */
    public ApplicationDefinitionDescriptor getTargetApplication(
            HttpServletRequest request);

    /**
     * Return the default navigation type according the user-agent browser. If
     * value stored is null or cookie not set, the default navigation type will
     * be returned: be specific for mobile. If the cookie store the true value,
     * the standard navigation (JSF) for non mobile browser and mobile
     * navigation for mobile browser. Redirection to the correct url is managed
     * by {@code ApplicationSelectionFilter}.
     */

    /**
     * Return true, if before first connexion, a page is displayed to the user
     * to choose the kind of navigation the user wants for mobile browser. The
     * page url is given by
     * {@code WebMobileDefinition#getNavigationSelectionURL()}.
     */

    /**
     * Return view exposed to mobile user to choose the kind of navigation the
     * user wants (if enabled). You must take care to let this url unprotected
     * (to disable nuxeo authentication filter) and all resources used as you
     * want expose this view before authentication. See {@link http
     * ://explorer.nuxeo.org/nuxeo/site/distribution
     * /Nuxeo%20Platform-5.5/viewExtensionPoint
     * /org.nuxeo.ecm.platform.ui.web.auth
     * .service.PluggableAuthenticationService--openUrl}
     */

    /**
     * Return the root path of the web application according request context.
     */
    public String getApplicationBaseURL(HttpServletRequest request);

    /**
     * Return the login view url according request context
     */
    public String getLoginURL(HttpServletRequest request);

    /**
     * Return the logout view url according request context
     */
    public String getLogoutURL(HttpServletRequest request);

    /**
     * return the list of login pages (witout nuxeo context path)
     */
    public List<String> getUnAuthenticatedURLPrefix();

    /**
     * return the base URL of resources needed by the application
     */
    public String getResourcesApplicationBaseURL(HttpServletRequest req);

}
