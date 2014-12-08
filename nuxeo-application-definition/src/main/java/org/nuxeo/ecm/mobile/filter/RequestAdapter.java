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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.mobile.ApplicationDefinitionException;
import org.nuxeo.ecm.mobile.ApplicationDefinitionService;
import org.nuxeo.ecm.platform.ui.web.auth.service.OpenUrlDescriptor;
import org.nuxeo.ecm.platform.ui.web.auth.service.PluggableAuthenticationService;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.START_PAGE_SAVE_KEY;

/**
 * Request Wrapper with some needed method.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public class RequestAdapter {

    public static final String TARGET_URL_PARAMETER_NAME = "targetURL";

    private static final Log log = LogFactory.getLog(RequestAdapter.class);

    private HttpServletRequest request;

    private PluggableAuthenticationService authenticationService;

    private ApplicationDefinitionService service;

    public RequestAdapter(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * return true if the PluggableAuthenticationService is configured to let this following request URI open.
     */
    public boolean isOpenURL() {
        if (getAuthenticationService() != null) {
            List<OpenUrlDescriptor> openUrls = getAuthenticationService().getOpenUrls();
            for (OpenUrlDescriptor openUrl : openUrls) {
                if (openUrl.allowByPassAuth(request)) {
                    log.debug("Open URL (defined into the PluggableAuthenticationService) detected: "
                            + "no redirect: final URL: " + request.getRequestURI());
                    return true;
                }
            }

            if (service == null) {
                service = Framework.getLocalService(ApplicationDefinitionService.class);
            }

            String requestPage = getRequestedPage(request);

            for (String prefix : service.getUnAuthenticatedURLPrefix(request)) {
                if (requestPage.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static String getRequestedPage(HttpServletRequest httpRequest) {
        String requestURI = httpRequest.getRequestURI();
        String context = httpRequest.getContextPath() + '/';

        return requestURI.substring(context.length());
    }

    private String getRequestURIWithParameters() throws UnsupportedEncodingException {
        String queryString = request.getQueryString() != null ? "?" + URIUtils.getURIQuery(getParameters()) : "";
        return request.getRequestURI() + queryString;
    }

    private PluggableAuthenticationService getAuthenticationService() {
        if (authenticationService == null) {
            authenticationService = (PluggableAuthenticationService) Framework.getRuntime().getComponent(
                    PluggableAuthenticationService.NAME);
            if (authenticationService == null) {
                throw new RuntimeException("Unable to get Service " + PluggableAuthenticationService.NAME);
            }
        }
        return authenticationService;
    }

    /**
     * Create the parameter map with parameter given into the request and add the initial request into the map into the
     * {@code NXAuthConstants#REQUESTED_URL} key.
     */
    public Map<String, String> getParametersAndAddTargetURLIfNotSet() throws UnsupportedEncodingException {

        Map<String, String> result = new HashMap<String, String>();

        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            String value = request.getParameter(name);
            result.put(name, value);
        }

        HttpSession session = request.getSession(false);
        String targetUrl = null;
        if (session != null && !result.containsKey(TARGET_URL_PARAMETER_NAME)) {
            targetUrl = (String) session.getAttribute(START_PAGE_SAVE_KEY);
            if (targetUrl != null && !"".equals(targetUrl.trim())) {
                log.debug("Put the target URL into the URL parameter: " + request.getRequestURI());
                result.put(TARGET_URL_PARAMETER_NAME, URLEncoder.encode(targetUrl, "UTF-8"));
            } else {
                result.put(TARGET_URL_PARAMETER_NAME, getRequestURIWithParameters());
            }
        }
        return result;
    }

    public String getTargetURLFromParameter() throws ApplicationDefinitionException {
        try {
            return getParameters().get(TARGET_URL_PARAMETER_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationDefinitionException(e.getMessage(), e);
        }
    }

    /**
     * Return map containing parameters given into the request
     */
    public Map<String, String> getParameters() throws UnsupportedEncodingException {

        Map<String, String> result = new HashMap<String, String>();

        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            String value = request.getParameter(name);
            result.put(name, value);
        }

        return result;
    }

}
