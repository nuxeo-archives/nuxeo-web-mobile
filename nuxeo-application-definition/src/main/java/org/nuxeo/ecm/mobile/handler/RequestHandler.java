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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * A mobile application redirection service provide a way to expose easily a new
 * dedicated UI for a specific type of environment (essentially user agent
 * browser). Pointer of the implementation of this interface is given into the
 * Application descriptor. Here is implemented the logic to tell if the request
 * is candidate for the application described.
 * 
 * @author bjalon
 * 
 */
public interface RequestHandler {

    /**
     * Used for initialize request handler with properties given in descriptor
     * definition.
     */
    public RequestHandler init(Map<String, String> properties);

    /**
     * return true if the request is a candidate for the Application described
     * into the {@code ApplicationDescriptor}.
     */
    public boolean isRequestRedirectedToApplication(HttpServletRequest request);

    /**
     * return true if the request is a candidate for the specific login form
     * described by application
     */
    public boolean isRequestRedirectedToApplicationLoginForm(
            HttpServletRequest request);

}
