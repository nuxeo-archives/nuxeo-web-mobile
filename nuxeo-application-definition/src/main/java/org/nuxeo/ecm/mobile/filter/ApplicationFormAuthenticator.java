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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.mobile.ApplicationDefinitionService;
import org.nuxeo.ecm.platform.api.login.UserIdentificationInfo;
import org.nuxeo.ecm.platform.ui.web.auth.interfaces.LoginResponseHandler;
import org.nuxeo.ecm.platform.ui.web.auth.interfaces.NuxeoAuthenticationPlugin;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.ERROR_USERNAME_MISSING;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.FORM_SUBMITTED_MARKER;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_ERROR;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.START_PAGE_SAVE_KEY;

/**
 * Authenticator that redirects user to dedicated application authentication
 * form if user has selected this application or if this application matched the
 * request context.
 * 
 * @author bjalon
 * 
 */
public class ApplicationFormAuthenticator implements LoginResponseHandler,
        NuxeoAuthenticationPlugin {

    protected static final Log log = LogFactory.getLog(ApplicationFormAuthenticator.class);

    private ApplicationDefinitionService service;

    private String usernameKey = "user_name";

    private String passwordKey = "user_password";

    public ApplicationDefinitionService getService() {
        if (service == null) {
            service = Framework.getLocalService(ApplicationDefinitionService.class);
        }
        return service;
    }

    @Override
    public void initPlugin(Map<String, String> parameters) {
        if (parameters.get("UsernameKey") != null) {
            usernameKey = parameters.get("UsernameKey");
        }
        if (parameters.get("PasswordKey") != null) {
            passwordKey = parameters.get("PasswordKey");
        }
    }

    @Override
    public Boolean needLoginPrompt(HttpServletRequest httpRequest) {
        if (getService().getApplicationBaseURL(httpRequest) != null) {
            return true;
        }
        log.debug("No Application match this request, next authenticator to expose login prompt");
        return false;
    }

    @Override
    public Boolean handleLoginPrompt(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, String baseURL) {
        if (log.isDebugEnabled()) {
            log.debug("Login Prompt - URL :" + httpRequest.getRequestURL()
                    + "?" + httpRequest.getQueryString());
        }

        String loginPage = getService().getLoginURL(httpRequest);
        if (loginPage == null) {
            log.debug("No Application matched for this request context, so next Authenticator in Chain will be used");
            return Boolean.FALSE;
        }

        log.debug("Application matched for this request context so login page "
                + "used will be the target application detected: " + loginPage);

        Map<String, String> parameters;
        try {
            RequestAdapter adapter = new RequestAdapter(httpRequest);
            parameters = adapter.getParameters();
            // avoid resending the password in clear !!!
            parameters.remove(passwordKey);
        } catch (UnsupportedEncodingException e) {
            log.error(e, e);
            return Boolean.FALSE;
        }
        String redirectUrl = URIUtils.addParametersToURIQuery(loginPage,
                parameters);
        try {
            httpResponse.sendRedirect(redirectUrl);
            return Boolean.TRUE;
        } catch (IOException e) {
            log.error(e, e);
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean onError(HttpServletRequest request,
            HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("On Error - URL :" + request.getRequestURL() + "?"
                    + request.getQueryString());
        }
        Map<String, String> params;
        String redirect = request.getRequestURI();
        try {
            params = new RequestAdapter(request).getParameters();
            params.remove(usernameKey);
            params.remove("Submit");
            params.remove(passwordKey);
            redirect = URIUtils.addParametersToURIQuery(
                    request.getRequestURI(), params);
        } catch (UnsupportedEncodingException e) {
            log.error("Can't transmit param on login post error, "
                    + "problem during parameter extraction", e);
        }
        try {
            response.sendRedirect(redirect);
            response.flushBuffer();
            return true;
        } catch (IOException e) {
            log.error("Problem during the redirect to the login form "
                    + "after bad authentication value send", e);
            return false;
        }
    }

    @Override
    public boolean onSuccess(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        if (log.isDebugEnabled()) {
            log.debug("On success - URL :" + httpRequest.getRequestURL() + "?"
                    + httpRequest.getQueryString());
        }
        Map<String, String> parameters;
        try {
            RequestAdapter adapter = new RequestAdapter(httpRequest);
            parameters = adapter.getParameters();
            // avoid resending the password in clear !!!
            parameters.remove(passwordKey);
        } catch (UnsupportedEncodingException e) {
            log.error(e, e);
            return Boolean.FALSE;
        }

        String initialEncodedRequest = parameters.get(START_PAGE_SAVE_KEY);
        if (initialEncodedRequest != null) {
            try {
                String value = URLDecoder.decode(initialEncodedRequest, "UTF-8");
                httpResponse.sendRedirect(value);
                httpResponse.flushBuffer();
            } catch (IOException e) {
                log.error(e, e);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getUnAuthenticatedURLPrefix() {
        List<String> result = getService().getUnAuthenticatedURLPrefix();
        log.debug("List of skipped URL:" + result);
        return result;
    }

    @Override
    public UserIdentificationInfo handleRetrieveIdentity(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        if (getService().getApplicationBaseURL(httpRequest) == null) {
            log.debug("No Application match this request, use next Authenticator "
                    + "in chain to retrieve identity");
            return null;
        }
        String userName = httpRequest.getParameter(usernameKey);
        String password = httpRequest.getParameter(passwordKey);

        if (httpRequest.getParameter(FORM_SUBMITTED_MARKER) != null
                && (userName == null || userName.length() == 0)) {
            httpRequest.setAttribute(LOGIN_ERROR, ERROR_USERNAME_MISSING);
        }
        if (userName == null || userName.length() == 0) {
            return null;
        }
        return new UserIdentificationInfo(userName, password);
    }

}
