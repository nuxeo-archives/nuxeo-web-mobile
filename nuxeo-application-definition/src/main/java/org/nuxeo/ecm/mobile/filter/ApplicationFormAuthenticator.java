/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     bjalon
 */
package org.nuxeo.ecm.mobile.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.mobile.ApplicationDefinitionService;
import org.nuxeo.ecm.platform.api.login.UserIdentificationInfo;
import org.nuxeo.ecm.platform.ui.web.auth.interfaces.NuxeoAuthenticationPlugin;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.ERROR_USERNAME_MISSING;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.FORM_SUBMITTED_MARKER;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_ERROR;

/**
 * Authenticator that redirects user to dedicated application authentication form if user has selected this application
 * or if this application matched the request context. Can't extends Form Authentication as login form is static and
 * here is dynamic.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public class ApplicationFormAuthenticator implements NuxeoAuthenticationPlugin {

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
    public Boolean handleLoginPrompt(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String baseURL) {
        log.debug("Login Prompt - URL :" + httpRequest.getRequestURL() + "?" + httpRequest.getQueryString());

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
            parameters = adapter.getParametersAndAddTargetURLIfNotSet();
            // avoid resending the password in clear !!!
            parameters.remove(passwordKey);
        } catch (UnsupportedEncodingException e) {
            log.error(e, e);
            return Boolean.FALSE;
        }
        String redirectUrl = URIUtils.addParametersToURIQuery(loginPage, parameters);
        try {
            httpResponse.sendRedirect(redirectUrl);
            return Boolean.TRUE;
        } catch (IOException e) {
            log.error(e, e);
        }
        return Boolean.FALSE;
    }

    @Override
    public List<String> getUnAuthenticatedURLPrefix() {
        List<String> result = getService().getUnAuthenticatedURLPrefix();
        log.debug("List of skipped URL:" + result);
        return result;
    }

    @Override
    public UserIdentificationInfo handleRetrieveIdentity(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        if (getService().getApplicationBaseURL(httpRequest) == null) {
            log.debug("No Application match this request, use next Authenticator " + "in chain to retrieve identity");
            return null;
        }

        Map<String, String> parameters;
        try {
            RequestAdapter adapter = new RequestAdapter(httpRequest);
            parameters = adapter.getParametersAndAddTargetURLIfNotSet();
            // avoid resending the password in clear !!!
        } catch (UnsupportedEncodingException e) {
            log.error(e, e);
            return null;
        }

        String userName = parameters.get(usernameKey);
        String password = parameters.get(passwordKey);

        if (parameters.get(FORM_SUBMITTED_MARKER) != null && (userName == null || userName.length() == 0)) {
            parameters.put(LOGIN_ERROR, ERROR_USERNAME_MISSING);
        }
        if (userName == null || userName.length() == 0) {
            return null;
        }

        return new UserIdentificationInfo(userName, password);
    }

}
