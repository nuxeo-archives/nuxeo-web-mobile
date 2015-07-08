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
package org.nuxeo.ecm.mobile.webengine.adapter;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "newDocument", type = "NewDocument", targetType = "MobileDocument")
public class NewDocumentAdapter extends DefaultMobileAdapter {

    private static final Log log = LogFactory.getLog(NewDocumentAdapter.class);

    @GET
    public Object doGet(@QueryParam("type") String docType) {
        CoreSession session = ctx.getCoreSession();

        DocumentModel doc;
        try {
            doc = session.createDocumentModel(docType);
        } catch (NuxeoException e) {
            log.error(e, e);
            throw new WebException(e.getMessage());
        }
        return getView("index").arg("doc", doc);
    }

    @POST
    public Object doPost() {
        return getView("index");
    }

}
