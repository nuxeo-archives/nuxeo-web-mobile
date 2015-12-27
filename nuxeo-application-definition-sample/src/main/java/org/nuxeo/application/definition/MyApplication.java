/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and others.
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

package org.nuxeo.application.definition;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.ui.web.auth.service.PluggableAuthenticationService;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.REQUESTED_URL;

/**
 * The root entry for the WebEngine module.
 *
 * @author bjalon
 */
@Path("/myApplication")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "MyApplication")
public class MyApplication extends ModuleRoot {
    private static final Log log = LogFactory.getLog(MyApplication.class);

    private PluggableAuthenticationService service;

    @GET
    public Object doGet() {
        return getView("index");
    }

    private PluggableAuthenticationService getService() {
        if (service == null && Framework.getRuntime() != null) {
            service = (PluggableAuthenticationService) Framework.getRuntime().getComponent(
                    PluggableAuthenticationService.NAME);
            // init preFilters
            service.initPreFilters();
        }
        return service;

    }

    @GET
    @Path("login")
    public Object doLogin() {
        return getView("login");
    }

    @GET
    @Path("logout")
    public Object doLogout(@Context HttpServletResponse response) {

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        getService().invalidateSession(request);

        String redirect = request.getParameter(REQUESTED_URL);
        if (redirect != null) {
            log.debug("Logout done: Redirect to default URL: " + redirect);
        } else {
            redirect = getContext().getBasePath();
        }
        return redirect(redirect);
    }
}
