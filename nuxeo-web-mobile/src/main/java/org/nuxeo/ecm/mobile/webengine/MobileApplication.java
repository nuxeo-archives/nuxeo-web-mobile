/*
 * (C) Copyright 2006-2012 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     Benjamin JALON <bjalon@nuxeo.com>
 */

package org.nuxeo.ecm.mobile.webengine;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.mobile.filter.RequestAdapter;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

/**
 * @author Benjamin JALON <bjalon@nuxeo.com>
 *
 *         Entry point of the webengine application
 */
@Path("mobile")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "MobileApplication")
public class MobileApplication extends ModuleRoot {

    private String nuxeoContextPath;

    /**
     * Home binding
     *
     */
    @GET
    public Object doGet() {
        RequestAdapter adapter = new RequestAdapter(request);
        String initialRequest = adapter.getInitialRequest();
        if (initialRequest == null
                || !initialRequest.startsWith(getNuxeoContextPath() + "/")
                || initialRequest.equals(getNuxeoContextPath() + "/")) {
            return getView("index");
        }
        // get the path after the context path + slash
        String relativePath = initialRequest.substring(getNuxeoContextPath().length() + 1);
        return redirect(getPath() + "/" + relativePath);
    }

    @Path("auth")
    public Object doAuthenticationComputation() {
        return ctx.newObject("WebMobileAuthentication");
    }

    @Path("nxdoc")
    public Object doFetchDocById() {
        return "nxdoc";
    }

    @Path("nxpath")
    public Object doFetchDocByPath() {
        return "nxpath";
    }

    @Path("proto")
    public Object doPrototype() {
        return ctx.newObject("Prototype");
    }

    private String getNuxeoContextPath() {
        if (nuxeoContextPath == null) {
            nuxeoContextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        }
        return nuxeoContextPath;
    }

}
