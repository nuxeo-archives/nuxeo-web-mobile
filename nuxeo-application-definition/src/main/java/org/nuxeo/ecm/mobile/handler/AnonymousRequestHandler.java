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
package org.nuxeo.ecm.mobile.handler;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.ui.web.auth.CachableUserIdentificationInfo;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.USERIDENT_KEY;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public class AnonymousRequestHandler implements RequestHandler {

    private static final Log log = LogFactory.getLog(AnonymousRequestHandler.class);

    @Override
    public boolean isRequestRedirectedToApplicationLoginForm(HttpServletRequest request) {
        // same logic
        return this.isRequestRedirectedToApplication(request);
    }

    @Override
    public boolean isRequestRedirectedToApplication(HttpServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String username = getUsernameFromRequest(httpRequest);

        if (username == null) {
            log.debug("No principal found in session, request not selected");
            return false;
        }

        String anonymousUsername = getAnonymousUsername();

        if (anonymousUsername.equals(username)) {
            return true;
        }

        return false;
    }

    protected String getUsernameFromRequest(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String result = null;

        if (principal != null) {
            result = principal.getName();
        } else {
            Object att = request.getSession().getAttribute(USERIDENT_KEY);
            if (att == null || !(att instanceof CachableUserIdentificationInfo)) {
                log.debug("No identity found in session, Application not selected");
                return null;
            }

            principal = ((CachableUserIdentificationInfo) att).getPrincipal();
            result = principal.getName();
        }
        log.debug("username fetched in session: " + result);
        return result;

    }

    protected String getAnonymousUsername() {
        return Framework.getService(UserManager.class).getAnonymousUserId();
    }

    @Override
    public RequestHandler init(Map<String, String> properties) {
        return this;
    }

}
