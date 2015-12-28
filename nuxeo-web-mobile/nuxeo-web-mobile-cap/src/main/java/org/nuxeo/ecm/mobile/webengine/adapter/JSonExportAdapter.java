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
package org.nuxeo.ecm.mobile.webengine.adapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;

/**
 * Adapter that expose a document according the DForm Jquery Framework structure.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "jsonExport", type = "JSONExport", targetType = "MobileDocument")
@Produces("application/json;charset=UTF-8")
public class JSonExportAdapter extends DefaultMobileAdapter {

    /**
     * Return the JSON export of document for dform layout manager
     * 
     * @param action
     * @param actionType
     * @return
     */
    @GET
    @Path("dform")
    public Object doGet(@QueryParam("action") String action, @QueryParam("actionType") String actionType) {

        if (action == null && ctx == null) {
            throw new WebException("No action given into parameters and no "
                    + "context found, can't generate json document export");
        }

        if (action == null) {
            action = ctx.getRequest().getRequestURI();
        }

        if (actionType == null) {
            actionType = "post";
        }

        return getView("index").render();
    }

}
