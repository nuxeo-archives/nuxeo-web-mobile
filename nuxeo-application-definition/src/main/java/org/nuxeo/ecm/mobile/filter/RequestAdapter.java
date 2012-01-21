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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.platform.ui.web.auth.service.OpenUrlDescriptor;
import org.nuxeo.ecm.platform.ui.web.auth.service.PluggableAuthenticationService;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.ApplicationConstants.APPLICATION_SELECTED_COOKIE_NAME;
import static org.nuxeo.ecm.mobile.ApplicationConstants.TARGET_URL_PARAMETER;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.ERROR_USERNAME_MISSING;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_ERROR;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_FAILED;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_MISSING;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.REQUESTED_URL;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.START_PAGE_SAVE_KEY;

/**
 * Expose needed method on the request.
 *
 * @author bjalon
 *
 */
public class RequestAdapter {

    private static final Log log = LogFactory.getLog(RequestAdapter.class);

    // private static List<String> MOBILE_USER_AGENT_REGEXP = Arrays.asList(
    // "(.*)Mobile(.*)Safari(.*)", "(.*)AppleWebKit(.*)Mobile(.*)");
    //
    private String uri;

    private StringBuffer url;

    private Cookie[] cookies;

    private HttpServletRequest request;

    private PluggableAuthenticationService authenticationService;

    public RequestAdapter() {
    }

    public RequestAdapter(HttpServletRequest request) {
        uri = request.getRequestURI();
        url = request.getRequestURL();
        cookies = request.getCookies();
        this.request = request;

        if (cookies == null) {
            cookies = new Cookie[0];
        }

    }

    public String getUri() {
        return uri;
    }

    public StringBuffer getUrl() {
        return url;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    /**
     * return true if the PluggableAuthenticationService is configured to let
     * this following request URI open.
     */
    public boolean isOpenURL() {
        if (getAuthenticationService() != null) {
            List<OpenUrlDescriptor> openUrls = getAuthenticationService().getOpenUrls();
            for (OpenUrlDescriptor openUrl : openUrls) {
                if (openUrl.allowByPassAuth(request)) {
                    log.debug("Open URL (defined into the PluggableAuthenticationService) detected: "
                            + "no redirect: final URL: "
                            + request.getRequestURI());
                    return true;
                }
            }
        }
        return false;
    }

    private PluggableAuthenticationService getAuthenticationService() {
        if (authenticationService == null) {
            authenticationService = (PluggableAuthenticationService) Framework.getRuntime().getComponent(
                    PluggableAuthenticationService.NAME);
            if (authenticationService == null) {
                throw new RuntimeException("Unable to get Service "
                        + PluggableAuthenticationService.NAME);
            }
        }
        return authenticationService;
    }

    /**
     * Create a parameters map with parameters given into the request, initial
     * request before the login redirect (not before the application
     * redirection)
     *
     */
    public Map<String, String> getParameters()
            throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<String, String>();

        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            String value = request.getParameter(name);
            result.put(name, value);
        }

        HttpSession session = request.getSession(false);
        String requestedUrl = null;
        if (session != null) {
            requestedUrl = (String) session.getAttribute(START_PAGE_SAVE_KEY);
            if (requestedUrl != null && !"".equals(requestedUrl)) {
                result.put(REQUESTED_URL,
                        URLEncoder.encode(requestedUrl, "UTF-8"));
            }
        }

        String loginError = (String) request.getAttribute(LOGIN_ERROR);
        if (loginError != null) {
            if (ERROR_USERNAME_MISSING.equals(loginError)) {
                result.put(LOGIN_MISSING, "true");
            } else {
                result.put(LOGIN_FAILED, "true");
            }
        }
        return result;
    }

    /**
     * Create the parameter map with parameter given into the request and add
     * the initial request into the map into the
     * {@code ApplicationConstants#TARGET_URL_PARAMETER} key.
     *
     */
    public Map<String, String> getParametersAndAddTargetURL()
            throws UnsupportedEncodingException {
        Map<String, String> result = getParameters();

        if (!result.containsKey(TARGET_URL_PARAMETER)) {
            log.debug("Put the target URL into the URL parameter" + getUri());
            result.put(TARGET_URL_PARAMETER, getUri());
        } else {
            log.debug("Forward the target URL parameter again into the target URL parameter: " + result.get(TARGET_URL_PARAMETER));
        }
        return result;
    }

    /**
     * Return the application selected by the user - if no handler has matched -
     * (see the
     * {@code ApplicationDefinitionService#getTargetApplication(HttpServletRequest)}
     * ) for more information
     */
    public String getNavigationSelectionCookieValue() {
        for (Cookie cookie : cookies) {
            if (APPLICATION_SELECTED_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Return the initial request if this browser has been redirected by the
     * application definition service. if not redirected, the value return is
     * null;
     */
    public String getInitialRequest() {
        try {
            return getParameters().get(TARGET_URL_PARAMETER);
        } catch (UnsupportedEncodingException e) {
            log.error(e, e);
            return null;
        }
    }

    /**
     * return the uri with the initial URL into parameter of the uri
     */
    public static final String generateURIWithTargetURL(String redirectURI,
            String initialURI) {
        Map<String, String> param = new HashMap<String, String>();
        param.put(TARGET_URL_PARAMETER, initialURI);
        return URIUtils.addParametersToURIQuery(redirectURI, param);

    }

}
