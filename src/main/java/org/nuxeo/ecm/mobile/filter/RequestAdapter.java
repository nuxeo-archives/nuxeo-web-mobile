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

import javax.servlet.http.Cookie;

import org.nuxeo.ecm.mobile.WebMobileConstants;

/**
 * Adapter that expose information needed by filters to let the user navigation
 * to the right url.
 *
 * @author bjalon
 *
 */
public interface RequestAdapter {

    public abstract String getUri();

    public abstract StringBuffer getUrl();

    public abstract Cookie[] getCookies();

    public abstract boolean isMobileBrowser();

    /**
     * return true if the broser is not mobile. return also true is the standard
     * navigation has been chosen by the user and false if the user has not
     * chosen the navigation type or have chosen the mobile navigation. The
     * navigation choice is stored into cookie named
     * {@value WebMobileConstants#FORCE_STANDARD_NAVIGATION_COOKIE_NAME}. If the
     * cookie is not set means the user has not yet select his type of
     * navigation.
     *
     */
    boolean isStandardNavigationChosen();

    /**
     * return true is the mobile navigation has been chosen by the user and
     * false if the user has not chosen the navigation type or have chosen the
     * standard navigation or the browser is not a mobile. The navigation choice
     * is stored into cookie named
     * {@value WebMobileConstants#FORCE_STANDARD_NAVIGATION_COOKIE_NAME}. If the
     * cookie is not set means the user has not yet select his type of
     * navigation (if his browser is a mobile browser)
     *
     */
    boolean isMobileNavigationChosen();

    /**
     * return true if the browser is not mobile or if the browser is mobile and
     * the {@value WebMobileConstants#FORCE_STANDARD_NAVIGATION_COOKIE_NAME}
     * cookie has been set.
     *
     */
    boolean isNavigationChosen();

    /**
     * Return the URL with parameters set
     */
    public abstract String getFullURL();

}