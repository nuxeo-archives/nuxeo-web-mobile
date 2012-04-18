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
 * @author bjalon
 *
 */
public class AnonymousRequestHandler implements RequestHandler {

    private static final Log log = LogFactory.getLog(AnonymousRequestHandler.class);

    private UserManager um;

    @Override
    public boolean isRequestRedirectedToApplicationLoginForm(
            HttpServletRequest request) {
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

        String anonymousUsername;
        try {
            anonymousUsername = getAnonymousUsername();
        } catch (Exception e) {
            log.error("Can't fetch anonymous username", e);
            return false;
        }

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

    protected String getAnonymousUsername() throws Exception {
        if (um == null) {
            um = Framework.getService(UserManager.class);
        }
        return um.getAnonymousUserId();
    }

    @Override
    public RequestHandler init(Map<String, String> properties) {
        return this;
    }

}
