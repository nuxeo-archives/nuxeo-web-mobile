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
package org.nuxeo.ecm.mobile.webengine;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.ui.web.auth.service.PluggableAuthenticationService;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.WebMobileConstants.getWebMobileURL;
import static org.nuxeo.ecm.mobile.WebMobileConstants.TARGET_URL_PARAMETER;

/**
 * Manage authentication form and logout action
 *
 * @author bjalon
 *
 */
@WebObject(type = "WebMobileAuthentication")
@Produces("text/html;charset=UTF-8")
public class WebMobileAuthentication extends DefaultObject {

    private static final Log log = LogFactory.getLog(WebMobileAuthentication.class);

    private PluggableAuthenticationService service;

    private PluggableAuthenticationService getService() throws Exception {
        if (service == null && Framework.getRuntime() != null) {
            service = (PluggableAuthenticationService) Framework.getRuntime().getComponent(
                    PluggableAuthenticationService.NAME);
            // init preFilters
            service.initPreFilters();
            if (service == null) {
                log.error("Unable to get Service "
                        + PluggableAuthenticationService.NAME);
                throw new Exception(
                        "Can't initialize Nuxeo Pluggable Authentication Service");
            }
        }
        return service;

    }

    @GET
    @Path("login")
    public Object doLogin() {
        log.debug("login call: " + ctx.getRequest().getParameter("failed"));
        return getView("login-mobile");
    }

    @GET
    @Path("logout")
    public Object doLogout(@Context HttpServletResponse response,
            @Context HttpServletRequest request,
            @QueryParam(TARGET_URL_PARAMETER) String targetURL)
            throws Exception {

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        getService().invalidateSession(request);

        if (targetURL == null) {
            log.debug("Logout done: Redirect to default URL: " + targetURL);
            targetURL = getWebMobileURL();
        } else {
            log.debug("Logout done: Redirect to specified URL: " + targetURL);
        }
        return redirect(targetURL);
    }
}
