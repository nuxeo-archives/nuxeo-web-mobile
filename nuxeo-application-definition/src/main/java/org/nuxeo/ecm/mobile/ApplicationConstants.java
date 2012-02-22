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
     * After Browser redirect to the application the initial URL asked by the
     * user is store as a parameter into the URI into this parameter name the
     * application URL parameter that will store the initial URL ask by the user
     * or a application handler. Application will use the value of this
     * parameter to navigate (into the application UI) to the inital resource
     * ask by the user.
     */
    public static final String TARGET_URL_PARAMETER = "targetURL";
}
