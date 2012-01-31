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

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

/**
 * Manage authentication form and logout action
 *
 * @author bjalon
 *
 */
@WebObject(type = "Search")
@Produces("text/html;charset=UTF-8")
public class Search extends DefaultObject {

    private static final Log log = LogFactory.getLog(Search.class);


    @GET
    public Object doLogin(@QueryParam("q") String queryString) {
        CoreSession session = ctx.getCoreSession();
        DocumentModelList docs;
        try {
            docs = session.query("SELECT * FROM Document WHERE ecm:fulltext = '" + queryString + "'");
        } catch (ClientException e) {
            log.error(e, e);
            throw new WebException(e.getMessage());
        };
        return getView("index").arg("docs", docs);
    }
    
}
