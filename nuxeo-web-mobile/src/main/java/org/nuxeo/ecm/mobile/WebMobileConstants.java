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

import org.nuxeo.ecm.mobile.filter.WebMobileNavigationSelectionFilter;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.webengine.WebEngine.SKIN_PATH_PREFIX_KEY;

/**
 * Contains all constants of the Mobile Application.
 *
 * @author bjalon
 *
 */
public class WebMobileConstants {

    /**
     * Name of the cookie that will store the Navigation choice for mobile
     * browser. if value stored is false, or the cookie don't exist the
     * navigation be specific for mobile. If the cookie store the true value,
     * the navigation will be standard (JSF). See the
     * {@link WebMobileNavigationSelectionFilter}.
     */
    public static final String FORCE_STANDARD_NAVIGATION_COOKIE_NAME = "ForceStandardNavigationEnabled";

    /**
     * Parameter that will store if the
     * {@link WebMobileNavigationSelectionFilter} is enable or not. By default
     * the filter is disabled.
     */
    public static final String NAVIGATION_FILTER_ACTIVATION_PARAMETER = "org.nuxeo.mobile.navigation.filter";

    /**
     * URL Parameter name that will store the initial target wanted to reach.
     * This is used when the user is not logged and the browser is redirected to
     * the login form. The target URL is maintained into this parameter.
     */
    public static final String TARGET_URL_PARAMETER = "targetURL";

    public static boolean isUnitTestExecution = false;

    public static String getNuxeoContextPath() {
        if (NUXEO_CONTEXT_PATH == null) {
            if (isUnitTestExecution) {
                return "/nuxeo";
            } else {
                NUXEO_CONTEXT_PATH = Framework.getProperty("org.nuxeo.ecm.contextPath");
            }
        }
        return NUXEO_CONTEXT_PATH;
    }

    /**
     * Return the root path of the web mobile application
     */
    public static String getWebMobileURL() {
        return getNuxeoContextPath() + WEB_MOBILE_HOME_URL;
    }

    private static final String WEB_MOBILE_HOME_URL = "/site/mobile";

    /**
     * return Unprotected prefix url path used to expose specific resources that
     * must go through the authentication system. For instance login form or the
     * navigation selection.
     */
    public static String getUnprotectedURLPrefix() {
        return getNuxeoContextPath() + UNPROTECTED_URL_PREFIX;
    }

    /**
     * return URL path for the navigation selection view.
     */
    public static String getNavigationSelectionURL() {
        return getUnprotectedURLPrefix() +  NAVIGATION_SELECTION_URL;
    }

    /**
     * return URL Path prefix for webengine resources
     */
    public static String getWebengineResourcesUrlPrefix() {
        if (WEBENGINE_RESOURCES_URL_PREFIX == null) {
            if (isUnitTestExecution) {
                WEBENGINE_RESOURCES_URL_PREFIX = "/nuxeo/site/resources";
            } else {
                WEBENGINE_RESOURCES_URL_PREFIX = Framework.getProperty(SKIN_PATH_PREFIX_KEY);
            }
        }
        return WEBENGINE_RESOURCES_URL_PREFIX;
    }

    private static String WEBENGINE_RESOURCES_URL_PREFIX = null;

    private static String NUXEO_CONTEXT_PATH = null;

    private static final String UNPROTECTED_URL_PREFIX = "/web-mobile/";

    private static final String NAVIGATION_SELECTION_URL = "navigation-choice.jsp";

}
