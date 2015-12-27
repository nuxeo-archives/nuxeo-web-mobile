/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and others.
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
 * This service will store all the information needed to redirect the user to the right url according his navigation
 * choice or the request context. This is needed for instance when we want to redirect the user to a dedicated
 * application according the user-agent browser or logic implemented into your RequestHandler.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public interface ApplicationDefinitionService {

    /**
     * Return the root path of the web application where the request must be redirected according request context. If
     * there is no application that match this request context, null is returned. Absolute URL is retuned with the
     * protocole, the servername, ...
     */
    public String getApplicationBaseURL(HttpServletRequest request);

    public String getApplicationBaseURI(HttpServletRequest request);

    /**
     * Return the login url according request context. This is the absolute URL with the protocole, the servername, ...
     */
    public String getLoginURL(HttpServletRequest request);

    /**
     * Return the logout url according request context.This is the absolute URL with the protocole, the servername, ...
     */
    public String getLogoutURL(HttpServletRequest request);

    /**
     * Return the list of relative unprotected URI for all application
     */
    public List<String> getUnAuthenticatedURLPrefix();

    /**
     * Return the list of relative unprotected URI for this request
     */
    public List<String> getUnAuthenticatedURLPrefix(HttpServletRequest request);

    /**
     * Return true if the given request is a resource for the application this request match
     */
    public boolean isResourceURL(HttpServletRequest request);

}
