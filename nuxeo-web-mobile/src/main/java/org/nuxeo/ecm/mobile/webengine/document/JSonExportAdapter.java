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
package org.nuxeo.ecm.mobile.webengine.document;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

/**
 * Adapter that expose a document according the DForm Jquery Framework structure.
 * @author bjalon
 * 
 */
@WebAdapter(name = "jsonExport", type = "JSONExport", targetType = "MobileDocument")
@Produces("application/json;charset=UTF-8")
public class JSonExportAdapter extends DefaultAdapter {

    /**
     * Return the JSON export of document for dform layout manager 
     * @param action
     * @param actionType
     * @return
     */
    @GET
    @Path("dform")
    public Object doGet(@QueryParam("action") String action,
            @QueryParam("actionType") String actionType) {

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
