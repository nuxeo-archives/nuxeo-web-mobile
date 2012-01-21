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

/**
 * @author bjalon
 *
 */
public class ApplicationConstants {

    /**
     * Name of the cookie that will store the application name chosen by the
     * user in
     * {@code ApplicationSelectionViewService#getApplicationSelectionURL()} or
     * selecting according the request context
     * {@code ApplicationDefinitionService#getTargetApplication(javax.servlet.http.HttpServletRequest)}
     * . See more information into
     * {@code ApplicationDefinitionService#getTargetApplication(HttpServletRequest)}
     * . Once the selection found the application target is stored into the
     * cookie named {@code APPLICATION_SELECTED_COOKIE_NAME}
     */
    public static final String APPLICATION_SELECTED_COOKIE_NAME = "applicationSelected";

    /**
     * After Browser redirect to the application the initial URL asked by the
     * user is store as a parameter into the URI into this parameter name the
     * application URL parameter that will store the initial URL ask by the user
     * or a application handler. Application will use the value of this
     * parameter to navigate (into the application UI) to the inital resource
     * ask by the user.
     */
    public static final String TARGET_URL_PARAMETER = "targetURL";
}
