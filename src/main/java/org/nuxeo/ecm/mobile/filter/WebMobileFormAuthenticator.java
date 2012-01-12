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
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.ui.web.auth.interfaces.LoginResponseHandler;
import org.nuxeo.ecm.platform.ui.web.auth.plugins.FormAuthenticator;

;

/**
 * Filter that redirects user to mobile authentication form if Mobile Browser
 * and user not authenticated.
 *
 * @author bjalon
 *
 */
public class WebMobileFormAuthenticator extends FormAuthenticator implements
        LoginResponseHandler {

    protected static final Log log = LogFactory.getLog(WebMobileFormAuthenticator.class);

    @Override
    public Boolean needLoginPrompt(HttpServletRequest httpRequest) {
        RequestAdapter request = new RequestAdapterImpl(httpRequest);

        if (request.isMobileBrowser()) {
            log.debug("Mobile browser => Mobile login page redirect: "
                    + loginPage + ". target URL: " + request.getFullURL());
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public Boolean handleLoginPrompt(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, String baseURL) {
        return super.handleLoginPrompt(httpRequest, httpResponse, baseURL);
    }

    @Override
    public boolean onError(HttpServletRequest request,
            HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            response.sendRedirect(getUrlLoginPageClean(request) + "&failed=true");
        } catch (IOException e) {
            log.error(e);
            return false;
        }
        return true;
    }

    /**
     * return the Login page cleaned means without submit information (if we
     * navigate with this parameter the login module will parse the request and
     * without the password written. This method is used to generate the
     * redirect url if an error occured during the authentication.
     *
     */
    protected String getUrlLoginPageClean(HttpServletRequest request) {

        String url = request.getRequestURI() + "?";
        @SuppressWarnings("unchecked")
        Enumeration<String> names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!"Submit".equals(name) && !passwordKey.equals(name)) {
                url += name + "=" + request.getParameter(name) + "&";
            }
        }
        return url;
    }

    @Override
    public boolean onSuccess(HttpServletRequest request,
            HttpServletResponse response) {
        return false;
    }
}
