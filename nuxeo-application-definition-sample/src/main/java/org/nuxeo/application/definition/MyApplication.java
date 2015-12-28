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
